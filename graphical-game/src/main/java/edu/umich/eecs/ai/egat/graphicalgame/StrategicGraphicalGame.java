package edu.umich.eecs.ai.egat.graphicalgame;

import edu.umich.eecs.ai.egat.game.StrategicGame;
import edu.umich.eecs.ai.egat.game.Outcome;
import edu.umich.eecs.ai.egat.game.Payoff;

/**
 * @author Patrick Jordan
 */
public interface StrategicGraphicalGame extends GraphicalMultiAgentSystem, StrategicGame {
    public void addSample(Outcome outcome, Payoff payoff);
}
