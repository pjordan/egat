/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

/**
 *
 * @author Yevgeniy Vorobeychik
 * @author Patrick Jordan
 */
public interface PayoffValue extends Comparable<PayoffValue> {
    /**
     * Get the value of the payoff for a (player,action) pair.
     *
     * @return the value of the payoff for a (player,action) pair
     */
    public double getValue();
}
