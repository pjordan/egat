package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class DefaultStrategicSimulation extends DefaultGame implements StrategicSimulation {
    protected Map<Player, Set<Action>> playerActions;

    public DefaultStrategicSimulation() {
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public DefaultStrategicSimulation(String name) {
        super(name);
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public DefaultStrategicSimulation(String name, String description) {
        super(name, description);
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public DefaultStrategicSimulation(String name, String description, Set<Player> players) {
        super(name, description, players);
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public DefaultStrategicSimulation(Set<Player> players) {
        super(players);
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public void putActions(Player player, Set<Action> actions) {
        playerActions.put(player, actions);
    }

    public Set<Action> getActions(Player player) {
        return playerActions.get(player);
    }
}
