package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.Map;
import java.util.Collection;

/**
 *
 * @author Patrick Jordan
 */
public interface SymmetricOutcome extends Outcome {
    /**
     * Get the count of the action.
     *
     * @param action the action.
     * @return the action, <code>0</code> if there is no mapping for the action.
     */
    Integer getCount(Action action);

    /**
     * Get the player-action entries.
     *
     * @return the player-action entries.
     */
    Set<Map.Entry<Action, Integer>> actionEntrySet();
        
    boolean symmetricEquals(SymmetricOutcome outcome);
}
