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
public interface MultiAgentSystem {
    /**
     * Gets the set of players of the multi-agent system.
     *
     * @return the set of players of the multi-agent system.
     */
    Set<Player> players();

    /**
     * Gets the name of the multi-agent system.
     *
     * @return the name of the multi-agent system.
     */
    String getName();

    /**
     * Gets the description of the multi-agent system.
     *
     * @return the description of the multi-agent system.
     */
    String getDescription();
}
