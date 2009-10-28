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
import java.io.PrintStream;

/**
 * @author Patrick Jordan
 */
public class SymmetricConstrainedReplicatorDynamics {
    private static final double MULTIPLIER = 2.0;
    private static final int INITIAL_ATTEMPTS = 1000;
    private double tolerance;
    private int maxIteration;
    private PrintStream printStream;
    private SymmetricRationalizableFinder rationalizableFinder;

    public SymmetricConstrainedReplicatorDynamics(double tolerance, int maxIteration, PrintStream printStream, SymmetricRationalizableFinder rationalizableFinder) {
        this.tolerance = tolerance;
        this.maxIteration = maxIteration;
        this.printStream = printStream;
        this.rationalizableFinder = rationalizableFinder;

    }
    public SymmetricConstrainedReplicatorDynamics(double tolerance, int maxIteration, PrintStream printStream) {
        this(tolerance, maxIteration, printStream, new LpSolveSymmetricRationalizableFinder());

    }

    public SymmetricConstrainedReplicatorDynamics(double tolerance, int maxIteration) {
        this(tolerance, maxIteration, null);
    }

    public Strategy run(SymmetricGame game, Strategy initialStrategy, Set<Action> restrictedActions) {



        Action[] actions = game.getActions().toArray(new Action[0]);

        boolean[] mask = new boolean[actions.length];

        int restrictedCount = 0;

        for(int i = 0; i < actions.length; i++) {

            mask[i] = restrictedActions.contains(actions[i]);

            if(mask[i]) {

                restrictedCount++;

            }

        }


        int[] restricted = new int[restrictedCount];

        for(int i = 0, restrictedIndex = 0; i < actions.length; i++) {

            if(mask[i]) {

                restricted[restrictedIndex++] = i;

            }
        }

        Player[] players = game.players().toArray(new Player[0]);

        PayoffMatrix pm = createPayoffMatrix(game, players, actions);
        
        // Normalize to a uniform distribution
        double[] distribution = new double[actions.length];

        double norm = 1.0 / restrictedActions.size();

        if (initialStrategy == null) {


            double[] bestDist = new double[actions.length];
            double bestRegret = Double.POSITIVE_INFINITY;

            for(int r = 0; r < INITIAL_ATTEMPTS; r++) {
                double sum = 0.0;
                for (int i = 0; i < distribution.length; i++) {
                    if(mask[i]) {
                        distribution[i] = Math.random();
                        sum += distribution[i];
                    }
                }

                for (int i = 0; i < distribution.length; i++) {
                    if(mask[i]) {
                        distribution[i] /= sum;
                    }
                }

                double regret = pm.regret(distribution);

                if(regret < bestRegret) {
                    System.arraycopy(distribution,0,bestDist,0,distribution.length);
                    bestRegret = regret;
                }
            }

            System.arraycopy(bestDist,0,distribution,0,distribution.length);
            
        } else {
            for (int i = 0; i < actions.length; i++) {
                if(mask[i]) {
                    distribution[i] = initialStrategy.getProbability(actions[i]).doubleValue();
                }
            }
        }

        // Initialize current strategy
        Strategy currentStrategy = buildStrategy(actions, distribution, restricted);

        fireUpdatedStrategy(currentStrategy, Double.POSITIVE_INFINITY, 0, Double.NaN);

        SymmetricGame reducedGame = new ActionReducedSymmetricGame(game, restrictedActions);

        double W = rationalizableFinder.rationalizableEpsilon(reducedGame, game);        
        double maxRegret = Double.NEGATIVE_INFINITY;

        loop:
        for (int iteration = 1; true; iteration++) {

            // Zero all expected payoffs
            double[] regret = new double[restricted.length];



            double epsilon = pm.regret(distribution);


            double[] deviation = new double[distribution.length];

            // Switch the first players strategy
            for (int i = 0; i < restricted.length; i++) {

                deviation[restricted[i]] = 1.0;

                regret[i] = pm.regret(distribution,deviation);

                deviation[restricted[i]] = 0.0;

                maxRegret = Math.max(maxRegret,regret[i]);
            }


            // Get the next distribution
            double[] next = nextDistribution(restricted, regret, distribution, Math.max(MULTIPLIER*maxRegret,W));

            // Calculate the norm
            norm = Linfinity(next, distribution);

            // Update the probability distribution
            distribution = next;

            // Build the new strategy
            currentStrategy = buildStrategy(actions, distribution, restricted);

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


    private double[] nextDistribution(int[] restricted, double[] regret, double[] distribution, double worstRegret) {
        double[] next = distribution.clone();
        double sum = 0.0;

        if (worstRegret > 0) {
            for (int i = 0; i < restricted.length; i++) {
                next[restricted[i]] *= worstRegret - regret[i];
                sum += next[restricted[i]];
            }

            for (int i = 0; i < restricted.length; i++) {
                if (sum > 0) {
                    next[restricted[i]] /= sum;
                } else {
                    next[restricted[i]] = 1.0 / distribution.length;
                }
            }
        }

        return next;
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

        for(int i = 0; i < pm.getTotalSize(); i++) {
            pm.expand(i,indices);

            for(int j = 0; j < players.length; j++) {
                playerActions[j] = actions[indices[j]];
            }

            Outcome outcome = Games.createOutcome(players, playerActions);

            Payoff p = game.payoff(outcome);
            pm.setPayoff(i,p.getPayoff(players[0]).getValue());
        }


        return pm;
    }
}