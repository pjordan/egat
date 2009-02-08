package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public interface SymmetricSimulation extends StrategicSimulation {
    /**
     * Get the action set of all players in the simulation.
     * @return the action set of all players in the simulation.
     */
    Set<Action> getActions();
}
