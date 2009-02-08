package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class BasicStatsSymmetricSimulationObserverImpl implements BasicStatsSymmetricSimulationObserver {
    protected SymmetricSimulationObserver baseObserver;

    public BasicStatsSymmetricSimulationObserverImpl(SymmetricSimulation simulation) {
        this(new AbstractSymmetricSimulationObserver(simulation));
    }

    public BasicStatsSymmetricSimulationObserverImpl(SymmetricSimulationObserver baseObserver) {
        this.baseObserver = baseObserver;
    }

    public SymmetricPayoff meanObservationPayoff(SymmetricOutcome outcome) {
        List<SymmetricPayoff> payoffs = getObservations(outcome);

        return EGames.meanObservationPayoff(payoffs,outcome);        
    }

    public int observationCount(SymmetricOutcome outcome) {
        return getObservations(outcome).size();
    }


    public SymmetricSimulation getSymmetricSimulation() {
        return baseObserver.getSymmetricSimulation();
    }

    public void observe(SymmetricOutcome outcome, SymmetricPayoff payoff) {
        baseObserver.observe(outcome, payoff);
    }

    public List<SymmetricPayoff> getObservations(SymmetricOutcome outcome) {
        return baseObserver.getObservations(outcome);
    }

    public long getObservationCount() {
        return baseObserver.getObservationCount();
    }
}