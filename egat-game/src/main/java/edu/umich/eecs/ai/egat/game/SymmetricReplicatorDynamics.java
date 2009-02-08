package edu.umich.eecs.ai.egat.game;

import java.util.Set;
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

        Strategy[] strategies = new Strategy[players.length];

        // Normalize to a uniform distribution
        double[] distribution = new double[actions.length];

        double norm = 1.0 / distribution.length;
        if (initialStrategy == null) {
            for (int i = 0; i < distribution.length; i++) {
                distribution[i] = norm;
            }
        } else {
            for (int i = 0; i < actions.length; i++) {
                distribution[i] = initialStrategy.getProbability(actions[i]).doubleValue();
            }
        }


        // Lowest possible payoff
        double W = Double.POSITIVE_INFINITY;

        for(SymmetricOutcome symmetricOutcome : Games.symmetricTraversal(game) ) {
            SymmetricPayoff payoff = game.payoff(symmetricOutcome);
            for(Action action : (Set<Action>)payoff.actions()) {
                W = Math.min(W, payoff.getPayoff(action).getValue());
            }
        }


        // Initialize current strategy
        Strategy currentStrategy = buildStrategy(actions, distribution);

        fireUpdatedStrategy(currentStrategy, Double.POSITIVE_INFINITY, 0, Double.NaN);

        loop:
        for (int iteration = 1; true; iteration++) {

            double epsilon = 0;


            // Zero all expected payoffs
            double[] payoffs = new double[actions.length];

            // Create a mixed profile of all players
            // playing the current strategy.
            for (int i = 0; i < players.length; i++) {
                strategies[i] = currentStrategy;
            }


            double currentPayoff = game.payoff(Games.createProfile(players, strategies)).getPayoff(players[0]).getValue();

            // Switch the first players strategy
            for (int i = 0; i < actions.length; i++) {
                Strategy s = buildPureStrategy(actions, i);

                strategies[0] = s;

                Payoff payoff = game.payoff(Games.createProfile(players, strategies));


                payoffs[i] = payoff.getPayoff(players[0]).getValue();
            }

            // Get the next distribution
            double[] next = nextDistribution(payoffs, distribution, W);

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
        if(printStream!=null) {
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
            next[i] /= sum;
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

    private Strategy buildPureStrategy(Action[] actions, int index) {
        Number[] number = new Number[actions.length];
        for (int i = 0; i < number.length; i++) {
            if (i == index) {
                number[i] = 1.0;
            } else {
                number[i] = 0.0;
            }
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

}