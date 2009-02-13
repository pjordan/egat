package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.Collections;

/**
 * @author Patrick Jordan
 */
public class ActionReducedStrategicSimulation implements StrategicSimulation {
    private StrategicSimulation base;
    private Player player;
    private Set<Action> actions;

    public ActionReducedStrategicSimulation(StrategicSimulation base, Player player, Set<Action> actions) {
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
