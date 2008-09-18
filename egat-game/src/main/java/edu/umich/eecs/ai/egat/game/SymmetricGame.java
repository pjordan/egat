/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * SymmetricGame is a marker interface for a symmetric game.
 *
 * @author Patrick Jordan
 */
public interface SymmetricGame<T extends PayoffValue> extends StrategicGame<T> {
    /**
     * Get the action set of all players in the game.
     * @return the action set of all players in the game.
     */
    Set<Action> getActions();

    /**
     * Get the payoff of the outcome.
     * @param outcome the outcome.
     * @return the payoff of the outcome, <code>null</code> if the outcome is
     * invalid.
     */
    SymmetricPayoff<T> payoff(Outcome outcome);
}
