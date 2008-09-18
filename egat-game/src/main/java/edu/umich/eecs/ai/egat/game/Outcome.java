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
 * Outcome interface that provides a mapping between
 * {@link Player game players} and their corresponding
 * {@link Action action}. {@link StrategicGame Strategic games}
 * use this mapping to determine
 * a {@link Payoff payoff}.
 * <p/>
 * <p>The methods {@link #equals(Object)} and {@link #hashCode()}
 * should reflect the player-action map.  This is
 * tested by comparing {@link #entrySet()}'s.
 * </p>
 *
 * @author Patrick Jordan
 */
public interface Outcome {
    /**
     * Get the player action.
     *
     * @param player the player.
     * @return the player action, <code>null</code> if there is no
     *         mapping for the player.
     */
    Action getAction(Player player);

    /**
     * Get the set of players which are mapped in the outcome.
     *
     * @return the set of players.
     */
    Set<Player> players();

    /**
     * Get the player-action entries.
     *
     * @return the player-action entries.
     */
    Set<Map.Entry<Player, Action>> entrySet();

    /**
     * Get the collection of actions played by the players.
     *
     * @return the collection of actions.
     */
    Collection<Action> actions();
}
