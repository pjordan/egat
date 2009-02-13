package edu.umich.eecs.ai.egat.game;

/**
 * @author Patrick Jordan
 */
public interface MutableGame extends Game {
    boolean addPlayer(Player player);

    boolean removePlayer(Player player);
}
