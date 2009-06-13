/*
 * EquivalentStrategyStrategicRegressionFactory.java
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
package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class EquivalentStrategyStrategicRegressionFactory implements StrategicRegressionFactory {
    protected Map<Player,Map<Action,Action>> actionConverter;


    public EquivalentStrategyStrategicRegressionFactory(Map<Player,Map<Action,Action>> actionConverter) {
        this.actionConverter = actionConverter;
    }


    public StrategicRegression regress(StrategicSimulationObserver observer) {
        StrategicMultiAgentSystem simulation = observer.getStrategicSimulation();
        MutableStrategicMultiAgentSystem reducedSimulation = new DefaultStrategicMultiAgentSystem(simulation.getName(), simulation.getDescription());
        
        for(Player p : simulation.players()) {
            reducedSimulation.addPlayer(p);

            Set<Action> actions = new HashSet<Action>();
            for(Action a : simulation.getActions(p)) {
                actions.add(actionConverter.get(p).get(a));
            }

            reducedSimulation.putActions(p,actions);
        }

        BasicStatsStrategicSimulationObserver reducedObserver = new BasicStatsStrategicSimulationObserverImpl(reducedSimulation);


        for(Outcome outcome : Games.traversal(simulation)) {
            Outcome convertedOutcome = convertOutcome(outcome);
            for(Payoff p : observer.getObservations(outcome)) {
                reducedObserver.observe(convertedOutcome, p);
            }
        }


        return new EquivalentStrategyStrategicRegression(new SimpleStrategicRegressionFactory().regress(reducedObserver).getStrategicGame(), actionConverter);
    }

    private Outcome convertOutcome(Outcome outcome) {
        Player[] players = outcome.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

        for(int i = 0; i < players.length; i++) {
            actions[i] = actionConverter.get(players[i]).get(outcome.getAction(players[i]));
        }

        return Games.createOutcome(players,actions);
    }


}
