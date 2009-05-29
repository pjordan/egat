package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public interface SymmetricMultiAgentSystem extends StrategicMultiAgentSystem {
    /**
     * Get the action set of all players in the simulation.
     * @return the action set of all players in the simulation.
     */
    Set<Action> getActions();
}
