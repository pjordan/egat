/*
 * DefaultSymmetricMultiAgentSystem.java
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
package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * @author Patrick Jordan
 */
public class DefaultSymmetricMultiAgentSystem extends DefaultMultiAgentSystem implements MutableSymmetricMultiAgentSystem {
    private Set<Action> actions;


    public DefaultSymmetricMultiAgentSystem() {
        actions = new HashSet<Action>();
    }

    public DefaultSymmetricMultiAgentSystem(String name) {
        super(name);
        actions = new HashSet<Action>();
    }

    public DefaultSymmetricMultiAgentSystem(String name, String description) {
        super(name, description);
        actions = new HashSet<Action>();
    }

    public DefaultSymmetricMultiAgentSystem(String name, String description, Set<Player> players) {
        super(name, description, players);
        actions = new HashSet<Action>();
    }

    public DefaultSymmetricMultiAgentSystem(Set<Player> players) {
        super(players);
        actions = new HashSet<Action>();
    }


    public Set<Action> getActions() {
        return actions;
    }

    public Set<Action> getActions(Player player) {
        return getActions();
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
}
