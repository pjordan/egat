package edu.umich.eecs.ai.egat.minform;

import edu.umich.eecs.ai.egat.game.*;

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
