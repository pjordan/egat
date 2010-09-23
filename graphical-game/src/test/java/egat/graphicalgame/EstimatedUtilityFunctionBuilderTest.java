/*
 * EstimatedUtilityFunctionBuilderTest.java
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
package egat.graphicalgame;

import org.junit.Test;
import static org.junit.Assert.*;
import egat.game.Player;
import egat.game.Games;
import egat.game.Action;
import egat.game.DefaultStrategicMultiAgentSystem;
import egat.graphicalgame.graphs.BasicSparseGraph;
import egat.graphicalgame.graphs.MutableGraph;
import egat.graphicalgame.utility.EstimatedUtilityNode;

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
