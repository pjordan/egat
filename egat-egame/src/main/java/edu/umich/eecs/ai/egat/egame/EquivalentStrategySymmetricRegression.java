package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class EquivalentStrategySymmetricRegression implements SymmetricRegression {
    protected SymmetricGame game;
    protected Map<Action, Action> actionConverter;


    public EquivalentStrategySymmetricRegression(SymmetricGame game, Map<Action, Action> actionConverter) {
        this.game = game;
        this.actionConverter = actionConverter;
    }

    public SymmetricGame getSymmetricGame() {
        return game;
    }

    public SymmetricPayoff predict(SymmetricOutcome outcome) {
        Player[] players = outcome.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

        for(int i = 0; i < players.length; i++) {
            actions[i] = actionConverter.get(outcome.getAction(players[i]));
        }

        SymmetricOutcome reducedOutcome = Games.createSymmetricOutcome(players,actions);

        SymmetricPayoff reducedPayoff = game.payoff(reducedOutcome);

        Map<Action,PayoffValue> map = new HashMap<Action,PayoffValue>();

        for(Map.Entry<Action,Integer> entry : outcome.actionEntrySet()) {
            map.put(entry.getKey(), reducedPayoff.getPayoff(actionConverter.get(entry.getKey())));
        }

        return PayoffFactory.createSymmetricPayoff(map,outcome);
    }
}
