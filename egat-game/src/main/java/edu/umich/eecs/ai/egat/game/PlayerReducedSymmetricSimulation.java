package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public class PlayerReducedSymmetricSimulation implements SymmetricSimulation {
    private SymmetricSimulation base;
    private Set<Player> players;

    public PlayerReducedSymmetricSimulation(SymmetricSimulation base, Set<Player> players) {
        this.base = base;
        this.players = players;
    }

    public Set<Action> getActions(Player player) {
        return base.getActions(player);
    }

    public Set<Player> players() {
        return players;
    }

    public String getName() {
        return base.getName();
    }

    public String getDescription() {
        return base.getDescription();
    }

    public Set<Action> getActions() {
        return base.getActions();
    }
}
