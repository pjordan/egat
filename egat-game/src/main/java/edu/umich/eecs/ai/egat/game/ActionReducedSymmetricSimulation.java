package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public class ActionReducedSymmetricSimulation implements SymmetricSimulation {
    private SymmetricSimulation base;
    private Set<Action> actions;

    public ActionReducedSymmetricSimulation(SymmetricSimulation base, Set<Action> actions) {
        this.base = base;
        this.actions = actions;
    }

    public Set<Action> getActions(Player player) {
        return actions;
    }

    public Set<Player> players() {
        return base.players();
    }

    public String getName() {
        return base.getName();
    }

    public String getDescription() {
        return base.getDescription();
    }

    public Set<Action> getActions() {
        return actions;
    }
}
