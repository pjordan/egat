package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.List;
import java.util.LinkedList;

/**
 * @author Patrick Jordan
 */
public class AbstractStrategicSimulationObserver implements StrategicSimulationObserver {
    protected StrategicMultiAgentSystem simulation;
    protected long observationCount;
    protected OutcomeMap<List<Payoff>, Outcome> observations;


    public AbstractStrategicSimulationObserver(StrategicMultiAgentSystem simulation) {
        if(simulation==null)
            throw new NullPointerException("Simulation cannot be null");
        this.simulation = simulation;
        observations =  new DefaultOutcomeMap<List<Payoff>>();
        observations.build(simulation);
    }


    public StrategicMultiAgentSystem getStrategicSimulation() {
        return simulation;
    }


    public long getObservationCount() {
        return observationCount;
    }


    public void observe(Outcome outcome, Payoff payoff) {
        List<Payoff> list = getObservations(outcome);        
        list.add(payoff);
        observationCount++;
    }

    public List<Payoff> getObservations(Outcome outcome) {
        List<Payoff> list = observations.get(outcome);

        if(list==null) {
            list = new LinkedList<Payoff>();
            observations.put(outcome,list);
        }

        return list;
    }
}
