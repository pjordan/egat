/*
 * PlayerReducedStrategicMultiAgentSystem.java
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
import java.util.Collections;

/**
 * @author Patrick Jordan
 */
public class PlayerReducedStrategicMultiAgentSystem implements StrategicMultiAgentSystem {
    private StrategicMultiAgentSystem base;
    private Set<Player> players;

    public PlayerReducedStrategicMultiAgentSystem(StrategicMultiAgentSystem base, Set<Player> players) {
        this.base = base;
        this.players = players;
    }

    public Set<Action> getActions(Player player) {
        if(players.contains(player)) {
            return base.getActions(player);
        } else {
            return Collections.emptySet();
        }
    }

    public Set<Player> players() {
        return players;
    }

    public String getName() {
        return base.getName();
    }

    public String getDescription() {
        return base.getDescription();
    }
}
