/*
 * BasicStatsStrategicSimulationObserverImpl.java
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
