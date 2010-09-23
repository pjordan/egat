/*
 * MutableStrategicMultiAgentSystem.java
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

/**
 * Mutable strategic multi-agent system is an extension of the {@link StrategicMultiAgentSystem strategic MAS}
 * interface that allows the {@link Action actions} of a given {@link Player player} to be modified.
 *
 * @author Patrick Jordan
 */
public interface MutableStrategicMultiAgentSystem extends MutableMultiAgentSystem, StrategicMultiAgentSystem {
    /**
     * Sets the action set for a player.
     * @param player the player whose actions are being set.
     * @param actions the set of actions the player is to have.
     */
    void putActions(Player player, Set<Action> actions);

    /**
     * Removes an action from the player's action set.
     * @param player the player whose actions are being set.
     * @param action the action to be removed.
     */
    void removeAction(Player player, Action action);

    /**
     * Adds an action from the player's action set.
     * @param player
     * @param action
     */
    void addAction(Player player, Action action);
}
