package edu.umich.eecs.ai.egat.egame;

import org.junit.Test;
import org.junit.Assert;
import edu.umich.eecs.ai.egat.game.*;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class AbstractStrategicSimulationObserverTest {


    @Test(expected=NullPointerException.class)
    public void testNullSimulation() {
        new AbstractStrategicSimulationObserver(null);
    }

    @Test
    public void testEmptyObservationCount() {
        StrategicSimulation sim = new DefaultStrategicSimulation("A","Test A");
        StrategicSimulationObserver observer = new AbstractStrategicSimulationObserver(sim);
        Assert.assertEquals(observer.getObservationCount(),0L);
        Assert.assertSame(sim,observer.getStrategicSimulation());
    }

    @Test
    public void testObservations() {
        Player[] players = new Player[]{Games.createPlayer("Alice"),
                                        Games.createPlayer("Bob")};
        Action[][] actions = new Action[][] {{Games.createAction("A"),
                                              Games.createAction("a")},
                                             {Games.createAction("B"),
                                              Games.createAction("b")}};
        double[] values = new double[] {0.0, 1.0};

        DefaultStrategicSimulation sim = new DefaultStrategicSimulation("test","testEmptyObservationCount");

        for(int p = 0; p < players.length; p++) {
            sim.addPlayer(players[p]);

            Set<Action> set = new HashSet<Action>();
            for(int a = 0; a < actions[p].length; a++) {
                set.add(actions[p][a]);
            }
            sim.putActions(players[p],set);
        }

        StrategicSimulationObserver observer = new AbstractStrategicSimulationObserver(sim);
        Assert.assertEquals(observer.getObservationCount(),0L);
        Assert.assertSame(sim,observer.getStrategicSimulation());

        long count = 0;
        for(Outcome outcome : Games.traversal(sim)) {
            Assert.assertEquals(observer.getObservations(outcome).size(),0);

            Payoff payoff = PayoffFactory.createPayoff(players,values);
            observer.observe(outcome, payoff);

            Assert.assertEquals(observer.getObservations(outcome).size(),1);
            count++;
        }

        Assert.assertEquals(observer.getObservationCount(),count);
    }
}
