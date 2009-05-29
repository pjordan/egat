package edu.umich.eecs.ai.egat.graphicalgame.graphs;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Games;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class BasicSparseGraphTest {

    @Test
    public void testNullConstructor() {
        assertNotNull(new BasicSparseGraph());
    }

    @Test
    public void testNodesConstructor() {
        List<Player> players = Arrays.asList(Games.createPlayer("alice"));

        BasicSparseGraph<Player> sparseGraph = new BasicSparseGraph<Player>(players);

        assertNotNull(sparseGraph);

        assertEquals(sparseGraph.nodes(), new LinkedHashSet<Player>(players));
    }

    @Test
    public void testNodes() {
        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");
        Player eve = Games.createPlayer("eve");

        List<Player> players = Arrays.asList(alice, bob);

        BasicSparseGraph<Player> sparseGraph = new BasicSparseGraph<Player>();

        assertNotNull(sparseGraph);

        assertTrue(sparseGraph.nodes().isEmpty());

        assertTrue(sparseGraph.addNode(alice));
        assertEquals(sparseGraph.nodes().size(),1,0);
        assertFalse(sparseGraph.addNode(alice));

        assertTrue(sparseGraph.addNode(bob));
        assertEquals(sparseGraph.nodes().size(),2,0);
        assertEquals(sparseGraph.nodes(), new LinkedHashSet<Player>(players));

        assertFalse(sparseGraph.removeNode(eve));
        assertEquals(sparseGraph.nodes().size(),2,0);
        assertTrue(sparseGraph.removeNode(alice));
        assertEquals(sparseGraph.nodes().size(),1,0);
        assertFalse(sparseGraph.removeNode(alice));
        assertEquals(sparseGraph.nodes().size(),1,0);
        assertTrue(sparseGraph.removeNode(bob));
        assertTrue(sparseGraph.nodes().isEmpty());
    }

    @Test
    public void testEquals() throws CloneNotSupportedException {
        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");
        
        List<Player> players = Arrays.asList(alice, bob);

        BasicSparseGraph<Player> sparseGraph = new BasicSparseGraph<Player>();
        BasicSparseGraph<Player> populatedSparseGraph =new BasicSparseGraph<Player>(players);

        assertEquals(sparseGraph,sparseGraph);
        assertFalse(sparseGraph.equals("a"));        
        assertEquals(sparseGraph, new BasicSparseGraph<Player>());
        assertEquals(populatedSparseGraph, new BasicSparseGraph<Player>(players));
        assertFalse(populatedSparseGraph.equals(sparseGraph));

        assertEquals(sparseGraph,sparseGraph.clone());
        assertEquals(populatedSparseGraph,populatedSparseGraph.clone());
        assertEquals(new BasicSparseGraph<Player>(populatedSparseGraph),populatedSparseGraph);
        assertEquals(new BasicSparseGraph<Player>(sparseGraph),sparseGraph);
    }

    @Test
    public void testEdges() {
        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");
        Player eve = Games.createPlayer("eve");

        List<Player> players = Arrays.asList(alice, bob);

        BasicSparseGraph<Player> sparseGraph =new BasicSparseGraph<Player>(players);

        assertFalse(sparseGraph.hasEdge(alice,bob));
        assertTrue(sparseGraph.addEdge(alice,bob));
        assertTrue(sparseGraph.hasEdge(alice,bob));
        assertFalse(sparseGraph.hasEdge(bob,alice));

        assertEquals(sparseGraph, new BasicSparseGraph<Player>(sparseGraph));

        assertTrue(sparseGraph.removeEdge(alice,bob));
        assertFalse(sparseGraph.hasEdge(alice,bob));
        assertFalse(sparseGraph.hasEdge(eve,bob));
        assertFalse(sparseGraph.removeEdge(alice,bob));
        assertFalse(sparseGraph.removeEdge(eve,bob));
        assertFalse(sparseGraph.addEdge(eve,bob));
        assertFalse(sparseGraph.addEdge(alice,eve));
    }
}
