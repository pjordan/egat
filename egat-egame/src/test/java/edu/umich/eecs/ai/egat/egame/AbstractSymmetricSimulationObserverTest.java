/*
 * AbstractSymmetricSimulationObserverTest.java
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

import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class AbstractSymmetricSimulationObserverTest {
    @Test(expected=NullPointerException.class)
    public void testNullSimulation() {
        new AbstractSymmetricSimulationObserver(null);
    }

    @Test
    public void testEmptyObservationCount() {
        SymmetricMultiAgentSystem sim = new DefaultSymmetricMultiAgentSystem("A","Test A");
        SymmetricSimulationObserver observer = new AbstractSymmetricSimulationObserver(sim);
        Assert.assertEquals(observer.getObservationCount(),0L);
        Assert.assertSame(sim,observer.getSymmetricSimulation());
    }

    @Test
    public void testObservations() {
        Player[] players = new Player[]{Games.createPlayer("Alice"),
                                        Games.createPlayer("Bob")};
        Action[] actions = new Action[] {Games.createAction("a"),
                                         Games.createAction("b")};



        MutableSymmetricMultiAgentSystem sim = new DefaultSymmetricMultiAgentSystem("test","testEmptyObservationCount");

        for(int p = 0; p < players.length; p++) {
            sim.addPlayer(players[p]);
        }

        for(int a = 0; a < actions.length; a++) {
            sim.addAction(actions[a]);
        }

        SymmetricSimulationObserver observer = new AbstractSymmetricSimulationObserver(sim);
        Assert.assertEquals(observer.getObservationCount(),0L);
        Assert.assertSame(sim,observer.getSymmetricSimulation());

        long count = 0;
        for(SymmetricOutcome outcome : Games.symmetricTraversal(sim)) {
            Assert.assertEquals(observer.getObservations(outcome).size(),0);

            Map<Action,PayoffValue> values = new HashMap<Action,PayoffValue>();

            for(Action a : actions) {
                if(outcome.getCount(a)>0) {
                    values.put(a,PayoffFactory.createPayoffValue(1.0));
                }
            }

            SymmetricPayoff payoff = PayoffFactory.createSymmetricPayoff(values, outcome);
            observer.observe(outcome, payoff);

            Assert.assertEquals(observer.getObservations(outcome).size(),1);
            count++;
        }

        Assert.assertEquals(observer.getObservationCount(),count);
    }
}
