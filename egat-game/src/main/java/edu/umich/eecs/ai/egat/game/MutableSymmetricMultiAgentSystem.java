package edu.umich.eecs.ai.egat.game;

import java.util.Collection;

/**
 * @author Patrick Jordan
 */
public interface MutableSymmetricMultiAgentSystem extends MutableMultiAgentSystem, SymmetricMultiAgentSystem {
    void addAction(Action action);

    void removeAction(Action action);

    void addAllActions(Collection<? extends Action> actions);

    void clearActions();
}
