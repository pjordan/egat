package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.SymmetricPayoff;
import edu.umich.eecs.ai.egat.game.SymmetricOutcome;

/**
 * @author Patrick Jordan
 */
public class SimpleSymmetricRegression implements SymmetricRegression {
    protected SymmetricGame game;


    public SimpleSymmetricRegression(SymmetricGame game) {
        this.game = game;
    }

    public SymmetricGame getSymmetricGame() {
        return game;
    }


    public SymmetricPayoff predict(SymmetricOutcome outome) {
        return game.payoff(outome);
    }
}
