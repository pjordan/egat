/*
 * SymmetricReplicatorDynamics.java
 *
 * Copyright (C) 2006-2009 Patrick R. Jordan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package egat.replicatordynamics;

import egat.minform.SymmetricRationalizableFinder;
import egat.minform.LpSolveSymmetricRationalizableFinder;

import java.util.Set;
import java.util.Arrays;
import java.io.PrintStream;

import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.random.RandomData;

/**
 * @author Patrick Jordan
 */
public class SymmetricConstrainedAmoebaSearch {
    private double tolerance;
    private int maxIteration;
    private PrintStream printStream;
    private RandomData randomData;

    public SymmetricConstrainedAmoebaSearch(double tolerance, int maxIteration, PrintStream printStream) {
        this.tolerance = tolerance;
        this.maxIteration = maxIteration;
        this.printStream = printStream;
        this.randomData = new RandomDataImpl();

    }

    
    public SymmetricConstrainedAmoebaSearch(double tolerance, int maxIteration) {
        this(tolerance, maxIteration, null);
    }

    public Strategy run(SymmetricGame game, Strategy initialStrategy, Set<Action> restrictedActions) {
        return run(game, initialStrategy, restrictedActions, false);
    }

    public Strategy run(SymmetricGame game, Strategy initialStrategy, Set<Action> restrictedActions, boolean randomize) {


        Action[] actions = game.getActions().toArray(new Action[0]);

        boolean[] mask = new boolean[actions.length];

        int restrictedCount = 0;

        for (int i = 0; i < actions.length; i++) {

            mask[i] = restrictedActions.contains(actions[i]);

            if (mask[i]) {

                restrictedCount++;

            }

        }


        int[] restricted = new int[restrictedCount];

        for (int i = 0, restrictedIndex = 0; i < actions.length; i++) {

            if (mask[i]) {

                restricted[restrictedIndex++] = i;

            }
        }

        Player[] players = game.players().toArray(new Player[0]);

        PayoffMatrix pm = createPayoffMatrix(game, players, actions);

        // Normalize to a uniform distribution
        double[] distribution = new double[actions.length];

        double[] bestDist = new double[actions.length];


        if (initialStrategy == null) {
            double sum = 0.0;
            for (int i = 0; i < bestDist.length; i++) {
                if (mask[i]) {
                    bestDist[i] = Math.random();
                    sum += bestDist[i];
                }
            }

            for (int i = 0; i < bestDist.length; i++) {
                if (mask[i]) {
                    bestDist[i] /= sum;
                }
            }

        } else {
            for (int i = 0; i < actions.length; i++) {
                if (mask[i]) {
                    bestDist[i] = initialStrategy.getProbability(actions[i]).doubleValue();
                }
            }
        }

        double bestRegret = pm.regret(bestDist);

        // Initialize current strategy
        Strategy currentStrategy = buildStrategy(actions, bestDist, restricted);

        fireUpdatedStrategy(currentStrategy, 0, Double.NaN);


        int SIMPLICES = restricted.length;
        double[][] X = new double[SIMPLICES][SIMPLICES - 1];
        double[] centroid = new double[SIMPLICES - 1];
        double[] reflect = new double[SIMPLICES - 1];
        double[] expand = new double[SIMPLICES - 1];
        double[] contract = new double[SIMPLICES - 1];
        double[] V = new double[SIMPLICES];
        double[] cur = distribution.clone();


        if (!randomize) {
            for (int r = 0, n = SIMPLICES - 1; r < n; r++) {
                X[0][r] = 0.0;
            }

            V[0] = calculateObjective(pm, X[0], restricted, distribution);

            for (int s = 1; s < SIMPLICES; s++) {
                System.arraycopy(X[0], 0, X[s], 0, SIMPLICES - 1);

                X[s][s - 1] = 1.0;

                V[s] = calculateObjective(pm, X[s], restricted, distribution);
            }
        } else {


            for (int s = 0; s < SIMPLICES; s++) {
                generateSimplex(X[s]);
                V[s] = calculateObjective(pm, X[s], restricted, distribution);
            }
        }

        int highest = -1;
        int lowest = -1;
        int second = -1;

        for (int s = 0; s < SIMPLICES; s++) {
            if (lowest < 0 || V[s] < V[lowest]) {
                lowest = s;
            }

            if (second < 0 || V[s] > V[second]) {

                if (highest < 0 || V[s] > V[highest]) {
                    second = highest;
                    highest = s;
                } else {
                    second = s;
                }

            }
        }

        double curRegret = Double.POSITIVE_INFINITY;

        loop:
        for (int iteration = 1; SIMPLICES > 1; iteration++) {

            generateCentroid(centroid, X, SIMPLICES, highest);

            generateTowards(reflect, centroid, X[highest], 1.0);

            double reflectV = calculateObjective(pm, reflect, restricted, distribution);


            // Reflect
            if (V[lowest] <= reflectV && reflectV < V[second]) {
                replaceHighest(reflect, reflectV, X, V, highest);

                // Expand
            } else if (reflectV < V[lowest]) {

                generateTowards(expand, centroid, X[highest], 2.0);

                double expandV = calculateObjective(pm, expand, restricted, distribution);

                if (expandV < reflectV) {
                    replaceHighest(expand, expandV, X, V, highest);
                } else {
                    replaceHighest(reflect, reflectV, X, V, highest);
                }

                // Contract
            } else {

                generateAway(contract, X[highest], centroid, 0.5);

                double contractV = calculateObjective(pm, contract, restricted, distribution);

                if (contractV < V[highest]) {
                    replaceHighest(contract, contractV, X, V, highest);


                // Reduction
                } else {
                    for (int s = 0; s < SIMPLICES; s++) {
                        if (s != lowest) {

                            generateAway(X[s], X[lowest], X[s], 0.5);

                            V[s] = calculateObjective(pm, X[s], restricted, distribution);
                        }
                    }
                }
            }

            highest = -1;
            lowest = -1;
            second = -1;

            for (int s = 0; s < SIMPLICES; s++) {
                if (lowest < 0 || V[s] < V[lowest]) {
                    lowest = s;
                }

                if (second < 0 || V[s] > V[second]) {

                    if (highest < 0 || V[s] > V[highest]) {
                        second = highest;
                        highest = s;
                    } else {
                        second = s;
                    }

                }
            }


            Arrays.fill(cur, 0.0);
            double sum = 0.0;
            for (int r = 0; r < restricted.length - 1; r++) {
                cur[restricted[r]] = Math.max(0, X[lowest][r]);
                sum += cur[restricted[r]];
            }

            cur[restricted[restricted.length - 1]] = Math.max(0.0, 1 - sum);
            sum += cur[restricted[restricted.length - 1]];

            for (int r = 0; r < restricted.length; r++) {
                cur[restricted[r]] /= sum;
            }

            curRegret = pm.regret(cur);

            if (curRegret < bestRegret) {
                System.arraycopy(cur, 0, bestDist, 0, cur.length);
                bestRegret = curRegret;
            }

            // Build the new strategy
            currentStrategy = buildStrategy(actions, bestDist, restricted);

            fireUpdatedStrategy(currentStrategy, iteration, bestRegret);

            // Calculate the norm
            double norm = V[highest] - V[lowest];

            // Check termination condition
            if (terminate(norm, iteration))
                break;
        }

        return currentStrategy;
    }

