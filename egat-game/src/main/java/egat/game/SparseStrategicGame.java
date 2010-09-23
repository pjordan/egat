/*
 * SparseStrategicGame.java
 *
 * Copyright (C) 2006-2010 Patrick R. Jordan
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
package egat.game;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class SparseStrategicGame<T extends PayoffValue> extends AbstractStrategicGame<T> {
    private Map<Player,Set<Action>> playerActions;
    private Map<Outcome,Payoff<T>> outcomePayoffs;


    public SparseStrategicGame() {
        playerActions = new HashMap<Player,Set<Action>>();
        outcomePayoffs = new HashMap<Outcome,Payoff<T>>();
    }

    public SparseStrategicGame(String name) {
        super(name);
        playerActions = new HashMap<Player,Set<Action>>();
        outcomePayoffs = new HashMap<Outcome,Payoff<T>>();
    }

    public SparseStrategicGame(String name, String description) {
        super(name, description);
        playerActions = new HashMap<Player,Set<Action>>();
        outcomePayoffs = new HashMap<Outcome,Payoff<T>>();
    }

    public Set<Action> getActions(Player player) {
        return playerActions.get(player);
    }

    public Payoff<T> payoff(Outcome outcome) {
        return outcomePayoffs.get(outcome);
    }

    public void putActions(Player player, Set<Action> actions) {
        playerActions.put(player,actions);
    }

    public void putPayoff(Outcome outcome, Payoff<T> payoff) {
        outcomePayoffs.put(outcome,payoff);
    }
}
