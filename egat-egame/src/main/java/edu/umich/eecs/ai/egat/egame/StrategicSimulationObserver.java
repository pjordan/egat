package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.StrategicMultiAgentSystem;
import edu.umich.eecs.ai.egat.game.Outcome;
import edu.umich.eecs.ai.egat.game.Payoff;

import java.util.List;

/**
 * @author Patrick Jordan
 */
public interface StrategicSimulationObserver {
    StrategicMultiAgentSystem getStrategicSimulation();

    void observe(Outcome outcome, Payoff payoff);

    List<Payoff> getObservations(Outcome outcome);

    long getObservationCount();
}
