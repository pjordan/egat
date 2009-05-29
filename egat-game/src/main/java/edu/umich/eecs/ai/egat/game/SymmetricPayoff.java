package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.Map;

/**
 * Payoff interface that provides a mapping between {@link Player players}
 * and their corresponding {@link Number value}.
 * {@link StrategicGame Strategic games} uses this mapping to determine a
 * {@link SymmetricPayoff payoff}.
 * <p/>
 * <p>The methods {@link #equals(Object)} and {@link #hashCode()} should reflect
 * the player-value map.  This is tested by comparing {@link #entrySet()}'s.
 * </p>
 *
 * @param <T> the payoff value type.
 *
 * @author Patrick Jordan
 */
public interface SymmetricPayoff<T extends PayoffValue> extends Payoff<T> {
    /**
     * Get the action payoff.
     *
     * @param action the action.
     * @return the action payoff.
     */
    T getPayoff(Action action);

    /**
     * Get the set of players which are mapped in the payoff.
     *
     * @return the set of players.
     */
    Set<Action> actions();

    /**
     * Get the action-value map.
     * @return the action-value map.
     */
    Map<Action, T> valueMap();
}
