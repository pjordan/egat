/*
 * EquivalentStrategySymmetricRegression.java
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
import java.util.HashMap;

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
