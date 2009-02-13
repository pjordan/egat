package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public interface MutableStrategicSimulation extends MutableGame, StrategicSimulation {
    void putActions(Player player, Set<Action> actions);

    void removeAction(Player player, Action action);

    void addAction(Player player, Action action);
}
