package edu.umich.eecs.ai.egat.game;

import java.io.Serializable;

/**
 * The simple player class is solely defined by the supplied player id.
 *
 * @author Patrick Jordan
 */
public class SimplePlayer extends AbstractIdentifiable<Player> implements Player, Serializable {
    public SimplePlayer(final String id) {
        super(id);
    }

    protected final boolean checkClass(final Object o) {
        return o instanceof Player;
    }
}
