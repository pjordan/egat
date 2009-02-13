package edu.umich.eecs.ai.egat.game;

import java.util.Collection;

/**
 * @author Patrick Jordan
 */
public interface MutableSymmetricSimulation extends MutableGame, SymmetricSimulation {
    void addAction(Action action);

    void removeAction(Action action);

    void addAllActions(Collection<? extends Action> actions);

    void clearActions();
}
