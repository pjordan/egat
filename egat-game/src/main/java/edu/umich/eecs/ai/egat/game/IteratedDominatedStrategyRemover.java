package edu.umich.eecs.ai.egat.game;

import java.io.PrintStream;
import java.util.Set;

/**
 * @author Patrick Jordan
 */
public class IteratedDominatedStrategyRemover {
    private PrintStream printStream;

    public IteratedDominatedStrategyRemover() {
        this(null);
    }

    public IteratedDominatedStrategyRemover(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void removeDominatedStrategies(DefaultStrategicGame game) {
        removeDominatedStrategies(game, 1);
    }

    private void removeDominatedStrategies(DefaultStrategicGame game, int round) {
        if (printStream!=null) {
            printStream.println(String.format("Round %d", round));
        }

        loop:
        for (Player player : game.players()) {
            for (Action a : (Set<Action>) game.getActions(player)) {
                Action dominatingAction = null;
                double dominatingGain = Double.NEGATIVE_INFINITY;


                for (Action b : (Set<Action>) game.getActions(player)) {
                    double minGain = Double.POSITIVE_INFINITY;

                    if (b.equals(a))
                        continue;

                    for (Outcome outcome : Games.traversal(game)) {
                        if (outcome.getAction(player).equals(a)) {
                            for (Outcome deviation : DeviationFactory.deviationTraversal(outcome, game, player)) {
                                if (deviation.getAction(player).equals(b)) {
                                    double deviationGain = Games.deviationGain(game.payoff(outcome), game.payoff(deviation), player);

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

                            game.removeAction(player, a);

                            removeDominatedStrategies(game, round + 1);

                            break loop;
                        }
                    }
                }
                if (printStream!=null) {
                    printStream.println(String.format("[%s] %s: %f %s", player, a, dominatingGain, dominatingAction));
                }
            }

        }
    }
}
