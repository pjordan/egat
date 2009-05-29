package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public class ActionReducedSymmetricGame implements SymmetricGame {
    private SymmetricGame base;
    private Set<Action> actions;

    public ActionReducedSymmetricGame(SymmetricGame base, Set<Action> actions) {
        this.base = base;
        this.actions = actions;
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

    public SymmetricPayoff payoff(Outcome outcome) {
        return base.payoff(outcome);
    }

    public Payoff payoff(Profile profile) {
        return base.payoff(profile);
    }

    public Set<Action> getActions() {
        return actions;
    }

    public Set<Action> getActions(Player player) {
        return actions;
    }
}