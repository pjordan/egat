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
package edu.umich.eecs.ai.egat.replicatordynamics;

import edu.umich.eecs.ai.egat.game.*;
import edu.umich.eecs.ai.egat.minform.SymmetricRationalizableFinder;
import edu.umich.eecs.ai.egat.minform.LpSolveSymmetricRationalizableFinder;

import java.util.Set;
import java.util.Arrays;
import java.io.PrintStream;

import org.apache.commons.math.distribution.GammaDistributionImpl;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.random.RandomData;

/**
 * @author Patrick Jordan
 */
public class SymmetricConstrainedTransformedAmoebaSearch {
    private static final int INITIAL_ATTEMPTS = 10;
    private static final double PHI = 0.618034;
    private double tolerance;
    private int maxIteration;
    private PrintStream printStream;
    private SymmetricRationalizableFinder rationalizableFinder;
    private RandomData randomData;

    public SymmetricConstrainedTransformedAmoebaSearch(double tolerance, int maxIteration, PrintStream printStream, SymmetricRationalizableFinder rationalizableFinder) {
        this.tolerance = tolerance;
        this.maxIteration = maxIteration;
        this.printStream = printStream;
        this.rationalizableFinder = rationalizableFinder;
        this.randomData = new RandomDataImpl();

    }

    public SymmetricConstrainedTransformedAmoebaSearch(double tolerance, int maxIteration, PrintStream printStream) {
        this(tolerance, maxIteration, printStream, new LpSolveSymmetricRationalizableFinder());

    }

    public SymmetricConstrainedTransformedAmoebaSearch(double tolerance, int maxIteration) {
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
        double[][] X = new double[SIMPLICES][SIMPLICES];
        double[] centroid = new double[SIMPLICES];
        double[] reflect = new double[SIMPLICES];
        double[] expand = new double[SIMPLICES];
        double[] contract = new double[SIMPLICES];
        double[] V = new double[SIMPLICES];
        double[] cur = distribution.clone();


        if (!randomize) {
            for (int s = 0;s < SIMPLICES; s++) {
                X[s][s] = 1.0;
                V[s] = calculateLagrangian(pm, X[s], restricted, distribution);
            }
        } else {
            for (int s = 0; s < SIMPLICES; s++) {
                generateSimplex(X[s]);
                V[s] = calculateLagrangian(pm, X[s], restricted, distribution);
            }
        }

        int highest = -1;
        int lowest = -1;
        int second = -1;

        for (int s = 0; s < SIMPLICES; s++) {
            if (highest < 0 || V[s] > V[highest]) {
                highest = s;
            }

            if (second < 0 || V[s] < V[second]) {

                if (lowest < 0 || V[s] < V[lowest]) {
                    second = lowest;
                    lowest = s;
                } else {
                    second = s;
                }
            }
        }

        double curRegret = Double.POSITIVE_INFINITY;

        loop:
        for (int iteration = 1; SIMPLICES > 1; iteration++) {


            for (int r = 0; r < SIMPLICES - 1; r++) {
                centroid[r] = 0.0;

                for (int s = 0; s < SIMPLICES; s++) {
                    if (s != highest) {
                        centroid[r] += X[s][r];
                    }
                }

                centroid[r] /= SIMPLICES - 1.0;
            }


            for (int r = 0; r < SIMPLICES - 1; r++) {
                reflect[r] = 2 * centroid[r] - X[highest][r];
            }

            double reflectV = calculateLagrangian(pm, reflect, restricted, distribution);


            // Reflect
            if (V[lowest] <= reflectV && reflectV < V[second]) {
                System.arraycopy(reflect, 0, X[highest], 0, SIMPLICES - 1);
                V[highest] = reflectV;
            } else {

                boolean shrink = false;

                // Expand
                if (reflectV < V[lowest]) {
                    for (int r = 0; r < SIMPLICES - 1; r++) {
                        expand[r] = centroid[r] + 2.0 * (reflect[r] - centroid[r]);
                    }

                    double expandV = calculateLagrangian(pm, expand, restricted, distribution);

                    if (expandV < reflectV) {
                        System.arraycopy(expand, 0, X[highest], 0, SIMPLICES - 1);
                        V[highest] = expandV;
                    } else {
                        System.arraycopy(reflect, 0, X[highest], 0, SIMPLICES - 1);
                        V[highest] = reflectV;
                    }

                    // Contract
                } else if (reflectV >= V[second]) {

                    //Outside
                    if (V[highest] > reflectV) {
                        for (int r = 0; r < SIMPLICES - 1; r++) {
                            contract[r] = centroid[r] + 0.5 * (reflect[r] - centroid[r]);
                        }
                        double contractV = calculateLagrangian(pm, contract, restricted, distribution);

                        if (contractV < reflectV) {
                            System.arraycopy(contract, 0, X[highest], 0, SIMPLICES - 1);
                            V[highest] = contractV;
                        } else {
                            shrink = true;
                        }

                        //Inside
                    } else {
                        for (int r = 0; r < SIMPLICES - 1; r++) {
                            contract[r] = centroid[r] + 0.5 * (X[highest][r] - centroid[r]);
                        }

                        double contractV = calculateLagrangian(pm, contract, restricted, distribution);

                        if (contractV < V[highest]) {
                            System.arraycopy(contract, 0, X[highest], 0, SIMPLICES - 1);
                            V[highest] = contractV;
                        } else {
                            shrink = true;
                        }
                    }


                    // Shrink
                    if (shrink) {
                        for (int s = 0; s < SIMPLICES; s++) {
                            if (s != lowest) {
                                for (int r = 0; r < SIMPLICES - 1; r++) {
                                    X[s][r] = X[lowest][r] + 0.5 * (X[s][r] - X[lowest][r]);
                                }

                                V[s] = calculateLagrangian(pm, X[s], restricted, distribution);
                            }
                        }
                    }
                }
            }

            highest = -1;
            lowest = -1;
            second = -1;

            for (int s = 0; s < SIMPLICES; s++) {
                if (highest < 0 || V[s] > V[highest]) {
                    highest = s;
                }

                if (second < 0 || V[s] < V[second]) {

                    if (lowest < 0 || V[s] < V[lowest]) {
                        second = lowest;
                        lowest = s;
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

    private double calculateLagrangian(PayoffMatrix pm, double[] cur, int[] restricted, double[] distribution) {
        return calculateRegret(pm, cur, restricted, distribution);
    }

    private void randomDirection(double[] current, int[] restricted, double[] gradient) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < restricted.length; i++) {
            gradient[i] = Math.random() - current[restricted[i]];
            max = Math.max(max, Math.abs(gradient[i]));
        }


        for (int i = 0; i < restricted.length; i++) {
            gradient[i] /= max;
        }
    }


    private double calculateRegret(PayoffMatrix pm, double[] cur, int[] restricted, double[] distribution) {
        Arrays.fill(distribution, 0.0);

        double sum = 0.0;
        for (int r = 0; r < restricted.length; r++) {
            distribution[restricted[r]] = Math.abs(cur[r]);
            sum += distribution[restricted[r]];
        }

        for (int r = 0; r < restricted.length; r++) {
            distribution[restricted[r]] /= sum;
        }
        
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

    private double Linfinity(double[] a, double[] b) {
        double norm = 0;

        for (int i = 0; i < a.length; i++) {
            norm = Math.max(Math.abs(a[i] - b[i]), norm);
        }

        return norm;
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
        for(int i = 0; i < X.length; i++) {
            X[i] = randomData.nextUniform(0.0,1.0);
        }        
    }
}