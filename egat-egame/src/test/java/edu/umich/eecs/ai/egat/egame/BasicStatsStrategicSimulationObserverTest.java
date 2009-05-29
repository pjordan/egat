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
