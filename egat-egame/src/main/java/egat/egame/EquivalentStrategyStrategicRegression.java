/*
 * EquivalentStrategyStrategicRegression.java
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
package egat.egame;

import egat.game.*;

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
