package edu.umich.eecs.ai.egat.mincurb;

import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.SymmetricGame;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public interface SymmetricAllRationalizableFinder {
    Set<Action> findRationalizable(Set<Action> actions, SymmetricGame game);
}
