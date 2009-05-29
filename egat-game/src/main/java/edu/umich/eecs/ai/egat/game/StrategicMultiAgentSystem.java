package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public interface StrategicMultiAgentSystem extends MultiAgentSystem {
    /**
     * Get the action set of a player in the simulation.
     * @param player the player
     * @return the action set of a player in the simulation and <code>null</code>
     * otherwise.
     */
    Set<Action> getActions(Player player);
}
