/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

/**
 * The action interface for a simulation.  The contract for {@link Action}
 * requires that two actions are equal if {@link #getID()} returns
 * equal strings for both actions.
 *
 * @author Patrick Jordan
 */
public interface Action {
    /**
     * The ID for an action.
     *
     * @return the ID for an action.
     */
    String getID();
}
