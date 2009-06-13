/*
 * BasicStatsStrategicSimulationObserverTest.java
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

/**
 * @author Patrick Jordan
 */
public class BasicStatsStrategicSimulationObserverTest {
    @Test
    public void testObservations() {
        Player[] players = new Player[]{Games.createPlayer("Alice"),
                                        Games.createPlayer("Bob")};
        Action[][] actions = new Action[][] {{Games.createAction("A"),
                                              Games.createAction("a")},
                                             {Games.createAction("B"),
                                              Games.createAction("b")}};
        double[] values = new double[] {0.0, 1.0};

        MutableStrategicMultiAgentSystem sim = new DefaultStrategicMultiAgentSystem("test","testEmptyObservationCount");

        for(int p = 0; p < players.length; p++) {
            sim.addPlayer(players[p]);

            Set<Action> set = new HashSet<Action>();
            for(int a = 0; a < actions[p].length; a++) {
                set.add(actions[p][a]);
            }
            sim.putActions(players[p],set);
        }

        BasicStatsStrategicSimulationObserver observer = new BasicStatsStrategicSimulationObserverImpl(sim);
        Assert.assertEquals(observer.getObservationCount(),0L);
        Assert.assertSame(sim,observer.getStrategicSimulation());

        long count = 0;
        for(Outcome outcome : Games.traversal(sim)) {
            Assert.assertEquals(observer.getObservations(outcome).size(),0);


            Payoff zeroPayoff = PayoffFactory.createPayoff(players,new double[] {0.0,0.0});

            Assert.assertEquals(observer.meanObservationPayoff(outcome),zeroPayoff);

            Payoff payoff = PayoffFactory.createPayoff(players,values);
            observer.observe(outcome, payoff);
            count++;
            payoff = PayoffFactory.createPayoff(players,values);
            observer.observe(outcome, payoff);
            count++;

            Assert.assertEquals(observer.observationCount(outcome),2);

            Payoff meanPayoff = PayoffFactory.createPayoff(players,values);

            Assert.assertEquals(observer.meanObservationPayoff(outcome), meanPayoff);
        }

        Assert.assertEquals(observer.getObservationCount(),count);
    }
}
