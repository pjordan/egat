package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public class ActionReducedStrategicGame implements StrategicGame {
    private StrategicGame base;
    private Player player;
    private Set<Action> actions;

    public ActionReducedStrategicGame(StrategicGame base, Player player, Set<Action> actions) {
        this.base = base;
        this.player = player;
        this.actions = actions;
    }

    public Set<Action> getActions(Player player) {
        if(this.player.equals(player)) {
            return actions;
        } else {
            return base.getActions(player);
        }
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

    public Payoff payoff(Outcome outcome) {
        return base.payoff(outcome);
    }

    public Payoff payoff(Profile profile) {
        return base.payoff(profile);
    }
}
