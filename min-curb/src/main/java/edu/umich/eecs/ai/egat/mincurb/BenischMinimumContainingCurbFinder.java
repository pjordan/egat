package edu.umich.eecs.ai.egat.mincurb;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class BenischMinimumContainingCurbFinder implements MinimumContainingCurbFinder {
    public SymmetricGame findMinContainingCurb(Action seed, SymmetricGame game) {
        if(game.getActions().size()<2) {
            return game;
        }

        Set<Action> rationalizable = new HashSet<Action>();

        boolean converged = false;

        SymmetricAllRationalizableFinder finder = new BenischSymmetricAllRationalizableFinder();

        rationalizable.add(seed);
        
        while(!converged) {
            converged = true;

            Set<Action> newRationalizable = finder.findRationalizable(rationalizable, game);

            if(!newRationalizable.isEmpty()) {
                converged = false;

                rationalizable.addAll(newRationalizable);
            }
        }


        DefaultSymmetricGame reducedGame = new DefaultSymmetricGame(game.getName(), game.getDescription());

        for(Player player : game.players()) {
            reducedGame.addPlayer(player);
        }

        for(Action action : rationalizable) {
            reducedGame.addAction(action);
        }

        for(SymmetricOutcome outcome : Games.symmetricTraversal(reducedGame)) {
            reducedGame.putPayoff(outcome, game.payoff(outcome).valueMap());
        }
        
        return reducedGame;
    }
}
