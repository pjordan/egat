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
        SymmetricSimulation sim = new DefaultSymmetricSimulation("A","Test A");
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



        DefaultSymmetricSimulation sim = new DefaultSymmetricSimulation("test","testEmptyObservationCount");

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
