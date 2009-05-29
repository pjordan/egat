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
 * @param <T> the payoff value type.
 *
 * @author Patrick Jordan
 */
public interface SymmetricGame<T extends PayoffValue> extends StrategicGame<T>, SymmetricMultiAgentSystem {

    /**
     * Get the payoff of the outcome.
     * @param outcome the outcome.
     * @return the payoff of the outcome, <code>null</code> if the outcome is
     * invalid.
     */
    SymmetricPayoff<T> payoff(Outcome outcome);
}
