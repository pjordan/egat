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

/**
 * @author Patrick Jordan
 */
public class SymmetricConstrainedPatternSearch {
    private static final int INITIAL_ATTEMPTS = 10;
    private static final double PHI = 0.618034;
    private double tolerance;
    private int maxIteration;
    private PrintStream printStream;
    private SymmetricRationalizableFinder rationalizableFinder;

    public SymmetricConstrainedPatternSearch(double tolerance, int maxIteration, PrintStream printStream, SymmetricRationalizableFinder rationalizableFinder) {
        this.tolerance = tolerance;
        this.maxIteration = maxIteration;
        this.printStream = printStream;
        this.rationalizableFinder = rationalizableFinder;

    }

    public SymmetricConstrainedPatternSearch(double tolerance, int maxIteration, PrintStream printStream) {
        this(tolerance, maxIteration, printStream, new LpSolveSymmetricRationalizableFinder());

    }

    public SymmetricConstrainedPatternSearch(double tolerance, int maxIteration) {
        this(tolerance, maxIteration, null);
    }

    public Strategy run(SymmetricGame game, Strategy initialStrategy, Set<Action> restrictedActions) {
        return run(game,initialStrategy,restrictedActions,false);
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


        int SIMPLEX = restricted.length;
        double[] X = new double[SIMPLEX - 1];
        double[] test = new double[SIMPLEX - 1];
        double V = Double.POSITIVE_INFINITY;
        double[] cur = distribution.clone();


        if (!randomize) {
            for (int r = 0, n = SIMPLEX - 1; r < n; r++) {
                X[r] = 0.0;
            }

            V = calculateLagrangian(pm, X, restricted, distribution);

        } else {
            double sum = 0.0;

            for (int r = 0, n = SIMPLEX - 1; r < n; r++) {
                X[r] = Math.random();
                sum += X[r];
            }

            double alpha = Math.random();

            for (int r = 0, n = SIMPLEX - 1; r < n; r++) {
                X[r] /= (sum/alpha);
            }

            V = calculateLagrangian(pm, X, restricted, distribution);
        }


        double curRegret = Double.POSITIVE_INFINITY;

        double delta = 1.0;

        loop:
        for (int iteration = 1; SIMPLEX > 1; iteration++) {

            boolean updated = false;

            for(int s = 0; s < SIMPLEX; s++) {
                System.arraycopy(X,0,test,0,X.length);

                if(s != SIMPLEX-1) {
                    test[s] = X[s] + delta;

                    for(int r = 0; r < SIMPLEX-1; r++) {
                        if(s != r) {
                            test[r] *= (1.0-delta);
                        }
                    }
                } else {
                    for(int r = 0; r < SIMPLEX-1; r++) {
                        test[r] *= 1.0-delta;
                    }
                }

                double testV = calculateLagrangian(pm, test, restricted, distribution);

                if(testV < V) {
                    V = testV;
                    System.arraycopy(test,0,X,0,X.length);
                    updated = true;
                }

                System.arraycopy(X,0,test,0,X.length);
                
                if(s != SIMPLEX-1) {
                    test[s] = X[s] - delta;

                    for(int r = 0; r < SIMPLEX-1; r++) {
                        if(s != r) {
                            test[r] /= (1.0-delta);
                        }
                    }
                } else {
                    for(int r = 0; r < SIMPLEX-1; r++) {
                        test[r] /= 1.0-delta;
                    }
                }

                testV = calculateLagrangian(pm, test, restricted, distribution);

                if(testV < V) {
                    V = testV;
                    System.arraycopy(test,0,X,0,X.length);
                    updated = true;
                }
            }

            if(!updated) {
                delta = delta/2.0;
            }

            Arrays.fill(cur, 0.0);
            double sum = 0.0;
            for (int r = 0; r < restricted.length - 1; r++) {
                cur[restricted[r]] = Math.max(0, X[r]);
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
            double norm = delta;

            // Check termination condition
            if (terminate(norm, iteration))
                break;
        }

        return currentStrategy;
    }

    private double calculateLagrangian(PayoffMatrix pm, double[] cur, int[] restricted, double[] distribution) {
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
}