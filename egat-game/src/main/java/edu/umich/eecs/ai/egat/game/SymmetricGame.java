/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

/**
 * SymmetricGame is a marker interface for a symmetric simulation.
 *
 * @author Patrick Jordan
 */
public interface SymmetricGame<T extends PayoffValue> extends StrategicGame<T>, SymmetricSimulation {

    /**
     * Get the payoff of the outcome.
     * @param outcome the outcome.
     * @return the payoff of the outcome, <code>null</code> if the outcome is
     * invalid.
     */
    SymmetricPayoff<T> payoff(Outcome outcome);
}
