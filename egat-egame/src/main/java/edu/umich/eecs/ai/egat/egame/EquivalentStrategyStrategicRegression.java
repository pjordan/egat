package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class EquivalentStrategyStrategicRegression implements StrategicRegression {
    protected StrategicGame game;
    protected Map<Player, Map<Action,Action>> actionConverter;

    public EquivalentStrategyStrategicRegression(StrategicGame game, Map<Player, Map<Action, Action>> actionConverter) {
        this.game = game;
        this.actionConverter = actionConverter;
    }


    public StrategicGame getStrategicGame() {
        return game;
    }

    public Payoff predict(Outcome outcome) {
        Player[] players = outcome.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

        for(int i = 0; i < players.length; i++) {
            actions[i] = actionConverter.get(players[i]).get(outcome.getAction(players[i]));
        }

        return game.payoff(Games.createOutcome(players,actions));
    }
}
