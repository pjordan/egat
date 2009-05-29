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
