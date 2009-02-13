package edu.umich.eecs.ai.egat.dominance;

import edu.umich.eecs.ai.egat.game.StrategicGame;
import edu.umich.eecs.ai.egat.game.MutableStrategicGame;

/**
 * @author Patrick Jordan
 */
public interface StrategicIteratedDominatedStrategiesEliminator {
    MutableStrategicGame eliminateDominatedStrategies(MutableStrategicGame game);
}
