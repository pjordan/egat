package edu.umich.eecs.ai.egat.minform;

import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Action;

/**
 * @author Patrick Jordan
 */
public interface MinimumContainingFormationFinder {
    SymmetricGame findMinContainingFormation(Action seed, SymmetricGame game);
}
