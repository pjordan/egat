/*
 * MutableMultiAgentSystem.java
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

/**
 * {@link MutableMultiAgentSystem} is the multi-agent system interface that allows for the
 * addition and removal of {@link Player players}.
 *
 * @author Patrick Jordan
 */
public interface MutableMultiAgentSystem extends MultiAgentSystem {
    /**
     * Adds the player.
     * @param player the player to add.
     * @return <code>true</code> if the player was added to the system, <code>false</code> otherwise.
     */
    boolean addPlayer(Player player);

    /**
     * Removes the player.
     * @param player the player to remove.
     * @return <code>true</code> if the player was removed from the system, <code>false</code> otherwise.
     */
    boolean removePlayer(Player player);
}
