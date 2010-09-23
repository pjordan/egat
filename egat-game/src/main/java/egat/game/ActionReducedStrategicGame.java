/*
 * ActionReducedStrategicGame.java
 *
 * Copyright (C) 2006-2010 Patrick R. Jordan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package egat.game;

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
