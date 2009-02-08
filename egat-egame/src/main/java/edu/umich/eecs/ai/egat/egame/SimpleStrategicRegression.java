package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.StrategicGame;
import edu.umich.eecs.ai.egat.game.Payoff;
import edu.umich.eecs.ai.egat.game.Outcome;

/**
 * @author Patrick Jordan
 */
public class SimpleStrategicRegression implements StrategicRegression {
    protected StrategicGame game;


    public SimpleStrategicRegression(StrategicGame game) {
        this.game = game;
    }

    public StrategicGame getStrategicGame() {
        return game;
    }

    public Payoff predict(Outcome outome) {
        return game.payoff(outome);
    }
}
