package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public class ActionReducedStrategicMultiAgentSystem implements StrategicMultiAgentSystem {
    private StrategicMultiAgentSystem base;
    private Player player;
    private Set<Action> actions;

    public ActionReducedStrategicMultiAgentSystem(StrategicMultiAgentSystem base, Player player, Set<Action> actions) {
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
}
