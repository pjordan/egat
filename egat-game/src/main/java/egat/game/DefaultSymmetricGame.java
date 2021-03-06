/*
 * DefaultSymmetricGame.java
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

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class DefaultSymmetricGame<T extends PayoffValue> extends AbstractStrategicGame<T> implements MutableSymmetricGame<T> {
    private Set<Action> actions;
    private Map<Object, Map<Action, T>> outcomePayoffs;
    private DefaultOutcomeMap<SymmetricPayoff<T>> outcomeMap;

    public DefaultSymmetricGame() {
        actions = new HashSet<Action>();
        outcomePayoffs = new HashMap<Object, Map<Action, T>>();
        outcomeMap = new DefaultOutcomeMap<SymmetricPayoff<T>>();
    }

    public DefaultSymmetricGame(String name) {
        super(name);
        actions = new HashSet<Action>();
        outcomePayoffs = new HashMap<Object, Map<Action, T>>();
        outcomeMap = new DefaultOutcomeMap<SymmetricPayoff<T>>();
    }


    public DefaultSymmetricGame(String name, String description) {
        super(name, description);
        actions = new HashSet<Action>();
        outcomePayoffs = new HashMap<Object, Map<Action, T>>();
        outcomeMap = new DefaultOutcomeMap<SymmetricPayoff<T>>();
    }

    public void build() {
        outcomeMap.build(this);
    }

    public Set<Action> getActions() {
        return actions;
    }

    public Set<Action> getActions(Player player) {
        return getActions();
    }

    public SymmetricPayoff<T> payoff(Outcome outcome) throws NonexistentPayoffException {
        SymmetricPayoff<T> payoff = outcomeMap.get(outcome);

        if (payoff == null) {
            SymmetricOutcome so = Games.createSymmetricOutcome(outcome);


            if (so == null)
                throw new NonexistentPayoffException(outcome);


            Map<Action, T> payoffs = outcomePayoffs.get(so.actionEntrySet());


            if (payoffs == null)
                throw new NonexistentPayoffException(outcome);


            payoff = PayoffFactory.createSymmetricPayoff(payoffs, outcome);

            outcomeMap.put(outcome, payoff);
        }

        return payoff;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

    public void addAllActions(Collection<? extends Action> actions) {
        this.actions.addAll(actions);
    }

    public void clearActions() {
        actions.clear();
    }

    public void putPayoff(Outcome outcome, Map<Action, T> values) {
        SymmetricOutcome so = Games.createSymmetricOutcome(outcome);

        outcomePayoffs.put(so.actionEntrySet(), values);
    }
}
