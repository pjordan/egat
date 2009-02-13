package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.Collections;

/**
 * @author Patrick Jordan
 */
public class PlayerReducedStrategicSimulation implements StrategicSimulation {
    private StrategicSimulation base;
    private Set<Player> players;

    public PlayerReducedStrategicSimulation(StrategicSimulation base, Set<Player> players) {
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
