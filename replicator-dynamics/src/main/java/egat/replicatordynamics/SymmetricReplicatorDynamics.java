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

import java.util.Arrays;
import java.io.PrintStream;

/**
 * @author Patrick Jordan
 */
public class SymmetricReplicatorDynamics {
    private double tolerance;
    private int maxIteration;
    private PrintStream printStream;

    public SymmetricReplicatorDynamics(double tolerance, int maxIteration, PrintStream printStream) {
        this.tolerance = tolerance;
        this.maxIteration = maxIteration;
        this.printStream = printStream;

    }

    public SymmetricReplicatorDynamics(double tolerance, int maxIteration) {
        this(tolerance, maxIteration, null);
    }

    public Strategy run(SymmetricGame game, Strategy initialStrategy) {


        Action[] actions = gameActions(game);

        Player[] players = game.players().toArray(new Player[0]);

        PayoffMatrix pm = createPayoffMatrix(game, players, actions);

        // Normalize to a uniform distribution
        double[] distribution = new double[actions.length];

        double norm = 1.0 / actions.length;

        if (initialStrategy == null) {


            Arrays.fill(distribution, norm);

        } else {
            for (int i = 0; i < actions.length; i++) {
                distribution[i] = initialStrategy.getProbability(actions[i]).doubleValue();
            }
        }

        // Initialize current strategy
        Strategy currentStrategy = buildStrategy(actions, distribution);

        fireUpdatedStrategy(currentStrategy, Double.POSITIVE_INFINITY, 0, Double.NaN);

        double W = pm.minPayoff();

        loop:
        for (int iteration = 1; true; iteration++) {

            // Zero all expected payoffs
            double[] payoffs = new double[distribution.length];


            double currentPayoff = pm.getProfilePayoff(distribution);


            double[] deviation = new double[distribution.length];

            // Switch the first players strategy
            for (int i = 0; i < payoffs.length; i++) {

                deviation[i] = 1.0;

                payoffs[i] = pm.getProfilePayoff(distribution, deviation);

                deviation[i] = 0.0;
            }


            // Get the next distribution
            double[] next = nextDistribution(payoffs, distribution, W);

            double epsilon = 0;

            for (double payoff : payoffs) {
                epsilon = Math.max(payoff - currentPayoff, epsilon);
            }

            // Calculate the norm
            norm = Linfinity(next, distribution);

            // Update the probability distribution
            distribution = next;

            // Build the new strategy
            currentStrategy = buildStrategy(actions, distribution);

            fireUpdatedStrategy(currentStrategy, norm, iteration, epsilon);

            // Check termination condition
            if (terminate(norm, iteration))
                break;
        }
        
        return currentStrategy;
    }

    private void fireUpdatedStrategy(Strategy currentStrategy, double norm, int iteration, double epsilon) {
        if (printStream != null) {
            printStream.println(String.format("(s: %s norm: %f iteration: %d epsilon: %s", currentStrategy, norm, iteration, epsilon));
        }
    }


    private double[] nextDistribution(double[] payoffs, double[] distribution, double W) {
        double[] next = distribution.clone();
        double sum = 0;

        for (int i = 0; i < distribution.length; i++) {
            next[i] *= payoffs[i] - W;
            sum += next[i];
        }

        for (int i = 0; i < distribution.length; i++) {
            if (sum > 0) {
                next[i] /= sum;
            } else {
                next[i] = 1.0 / distribution.length;
            }
        }

        return next;
    }

    private Strategy buildStrategy(Action[] actions, double[] distribution) {
        Number[] number = new Number[distribution.length];
        for (int i = 0; i < number.length; i++) {
            number[i] = distribution[i];
        }

        return Games.createStrategy(actions, number);
    }

    private Action[] gameActions(StrategicGame game) {
        Action[] actions = new Action[0];
        for (Player player : game.players()) {
            actions = game.getActions(player).toArray(actions);
            break;
        }
        return actions;
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
