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
import java.io.PrintStream;

/**
 * @author Patrick Jordan
 */
public class SymmetricConstrainedNaiveSearch {
    private static final int INITIAL_ATTEMPTS = 10;
    private static final double PHI = 0.618034;
    private double tolerance;
    private int maxIteration;
    private PrintStream printStream;
    private SymmetricRationalizableFinder rationalizableFinder;

    public SymmetricConstrainedNaiveSearch(double tolerance, int maxIteration, PrintStream printStream, SymmetricRationalizableFinder rationalizableFinder) {
        this.tolerance = tolerance;
        this.maxIteration = maxIteration;
        this.printStream = printStream;
        this.rationalizableFinder = rationalizableFinder;

    }

    public SymmetricConstrainedNaiveSearch(double tolerance, int maxIteration, PrintStream printStream) {
        this(tolerance, maxIteration, printStream, new LpSolveSymmetricRationalizableFinder());

    }

    public SymmetricConstrainedNaiveSearch(double tolerance, int maxIteration) {
        this(tolerance, maxIteration, null);
    }

    public Strategy run(SymmetricGame game, Strategy initialStrategy, Set<Action> restrictedActions) {


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
        double bestRegret = Double.POSITIVE_INFINITY;

        if (initialStrategy == null) {
            for (int r = 0; r < INITIAL_ATTEMPTS; r++) {
                double sum = 0.0;
                for (int i = 0; i < distribution.length; i++) {
                    if (mask[i]) {
                        distribution[i] = Math.random();
                        sum += distribution[i];
                    }
                }

                for (int i = 0; i < distribution.length; i++) {
                    if (mask[i]) {
                        distribution[i] /= sum;
                    }
                }

                double regret = pm.regret(distribution);

                if (regret < bestRegret) {
                    System.arraycopy(distribution, 0, bestDist, 0, distribution.length);
                    bestRegret = regret;
                }
            }

            System.arraycopy(bestDist, 0, distribution, 0, distribution.length);

        } else {
            for (int i = 0; i < actions.length; i++) {
                if (mask[i]) {
                    distribution[i] = initialStrategy.getProbability(actions[i]).doubleValue();
                }
            }
        }

        // Initialize current strategy
        Strategy currentStrategy = buildStrategy(actions, distribution, restricted);

        fireUpdatedStrategy(currentStrategy, 0, Double.NaN);


        double delta = 1e-6;
        double[] direction = new double[restricted.length];
        double[] sampleDirection = new double[restricted.length];
        double[] cur = bestDist.clone();
        double curRegret = bestRegret;
        double[] old = cur.clone();

        loop:
        for (int iteration = 1; true; iteration++) {


            System.arraycopy(cur, 0, old, 0, cur.length);

            double sum = 0.0;

            double bestDeriv = Double.POSITIVE_INFINITY;

            for (int i = 0; i < 100; i++) {
                randomDirection(cur, restricted, sampleDirection);

                sum = 0.0;

                for (int r = 0; r < restricted.length; r++) {
                    distribution[restricted[r]] = cur[restricted[r]] + delta * sampleDirection[r];
                    sum += distribution[restricted[r]];
                }

                for (int r = 0; r < restricted.length; r++) {
                    distribution[restricted[r]] /= sum;
                }

                double deriv = (pm.regret(distribution) - curRegret) / delta;

                if (-deriv > bestDeriv) {
                    for (int r = 0; r < restricted.length; r++) {
                        direction[r] *= -sampleDirection[r];
                    }
                    bestDeriv = -deriv;
                } else if(deriv < bestDeriv) {
                    System.arraycopy(sampleDirection,0,direction,0,direction.length);
                    bestDeriv = deriv;
                }
            }

            double alpha = lineSearch(pm, cur, restricted, distribution, direction, 1e-2, 1e-10);

            sum = 0.0;
            for (int r = 0; r < restricted.length; r++) {
                cur[restricted[r]] = Math.max(0, cur[restricted[r]] + alpha * direction[r]);
                sum += cur[restricted[r]];
            }

            if (sum > 0) {
                for (int r = 0; r < restricted.length; r++) {
                    cur[restricted[r]] /= sum;
                }
            } else {
                for (int r = 0; r < restricted.length; r++) {
                    cur[restricted[r]] = 1.0 / restricted.length;
                }
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
            double norm = Linfinity(cur, old);

            // Check termination condition
            if (terminate(norm, iteration))
                break;
        }

        return currentStrategy;
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

    private double lineSearch(PayoffMatrix pm, double[] cur, int[] restricted, double[] distribution, double[] gradient, double scale, double tol) {


        double alphaMin = 0.0;
        double min = calculateRegret(pm, cur, restricted, distribution, gradient, alphaMin);


        for (double alpha = 0.0; alpha <= 1; alpha += scale) {
            double r = calculateRegret(pm, cur, restricted, distribution, gradient, alpha);
            if (r < min) {
                min = r;
                alphaMin = alpha;
            }
        }

        return lineSearchGS(pm, cur, restricted, distribution, gradient, alphaMin - scale, alphaMin + scale, tol);
    }


    private double lineSearchGS(PayoffMatrix pm, double[] cur, int[] restricted, double[] distribution, double[] gradient, double a, double b, double tol) {
        double x1 = a * PHI + (1 - PHI) * b;
        double r1 = calculateRegret(pm, cur, restricted, distribution, gradient, x1);

        double x2 = a * (1 - PHI) + (PHI) * b;
        double r2 = calculateRegret(pm, cur, restricted, distribution, gradient, x2);

        while (b - a > tol) {
            if (r1 < r2) {
                b = x2;
                x2 = x1;
                r2 = r1;

                x1 = PHI * a + (1 - PHI) * b;
                r1 = calculateRegret(pm, cur, restricted, distribution, gradient, x1);
            } else {
                a = x1;
                x1 = x2;
                r1 = r2;

                x2 = (1 - PHI) * a + PHI * b;
                r2 = calculateRegret(pm, cur, restricted, distribution, gradient, x2);
            }
        }

        return (b + a) / 2;
    }

    private double calculateRegret(PayoffMatrix pm, double[] cur, int[] restricted, double[] distribution, double[] gradient, double x) {
        double sum = 0.0;

        for (int r = 0; r < restricted.length; r++) {
            distribution[restricted[r]] = Math.max(0, cur[restricted[r]] + x * gradient[r]);
            sum += distribution[restricted[r]];
        }
        if (sum > 0) {
            for (int r = 0; r < restricted.length; r++) {
                distribution[restricted[r]] /= sum;
            }
        } else {
            for (int r = 0; r < restricted.length; r++) {
                distribution[restricted[r]] = 1.0 / restricted.length;
            }
        }
        return pm.regret(distribution);
    }

    private void fireUpdatedStrategy(Strategy currentStrategy, int iteration, double epsilon) {
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