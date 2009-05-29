package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.List;

/**
 * @author Patrick Jordan
 */
public interface SymmetricSimulationObserver {
    SymmetricMultiAgentSystem getSymmetricSimulation();

    void observe(SymmetricOutcome outcome, SymmetricPayoff payoff);

    List<SymmetricPayoff> getObservations(SymmetricOutcome outcome);

    long getObservationCount();
}