    private void replaceHighest(double[] src, double srcV, double X[][], double[] V, int highest) {
        System.arraycopy(src, 0, X[highest], 0, src.length);
        V[highest] = srcV;
    }

    private void generateCentroid(double[] centroid, double[][] X, int simplices, int highest) {
        for (int r = 0; r < simplices - 1; r++) {
            centroid[r] = 0.0;

            for (int s = 0; s < simplices; s++) {
                if (s != highest) {
                    centroid[r] += X[s][r];
                }
            }

            centroid[r] /= simplices - 1.0;
        }
    }


    private void generateTowards(double[] newVertex, double[] first, double[] second, double scale) {
        for (int r = 0; r < newVertex.length; r++) {
            newVertex[r] = first[r] + scale * (first[r] - second[r]);
        }
    }

    private void generateAway(double[] newVertex, double[] first, double[] second, double scale) {
        generateTowards(newVertex, first, second, -scale);
    }

    private double calculateObjective(PayoffMatrix pm, double[] cur, int[] restricted, double[] distribution) {
        double sum = 0.0;
        for (int r = 0, n = restricted.length - 1; r < n; r++) {
            if (cur[r] < 0) {
                return Double.POSITIVE_INFINITY;
            }
            sum += cur[r];
        }

        if (sum > 1.0) {
            return Double.POSITIVE_INFINITY;
        }


        return calculateRegret(pm, cur, restricted, distribution);
    }

    private double calculateRegret(PayoffMatrix pm, double[] cur, int[] restricted, double[] distribution) {
        Arrays.fill(distribution, 0.0);

        double sum = 0.0;
        for (int r = 0; r < restricted.length - 1; r++) {
            distribution[restricted[r]] = cur[r];
            sum += cur[r];
        }

        distribution[restricted[restricted.length - 1]] = 1.0 - sum;


        return pm.regret(distribution);
    }

    private void fireUpdatedStrategy(Strategy currentStrategy, int iteration,
                                     double epsilon) {
        if (printStream != null) {
            printStream.println(String.format("(s: %s iteration: %d epsilon: %s", currentStrategy, iteration, epsilon));
        }
    }

    private Strategy buildStrategy(Action[] actions, double[] distribution, int[] restricted) {

        Action[] maskedActions = new Action[restricted.length];

        Number[] number = new Number[restricted.length];

        for (int i = 0; i < restricted.length; i++) {
            maskedActions[i] = actions[restricted[i]];
            number[i] = distribution[restricted[i]];
        }

        return Games.createStrategy(maskedActions, number);
    }

    private boolean terminate(double norm, int iteration) {
        return (norm < tolerance || iteration > maxIteration);
    }

    private PayoffMatrix createPayoffMatrix(SymmetricGame game, Player[] players, Action[] actions) {

        Action[] playerActions = new Action[players.length];

        PayoffMatrix pm = new PayoffMatrix(players.length, actions.length);

        int[] indices = new int[players.length];

        for (int i = 0; i < pm.getTotalSize(); i++) {
            pm.expand(i, indices);

            for (int j = 0; j < players.length; j++) {
                playerActions[j] = actions[indices[j]];
            }

            Outcome outcome = Games.createOutcome(players, playerActions);

            Payoff p = game.payoff(outcome);
            pm.setPayoff(i, p.getPayoff(players[0]).getValue());
        }


        return pm;
    }

    private void generateSimplex(double[] X) {
        double sum = 0.0;
        for (int i = 0; i < X.length; i++) {
            X[i] = randomData.nextExponential(1.0);
            sum += X[i];
        }
        sum += randomData.nextExponential(1.0);

        for (int i = 0; i < X.length; i++) {
            X[i] /= sum;
        }
    }
}