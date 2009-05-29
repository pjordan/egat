package edu.umich.eecs.ai.egat.dominance;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class DominanceUtils {
    private DominanceUtils() {
    }

    public static int indexForPlayer(Player player, Player[] players) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(player)) {
                return i;
            }
        }

        return -1;
    }

    public static StrategicMultiAgentSystem createPlayerReducedStrategicSimulation(Player player, StrategicMultiAgentSystem simulation) {
        Set<Player> otherPlayers = new HashSet<Player>(simulation.players());
        otherPlayers.remove(player);

        return new PlayerReducedStrategicMultiAgentSystem(simulation, otherPlayers);
    }

    public static SymmetricMultiAgentSystem createPlayerReducedSymmetricSimulation(Player player, SymmetricMultiAgentSystem simulation) {
        Set<Player> otherPlayers = new HashSet<Player>(simulation.players());
        otherPlayers.remove(player);

        return new PlayerReducedSymmetricMultiAgentSystem(simulation, otherPlayers);
    }

    public static double getPayoff(Action action, Outcome outcome, Player[] players, Action[] actions, StrategicGame game, int index) {
        for (int i = 0; i < players.length; i++) {
            if (i != index) {
                actions[i] = outcome.getAction(players[i]);
            }
        }

        actions[index] = action;

        return game.payoff(Games.createOutcome(players, actions)).getPayoff(players[index]).getValue();
    }

    public static double getPayoff(Strategy strategy, Outcome outcome, Player[] players, Strategy[] strategies, StrategicGame game, int index) {
        for (int i = 0; i < players.length; i++) {
            if (i != index) {
                strategies[i] = Games.createPureStrategy(outcome.getAction(players[i]));
            }
        }

        strategies[index] = strategy;

        return game.payoff(Games.createProfile(players, strategies)).getPayoff(players[index]).getValue();
    }
}
