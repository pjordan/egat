package edu.umich.eecs.ai.egat.game;

import java.io.PrintStream;
import java.util.Set;

/**
 * @author Patrick Jordan
 */
public class SymmetricIteratedDominatedStrategyRemover {
    private PrintStream printStream;

    public SymmetricIteratedDominatedStrategyRemover() {
        this(null);
    }

    public SymmetricIteratedDominatedStrategyRemover(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void removeDominatedStrategies(DefaultSymmetricGame game) {
        removeDominatedStrategies(game, 1);
    }

    private void removeDominatedStrategies(DefaultSymmetricGame game, int round) {
        if (printStream!=null) {
            printStream.println(String.format("Round %d", round));
        }

        loop:
        for (Action a : (Set<Action>) game.getActions()) {
            Action dominatingAction = null;
            double dominatingGain = Double.NEGATIVE_INFINITY;

            for (Action b : (Set<Action>) game.getActions()) {
                double minGain = Double.POSITIVE_INFINITY;

                if (b.equals(a))
                    continue;

                for (SymmetricOutcome outcome : Games.symmetricTraversal(game)) {
                    if (outcome.getCount(a) > 0) {
                        for (SymmetricOutcome deviation : DeviationFactory.deviationTraversal(outcome, game)) {
                            Player[] players = Games.findDeviatingPlayers(deviation, outcome);

                            if (outcome.getAction(players[1]).equals(a) && deviation.getAction(players[0]).equals(b)) {
                                double deviationGain = Games.deviationGain(game.payoff(outcome), game.payoff(deviation), players);

                                if (deviationGain < minGain) {
                                    minGain = deviationGain;
                                }
                            }
                        }
                    }
                }


                if (minGain >= dominatingGain) {
                    dominatingAction = b;
                    dominatingGain = minGain;

                    if (minGain >= 0) {
                        if (printStream!=null) {
                            printStream.println(String.format("%s: %f %s", a, dominatingGain, dominatingAction));
                            printStream.println(String.format("%s dominated by %s", a, dominatingAction));
                        }

                        game.removeAction(a);

                        removeDominatedStrategies(game, round + 1);

                        break loop;
                    }
                }
            }

            if (printStream!=null) {
                printStream.println(String.format("%s: %f %s", a, dominatingGain, dominatingAction));
            }
        }
    }
}
