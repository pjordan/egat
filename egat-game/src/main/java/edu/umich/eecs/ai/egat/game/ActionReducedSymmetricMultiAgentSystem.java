/*
 * ActionReducedSymmetricMultiAgentSystem.java
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

/**
 * @author Patrick Jordan
 */
public class ActionReducedSymmetricMultiAgentSystem implements SymmetricMultiAgentSystem {
    private SymmetricMultiAgentSystem base;
    private Set<Action> actions;

    public ActionReducedSymmetricMultiAgentSystem(SymmetricMultiAgentSystem base, Set<Action> actions) {
        this.base = base;
        this.actions = actions;
    }

    public Set<Action> getActions(Player player) {
        return actions;
    }

    public Set<Player> players() {
        return base.players();
    }

    public String getName() {
        return base.getName();
    }

    public String getDescription() {
        return base.getDescription();
    }

    public Set<Action> getActions() {
        return actions;
    }
}
