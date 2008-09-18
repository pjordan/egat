/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * {@link Game} which is partially defined by a payoff function which maps
 *  {@link Profile profiles} to {@link Payoff payoffs}.
 *
 * @author Patrick Jordan
 */
public interface StrategicGame<T extends PayoffValue> extends Game {
    /**
     * Get the action set of a player in the game.
     * @param player the player
     * @return the action set of a player in the game and <code>null</code>
     * otherwise.
     */
    Set<Action> getActions(Player player);

    /**
     * Get the payoff of the outcome.
     * @param outcome the outcome.
     * @return the payoff of the outcome, <code>null</code> if the outcome is
     * invalid.
     */
    Payoff<T> payoff(Outcome outcome);

    /**
     * Get the payoff of the profile.
     * @param profile the profile.
     * @return the payoff of the profile, <code>null</code> if the profile is
     * invalid.
     */
    Payoff<T> payoff(Profile profile);
    
}
