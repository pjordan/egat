package edu.umich.eecs.ai.egat.dominance;

import edu.umich.eecs.ai.egat.game.MutableSymmetricGame;

/**
 * @author Patrick Jordan
 */
public interface SymmetricIteratedDominatedStrategiesEliminator {
    MutableSymmetricGame eliminateDominatedStrategies(MutableSymmetricGame game);
}
