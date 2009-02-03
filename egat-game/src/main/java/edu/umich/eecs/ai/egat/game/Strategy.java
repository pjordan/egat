/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Player strategy in a {@link StrategicGame strategic simulation}.
 * <p/>
 * <p>The methods {@link #equals(Object)} and {@link #hashCode()}
 * should reflect the action-probability map.
 * This is tested by comparing {@link #entrySet()}'s.
 * </p>
 *
 * @author Patrick Jordan
 */
public interface Strategy {
    /**
     * Get the probability of the action being taken.
     *
     * @param action the action.
     * @return the probability of the action being taken.
     */
    Number getProbability(Action action);

    /**
     * Get the set of actions which are mapped in the strategy.
     *
     * @return the set of actions.
     */
    Set<Action> actions();

    /**
     * Get the action-probability entries.
     *
     * @return the action-probability entries.
     */
    Set<Map.Entry<Action, Number>> entrySet();

    /**
     * Get the collection of probabilities used in the strategy.
     *
     * @return the collection of probabilities.
     */
    Collection<Number> probabilities();
}
