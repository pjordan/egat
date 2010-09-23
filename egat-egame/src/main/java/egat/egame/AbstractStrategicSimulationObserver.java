/*
 * AbstractStrategicSimulationObserver.java
 *
 * Copyright (C) 2006-2009 Patrick R. Jordan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package egat.egame;

import egat.game.*;

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
