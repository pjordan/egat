/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * EGAT game interface.
 * @author Patrick Jordan
 */
public interface Game {
    /**
     * Get the set of players of the game.
     *
     * @return the set of players of the game.
     */
    Set<Player> players();

    /**
     * Get the name of the game.
     *
     * @return the name of the game.
     */
    String getName();

    /**
     * Get the description of the game.
     *
     * @return the description of the game.
     */
    String getDescription();
}
