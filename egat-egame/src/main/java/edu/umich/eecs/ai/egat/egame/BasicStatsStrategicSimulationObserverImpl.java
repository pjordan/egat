package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.List;

/**
 * @author Patrick Jordan
 */
public class BasicStatsStrategicSimulationObserverImpl implements BasicStatsStrategicSimulationObserver {
    protected StrategicSimulationObserver baseObserver;

    public BasicStatsStrategicSimulationObserverImpl(StrategicMultiAgentSystem simulation) {
        this(new AbstractStrategicSimulationObserver(simulation));
    }


    public BasicStatsStrategicSimulationObserverImpl(StrategicSimulationObserver baseObserver) {
        this.baseObserver = baseObserver;
    }

    public Payoff meanObservationPayoff(Outcome outcome) {
        List<Payoff> list = getObservations(outcome);
        Player[] players = outcome.players().toArray(new Player[0]);
        return EGames.meanPayoff(list,players);
    }

    public int observationCount(Outcome outcome) {
        return getObservations(outcome).size();
    }


    public StrategicMultiAgentSystem getStrategicSimulation() {
        return baseObserver.getStrategicSimulation();
    }

    public void observe(Outcome outcome, Payoff payoff) {
        baseObserver.observe(outcome, payoff);
    }

    public List<Payoff> getObservations(Outcome outcome) {
        return baseObserver.getObservations(outcome);
    }

    public long getObservationCount() {
        return baseObserver.getObservationCount();
    }
}
