package edu.umich.eecs.ai.egat.egame;

import org.junit.Test;
import org.junit.Assert;
import edu.umich.eecs.ai.egat.game.*;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class BasicStatsSymmetricSimulationObserverTest {
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

        BasicStatsSymmetricSimulationObserver observer = new BasicStatsSymmetricSimulationObserverImpl(sim);
        Assert.assertEquals(observer.getObservationCount(),0L);
        Assert.assertSame(sim,observer.getSymmetricSimulation());

        long count = 0;
        for(SymmetricOutcome outcome : Games.symmetricTraversal(sim)) {
            Assert.assertEquals(observer.getObservations(outcome).size(),0);

            Map<Action,PayoffValue> zeros = new HashMap<Action,PayoffValue>();

            for(Action a : actions) {
                if(outcome.getCount(a)>0) {
                    zeros.put(a,PayoffFactory.createPayoffValue(0.0));
                }
            }

            SymmetricPayoff zeroPayoff = PayoffFactory.createSymmetricPayoff(zeros, outcome);

            Assert.assertEquals(observer.meanObservationPayoff(outcome),zeroPayoff);

            Map<Action,PayoffValue> values = new HashMap<Action,PayoffValue>();

            for(Action a : actions) {
                if(outcome.getCount(a)>0) {
                    values.put(a,PayoffFactory.createPayoffValue(1.0));
                }
            }


            SymmetricPayoff payoff = PayoffFactory.createSymmetricPayoff(values, outcome);
            observer.observe(outcome, payoff);
            count++;
            payoff = PayoffFactory.createSymmetricPayoff(values, outcome);
            observer.observe(outcome, payoff);
            count++;

            Assert.assertEquals(observer.observationCount(outcome),2);

            Assert.assertEquals(observer.meanObservationPayoff(outcome),payoff);
        }

        Assert.assertEquals(observer.getObservationCount(),count);
    }
}
