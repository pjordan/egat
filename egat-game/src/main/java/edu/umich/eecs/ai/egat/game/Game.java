/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * EGAT simulation interface.
 * @author Patrick Jordan
 */
public interface Game {
    /**
     * Get the set of players of the simulation.
     *
     * @return the set of players of the simulation.
     */
    Set<Player> players();

    /**
     * Get the name of the simulation.
     *
     * @return the name of the simulation.
     */
    String getName();

    /**
     * Get the description of the simulation.
     *
     * @return the description of the simulation.
     */
    String getDescription();
}
