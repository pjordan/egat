/*
 * EquivalentStrategyStrategicRegressionFactoryTest.java
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

import org.junit.Test;
import org.junit.Assert;
import edu.umich.eecs.ai.egat.game.*;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class EquivalentStrategyStrategicRegressionFactoryTest {

    @Test
    public void testRegression() {
        Player[] players = new Player[]{Games.createPlayer("Alice"),
                                        Games.createPlayer("Bob")};
        Action[][] actions = new Action[][] {{Games.createAction("a"),
                                              Games.createAction("a'")},
                                             {Games.createAction("b"),
                                              Games.createAction("b'")}};
        double[][] values = new double[][] {{0.0, 1.0},{2.0, 3.0},{4.0,5.0},{6.0,7.0}};


        Map<Player, Map<Action,Action>> actionConverter = new HashMap<Player,Map<Action,Action>>();

        Map<Action,Action> actionMap = new HashMap<Action,Action>();
        actionMap.put(Games.createAction("a"), Games.createAction("{a,a'}"));
        actionMap.put(Games.createAction("a'"), Games.createAction("{a,a'}"));
        actionConverter.put(players[0],actionMap);
        actionMap = new HashMap<Action,Action>();
        actionMap.put(Games.createAction("b"), Games.createAction("{b,b'}"));
        actionMap.put(Games.createAction("b'"), Games.createAction("{b,b'}"));
        actionConverter.put(players[1],actionMap);

        EquivalentStrategyStrategicRegressionFactory factory = new EquivalentStrategyStrategicRegressionFactory(actionConverter);

        StrategicMultiAgentSystem sim = createTestSimulation(players,actions);

        StrategicSimulationObserver observer = new AbstractStrategicSimulationObserver(sim);

        addObservations(observer, players, values);

        StrategicRegression regression = factory.regress(observer);

        Action[] reducedAction = new Action[] {Games.createAction("{a,a'}"), Games.createAction("{b,b'}")};
        Payoff p = regression.getStrategicGame().payoff(Games.createOutcome(players,reducedAction));
        Payoff p1 = PayoffFactory.createPayoff(players, new double[] {3.0,4.0});

        Assert.assertEquals(p,p1);
    }


    protected StrategicMultiAgentSystem createTestSimulation(Player[] players, Action[][] actions) {
        MutableStrategicMultiAgentSystem sim = new DefaultStrategicMultiAgentSystem("test","testRegression");

        for(int p = 0; p < players.length; p++) {
            sim.addPlayer(players[p]);

            Set<Action> set = new HashSet<Action>();
            for(int a = 0; a < actions[p].length; a++) {
                set.add(actions[p][a]);
            }
            sim.putActions(players[p],set);
        }

        return sim;
    }

    protected void addObservations(StrategicSimulationObserver observer, Player[] players, double[][] values) {
        int count = 0;
        for (Outcome outcome : Games.traversal(observer.getStrategicSimulation())) {
            Payoff payoff = PayoffFactory.createPayoff( players, values[count++] );
            observer.observe(outcome, payoff);
        }
    }
}
