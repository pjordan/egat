package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.Map;
import java.util.Collection;

/**
 * Payoff interface that provides a mapping between {@link edu.umich.eecs.ai.egat.game.Player game players}
 *  and their corresponding {@link Number value}.
 * {@link edu.umich.eecs.ai.egat.game.StrategicGame Strategic games} use this mapping to determine a
 * {@link SymmetricPayoff payoff}.
 * <p/>
 * <p>The methods {@link #equals(Object)} and {@link #hashCode()} should reflect
 * the player-value map.  This is tested by comparing {@link #entrySet()}'s.
 * </p>
 *
 * @author Patrick Jordan
 */
public interface SymmetricPayoff<T extends PayoffValue> extends Payoff<T>{
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
}
