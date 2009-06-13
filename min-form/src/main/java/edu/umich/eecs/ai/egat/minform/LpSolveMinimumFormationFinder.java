/*
 * LpSolveMinimumFormationFinder.java
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
package edu.umich.eecs.ai.egat.minform;

import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Action;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class LpSolveMinimumFormationFinder implements MinimumFormationFinder {
    private LpSolveMinimumContainingFormationFinder mccFinder;

    public LpSolveMinimumFormationFinder() {
        mccFinder = new LpSolveMinimumContainingFormationFinder();
    }

    public SymmetricGame findMinimumFormation(SymmetricGame game) {
        SymmetricGame currentFormation = game;

        List<Action> remainingActions = new ArrayList<Action>(game.getActions());

        boolean flag = true;

        while (flag) {
            flag = false;

            for (Action a : currentFormation.getActions()) {
                if (remainingActions.contains(a)) {
                    remainingActions.remove(a);

                    SymmetricGame candidate = mccFinder.findMinContainingFormation(a, currentFormation);

                    if (candidate.getActions().size() < currentFormation.getActions().size()) {
                        currentFormation = candidate;

                        flag = true;

                        break;
                    }
                }
            }
        }

        return currentFormation;
    }

    public Set<SymmetricGame> findAllMinimumFormations(SymmetricGame game) {
        Map<Action, SymmetricGame> formations = new HashMap<Action, SymmetricGame>();

        Set<SymmetricGame> minFormations = new HashSet<SymmetricGame>();

        for (Action action : game.getActions()) {

            SymmetricGame formationGame = formations.get(action);

            if (formationGame == null) {
                formationGame = game;
            }

            if (formationGame.getActions().contains(action)) {
                SymmetricGame reducedGame = mccFinder.findMinContainingFormation(action, formationGame);

                if (reducedGame.getActions().size() < formationGame.getActions().size()) {

                    for (Action a : formationGame.getActions()) {
                        formations.put(a, reducedGame);
                    }

                    minFormations.remove(formationGame);
                    minFormations.add(reducedGame);
                }

            }
        }


        return minFormations;
    }
}
