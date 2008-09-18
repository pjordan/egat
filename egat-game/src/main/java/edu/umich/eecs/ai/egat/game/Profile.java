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
 * Profile interface that provides a mapping between
 * {@link Player game players} and their corresponding
 * {@link Strategy strategies}. {@link StrategicGame Strategic games}
 * use this mapping to determine
 * an expected {@link Payoff payoff}.
 * <p/>
 * <p>The methods {@link #equals(Object)} and {@link #hashCode()} should
 * reflect the player-strategy map.  This is
 * tested by comparing {@link #entrySet()}'s.
 * </p>
 *
 * @author Patrick Jordan
 */
public interface Profile {
    /**
     * Get the player strategy.
     *
     * @param player the player.
     * @return the player strategy, <code>null</code> if there is no mapping
     *  for the player.
     */
    Strategy getStrategy(Player player);

    /**
     * Get the set of players which are mapped in the profile.
     *
     * @return the set of players.
     */
    Set<Player> players();

    /**
     * Get the player-strategy entries.
     *
     * @return the player-strategy entries.
     */
    Set<Map.Entry<Player, Strategy>> entrySet();

    /**
     * Get the collection of strategies used by the players.
     *
     * @return the collection of strategies.
     */
    Collection<Strategy> strategies();
}
