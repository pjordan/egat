/*
 * StrategicIteratedDominatedStrategiesEliminatorImpl.java
 *
 * Copyright (C) 2006-2009 Patrick R. Jordan
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
package edu.umich.eecs.ai.egat.dominance;

import edu.umich.eecs.ai.egat.game.MutableStrategicGame;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Action;

/**
 * @author Patrick Jordan
 */
public class StrategicIteratedDominatedStrategiesEliminatorImpl implements StrategicIteratedDominatedStrategiesEliminator {
    private StrategicDominanceTester strategicDominanceTester;

    public StrategicIteratedDominatedStrategiesEliminatorImpl() {
        this(new MixedStrategicDominanceTesterImpl());
    }

    public StrategicIteratedDominatedStrategiesEliminatorImpl(StrategicDominanceTester strategicDominanceTester) {
        this.strategicDominanceTester = strategicDominanceTester;
    }

    public MutableStrategicGame eliminateDominatedStrategies(MutableStrategicGame game) {
        boolean flag = true;

        while(flag) {
            flag = false;

            for(Player player : game.players()) {
                Action[] actions = game.getActions(player).toArray(new Action[0]);
                for(Action action : actions) {
                    if(strategicDominanceTester.isDominated(player,action,game)) {
                        flag = true;

                        game.removeAction(player, action);
                    }
                }
            }
        }

        return game;
    }
}
