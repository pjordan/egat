/*
 * SymmetricIteratedDominatedStrategiesEliminatorImpl.java
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

import edu.umich.eecs.ai.egat.game.*;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class SymmetricIteratedDominatedStrategiesEliminatorImpl implements SymmetricIteratedDominatedStrategiesEliminator {
    private SymmetricDominanceTester strategicDominanceTester;

    public SymmetricIteratedDominatedStrategiesEliminatorImpl() {
        this(new MixedSymmetricDominanceTesterImpl());
    }

    public SymmetricIteratedDominatedStrategiesEliminatorImpl(SymmetricDominanceTester strategicDominanceTester) {
        this.strategicDominanceTester = strategicDominanceTester;
    }

    public SymmetricGame eliminateDominatedStrategies(SymmetricGame game) {
        boolean flag = true;

        SymmetricGame reduced = game;

        while (flag) {
            flag = false;

            Action[] actions = reduced.getActions().toArray(new Action[0]);
            for (Action action : actions) {
                if (strategicDominanceTester.isDominated(action, reduced)) {
                    flag = true;

                    Set<Action> candidates = new HashSet<Action>(reduced.getActions());
                    candidates.remove(action);

                    reduced = new ActionReducedSymmetricGame(reduced, candidates);
                }
            }
        }

        return reduced;
    }
}
