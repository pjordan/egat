/*
 * LpSolveMinimumContainingFormationFinder.java
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
package egat.minform;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class LpSolveMinimumContainingFormationFinder implements MinimumContainingFormationFinder {
    public SymmetricGame findMinContainingFormation(Action seed, SymmetricGame game) {
        if(game.getActions().size()<2) {
            return game;
        }

        Set<Action> rationalizable = new HashSet<Action>();

        boolean converged = false;

        SymmetricRationalizableFinder finder = new LpSolveSymmetricRationalizableFinder();

        rationalizable.add(seed);
        
        while(!converged) {
            converged = true;

            Set<Action> newRationalizable = finder.findRationalizable(rationalizable, game);

            if(!newRationalizable.isEmpty()) {
                converged = false;

                rationalizable.addAll(newRationalizable);
            }
        }      
        
        return new ActionReducedSymmetricGame(game,rationalizable);
    }
}
