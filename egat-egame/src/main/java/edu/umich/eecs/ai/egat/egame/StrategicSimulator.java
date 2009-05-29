package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.StrategicMultiAgentSystem;
import edu.umich.eecs.ai.egat.game.Payoff;
import edu.umich.eecs.ai.egat.game.Outcome;

/**
 * @author Patrick Jordan
 */
public interface StrategicSimulator extends StrategicMultiAgentSystem {
    Payoff simulate(Outcome outcome);
}
