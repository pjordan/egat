package edu.umich.eecs.ai.egat.dominance;

import edu.umich.eecs.ai.egat.game.*;
import static edu.umich.eecs.ai.egat.dominance.DominanceUtils.*;

/**
 * @author Patrick Jordan
 */
public class PureSymmetricDominanceTesterImpl implements SymmetricDominanceTester {
    public boolean isDominated(Action action, SymmetricGame game) {
        Player[] players = game.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

        int playerIndex = 0;

        SymmetricMultiAgentSystem reduced = createPlayerReducedSymmetricSimulation(players[playerIndex], game);

        for (Action a : game.getActions()) {

            if(a.equals(action)) {
                continue;
            }

            boolean dominated = true;

            for (SymmetricOutcome outcome : Games.symmetricTraversal(reduced)) {
                if(isPayoffUndominated(action, a, game, outcome, players, actions, playerIndex)) {
                    dominated = false;
                    break;
                }
            }

            if (dominated) {
                return true;
            }
        }

        return false;
    }

    public boolean isDominated(Strategy strategy, SymmetricGame game) {
        Player[] players = game.players().toArray(new Player[0]);
        Strategy[] strategies = new Strategy[players.length];

        int playerIndex = 0;

        SymmetricMultiAgentSystem reduced = createPlayerReducedSymmetricSimulation(players[playerIndex], game);

        for (Action a : game.getActions()) {
            Strategy s = Games.createPureStrategy(a);

            if(s.equals(strategy)) {
                continue;
            }

            boolean dominated = true;

            for (SymmetricOutcome outcome : Games.symmetricTraversal(reduced)) {
                if(isPayoffUndominated(strategy, s, game, outcome, players, strategies, playerIndex)) {
                    dominated = false;
                    break;
                }
            }

            if (dominated) {
                return true;
            }
        }

        return false;
    }

    protected static boolean isPayoffUndominated(Action baseAction, Action deviantAction, StrategicGame game,
                                               Outcome background, Player[] players, Action[] actions, int index) {
        for(int i = 0; i < players.length; i++) {
            if(i!=index) {
                actions[i] = background.getAction(players[i]);
            }
        }

        actions[index] = baseAction;

        double basePayoff = game.payoff(Games.createOutcome(players,actions)).getPayoff(players[index]).getValue();

        actions[index] = deviantAction;

        double deviantPayoff = game.payoff(Games.createOutcome(players,actions)).getPayoff(players[index]).getValue();

        return basePayoff >= deviantPayoff;
    }

    protected static boolean isPayoffUndominated(Strategy baseStrategy, Strategy deviantStrategy, StrategicGame game,
                                               Outcome background, Player[] players, Strategy[] strategies, int index) {
        for(int i = 0; i < players.length; i++) {
            if(i!=index) {
                strategies[i] = Games.createPureStrategy(background.getAction(players[i]));
            }
        }

        strategies[index] = baseStrategy;

        double basePayoff = game.payoff(Games.createProfile(players,strategies)).getPayoff(players[index]).getValue();

        strategies[index] = deviantStrategy;

        double deviantPayoff = game.payoff(Games.createProfile(players,strategies)).getPayoff(players[index]).getValue();

        return basePayoff >= deviantPayoff;
    }
}
