/*
 * AbstractSymmetricSimulationObserver.java
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
public class AbstractSymmetricSimulationObserver implements SymmetricSimulationObserver {
    protected SymmetricMultiAgentSystem simulation;
    protected long observationCount;
    protected OutcomeMap<List<SymmetricPayoff>, SymmetricOutcome> observations;

    public AbstractSymmetricSimulationObserver(SymmetricMultiAgentSystem simulation) {
        if(simulation==null)
            throw new NullPointerException("Simulation cannot be null");
        
        this.simulation = simulation;
        observations =  new DefaultSymmetricOutcomeMap<List<SymmetricPayoff>>();
        observations.build(simulation);
    }


    public SymmetricMultiAgentSystem getSymmetricSimulation() {
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
