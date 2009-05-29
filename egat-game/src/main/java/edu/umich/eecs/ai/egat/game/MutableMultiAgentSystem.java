package edu.umich.eecs.ai.egat.game;

/**
 * {@link MutableMultiAgentSystem} is the multi-agent system interface that allows for the
 * addition and removal of {@link Player players}.
 *
 * @author Patrick Jordan
 */
public interface MutableMultiAgentSystem extends MultiAgentSystem {
    /**
     * Adds the player.
     * @param player the player to add.
     * @return <code>true</code> if the player was added to the system, <code>false</code> otherwise.
     */
    boolean addPlayer(Player player);

    /**
     * Removes the player.
     * @param player the player to remove.
     * @return <code>true</code> if the player was removed from the system, <code>false</code> otherwise.
     */
    boolean removePlayer(Player player);
}
