package edu.umich.eecs.ai.egat.graphicalgame;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Games;
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.DefaultStrategicMultiAgentSystem;
import edu.umich.eecs.ai.egat.graphicalgame.graphs.BasicSparseGraph;
import edu.umich.eecs.ai.egat.graphicalgame.graphs.MutableGraph;
import edu.umich.eecs.ai.egat.graphicalgame.utility.EstimatedUtilityNode;

/**
 * @author Patrick Jordan
 */
public class EstimatedUtilityFunctionBuilderTest {
    @Test
    public void testBuild() {
        EstimatedUtilityFunctionBuilder builder = new EstimatedUtilityFunctionBuilder();


        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");

        Action[] aliceActions = new Action[] {Games.createAction("a1"), Games.createAction("a2")};
        Action[] bobActions = new Action[] {Games.createAction("b1"), Games.createAction("b2")};

        assertNotNull(builder);

        DefaultStrategicMultiAgentSystem simulation = new DefaultStrategicMultiAgentSystem("","");
        simulation.addPlayer(alice);
        simulation.addPlayer(bob);

        for(Action aliceAction : aliceActions) {
            simulation.addAction(alice, aliceAction);
        }

        for(Action bobAction : bobActions) {
            simulation.addAction(bob, bobAction);
        }

        MutableGraph<Player> graph = new BasicSparseGraph<Player>();
        graph.addNode(alice);
        graph.addNode(bob);

        graph.addEdge(bob, bob);
        graph.addEdge(alice, alice);
        graph.addEdge(alice, bob);

        EstimatedUtilityNode aliceFunction = builder.build(alice, simulation, graph);
        EstimatedUtilityNode bobFunction = builder.build(bob, simulation, graph);

        assertNotNull(aliceFunction);
        assertNotNull(bobFunction);

        assertEquals(aliceFunction.decendantCount(),6,0);
        assertEquals(bobFunction.decendantCount(),2,0);
    }
}
