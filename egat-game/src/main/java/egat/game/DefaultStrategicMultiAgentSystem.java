/*
 * DefaultStrategicMultiAgentSystem.java
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
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class DefaultStrategicMultiAgentSystem extends DefaultMultiAgentSystem implements MutableStrategicMultiAgentSystem {
    protected Map<Player, Set<Action>> playerActions;

    public DefaultStrategicMultiAgentSystem() {
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public DefaultStrategicMultiAgentSystem(String name) {
        super(name);
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public DefaultStrategicMultiAgentSystem(String name, String description) {
        super(name, description);
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public DefaultStrategicMultiAgentSystem(String name, String description, Set<Player> players) {
        super(name, description, players);
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public DefaultStrategicMultiAgentSystem(Set<Player> players) {
        super(players);
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public void putActions(Player player, Set<Action> actions) {
        playerActions.put(player, actions);
    }

    public Set<Action> getActions(Player player) {
        return playerActions.get(player);
    }

    public void removeAction(Player player, Action action) {
        playerActions.get(player).remove(action);
    }

    public void addAction(Player player, Action action) {
        playerActions.get(player).add(action);
    }

    @Override
    protected void afterAddPlayer(Player player) {
        playerActions.put(player, new HashSet<Action>());
    }

    @Override
    protected void afterRemovePlayer(Player player) {
        playerActions.remove(player);
    }

    @Override
    protected DefaultStrategicMultiAgentSystem clone() throws CloneNotSupportedException {
        DefaultStrategicMultiAgentSystem mas = (DefaultStrategicMultiAgentSystem) super.clone();

        mas.playerActions = new HashMap<Player, Set<Action>>();

        for(Player player : playerActions.keySet()) {
            Set<Action> actions = playerActions.get(player);
            if(actions!=null) {
                mas.playerActions.put(player, new HashSet<Action>(actions));
            }
        }
        
        return mas;
    }
}
