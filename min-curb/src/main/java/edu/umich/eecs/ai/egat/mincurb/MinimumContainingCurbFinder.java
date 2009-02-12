package edu.umich.eecs.ai.egat.mincurb;

import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Action;

/**
 * @author Patrick Jordan
 */
public interface MinimumContainingCurbFinder {
    SymmetricGame findMinContainingCurb(Action seed, SymmetricGame game); 
}
