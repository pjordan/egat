package edu.umich.eecs.ai.egat.dominance;

import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.Strategy;
import edu.umich.eecs.ai.egat.game.SymmetricGame;

/**
 * @author Patrick Jordan
 */
public interface SymmetricDominanceTester {
    boolean isDominated(Action action, SymmetricGame game);
    boolean isDominated(Strategy strategy, SymmetricGame game);
}
