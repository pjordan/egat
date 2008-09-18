/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.gamexml;

import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.AbstractPlayer;

/**
 * @author Patrick Jordan
 */
final class PlayerImpl extends AbstractPlayer implements Comparable<Player> {
    String id;

    public PlayerImpl(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public String toString() {
        return "<player id=\"" + getID() + "\" />";
    }
}
