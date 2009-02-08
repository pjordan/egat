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
public interface StrategicGame<T extends PayoffValue> extends StrategicSimulation {
    
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
