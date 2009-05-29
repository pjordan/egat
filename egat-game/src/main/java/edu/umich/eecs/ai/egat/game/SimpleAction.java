package edu.umich.eecs.ai.egat.game;

import java.io.Serializable;

/**
 * The simple action class is solely defined by the supplied action id.
 *
 * @author Patrick Jordan
 */
public class SimpleAction extends AbstractIdentifiable<Action> implements Action, Serializable {

    public SimpleAction(final String id) {
        super(id);
    }

    protected final boolean checkClass(final Object o) {
        return o instanceof Action;
    }
}
