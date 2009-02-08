package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.List;
import java.util.LinkedList;

/**
 * @author Patrick Jordan
 */
public class AbstractSymmetricSimulationObserver implements SymmetricSimulationObserver {
    protected SymmetricSimulation simulation;
    protected long observationCount;
    protected OutcomeMap<List<SymmetricPayoff>, SymmetricOutcome> observations;

    public AbstractSymmetricSimulationObserver(SymmetricSimulation simulation) {
        if(simulation==null)
            throw new NullPointerException("Simulation cannot be null");
        
        this.simulation = simulation;
        observations =  new DefaultSymmetricOutcomeMap<List<SymmetricPayoff>>();
        observations.build(simulation);
    }


    public SymmetricSimulation getSymmetricSimulation() {
        return simulation;
    }


    public long getObservationCount() {
        return observationCount;
    }


    public void observe(SymmetricOutcome outcome, SymmetricPayoff payoff) {
        List<SymmetricPayoff> list = getObservations(outcome);
        list.add(payoff);
        observationCount++;
    }

    public List<SymmetricPayoff> getObservations(SymmetricOutcome outcome) {
        List<SymmetricPayoff> list = observations.get(outcome);

        if(list==null) {
            list = new LinkedList<SymmetricPayoff>();
            observations.put(outcome,list);
        }

        return list;
    }
}
