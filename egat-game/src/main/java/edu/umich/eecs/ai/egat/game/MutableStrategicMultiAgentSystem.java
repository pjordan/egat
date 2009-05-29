package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * Mutable strategic multi-agent system is an extension of the {@link StrategicMultiAgentSystem strategic MAS}
 * interface that allows the {@link Action actions} of a given {@link Player player} to be modified.
 *
 * @author Patrick Jordan
 */
public interface MutableStrategicMultiAgentSystem extends MutableMultiAgentSystem, StrategicMultiAgentSystem {
    /**
     * Sets the action set for a player.
     * @param player the player whose actions are being set.
     * @param actions the set of actions the player is to have.
     */
    void putActions(Player player, Set<Action> actions);

    /**
     * Removes an action from the player's action set.
     * @param player the player whose actions are being set.
     * @param action the action to be removed.
     */
    void removeAction(Player player, Action action);

    /**
     * Adds an action from the player's action set.
     * @param player
     * @param action
     */
    void addAction(Player player, Action action);
}
