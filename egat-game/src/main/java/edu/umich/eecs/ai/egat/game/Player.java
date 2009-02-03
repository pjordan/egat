/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

/**
 * The player interface for a simulation.  The contract for {@link Player}
 * requires that two players areequal if {@link #getID()} returns equal
 * strings for both players.
 *
 * @author Patrick Jordan
 */
public interface Player {
    /**
     * Get the player identifier.
     *
     * @return the player identifier.
     */
    String getID();
}
