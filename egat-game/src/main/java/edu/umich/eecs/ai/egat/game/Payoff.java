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
 * Payoff interface that provides a mapping between {@link Player simulation players}
 *  and their corresponding {@link Number value}.
 * {@link StrategicGame Strategic games} use this mapping to determine a
 * {@link Payoff payoff}.
 * <p/>
 * <p>The methods {@link #equals(Object)} and {@link #hashCode()} should reflect
 * the player-value map.  This is tested by comparing {@link #entrySet()}'s.
 * </p>
 *
 * @author Patrick Jordan
 */
public interface Payoff<T extends PayoffValue> {
    /**
     * Get the player payoff.
     *
     * @param player the player.
     * @return the player payoff.
     */
    T getPayoff(Player player);

    /**
     * Get the set of players which are mapped in the payoff.
     *
     * @return the set of players.
     */
    Set<Player> players();

    /**
     * Get the player-value entries.
     *
     * @return the player-value entries.
     */
    Set<Map.Entry<Player, T>> entrySet();

    /**
     * Get the collection of values received by the players.
     *
     * @return the collection of values.
     */
    Collection<T> values();
}
