package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.SymmetricPayoff;
import edu.umich.eecs.ai.egat.game.SymmetricOutcome;

/**
 * @author Patrick Jordan
 */
public interface SymmetricRegression {
    public SymmetricGame getSymmetricGame();
    public SymmetricPayoff predict(SymmetricOutcome outome);
}
