/*
 * DefaultStrategicEstimatedGraphicalGameTest.java
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
import egat.game.*;
import egat.graphicalgame.graphs.Graph;

import java.util.HashSet;
import java.util.Arrays;

/**
 * @author Patrick Jordan
 */
public class DefaultStrategicEstimatedGraphicalGameTest {

    @Test
    public void testReallySmall() {
        Player alice = Games.createPlayer("alice");

        Player bob = Games.createPlayer("bob");

        Action[] aliceActions = new Action[] {Games.createAction("a1"), Games.createAction("a2")};

        Action[] bobActions = new Action[] {Games.createAction("b1"), Games.createAction("b2")};

        StrategicEstimatedGraphicalGameBuilder builder = new StrategicEstimatedGraphicalGameBuilder();

        builder.addPlayer(alice);

        for(Action action : aliceActions) {

            builder.addAction(alice, action);

        }

        builder.addPlayer(bob);

        for(Action action : bobActions) {

            builder.addAction(bob, action);

        }

        StrategicEstimatedGraphicalGame game = builder.build();

        Graph<Player> graph = game.getGraph();

        assertEquals(game.players(), new HashSet<Player>(Arrays.asList(alice, bob)));

        Player[] players = new Player[] {alice, bob};

        Outcome sample = Games.createOutcome(players, new Action[] {aliceActions[0], bobActions[0]});

        Payoff samplePayoff = PayoffFactory.createPayoff(new Player[] {alice, bob}, new double[]{1.0,2.0});

        Payoff testPayoff = PayoffFactory.createPayoff(new Player[] {alice, bob}, new double[]{1.0,2.0});

        game.addSample(sample,samplePayoff);

        for(Outcome outcome : Games.traversal(game)) {

            assertEquals(game.payoff(outcome), testPayoff);
            
        }
    }

    @Test
    public void testSmall() {
        Player alice = Games.createPlayer("alice");

        Player bob = Games.createPlayer("bob");

        Action[] aliceActions = new Action[] {Games.createAction("a1"), Games.createAction("a2")};

        Action[] bobActions = new Action[] {Games.createAction("b1"), Games.createAction("b2")};

        StrategicEstimatedGraphicalGameBuilder builder = new StrategicEstimatedGraphicalGameBuilder();

        builder.addPlayer(alice);

        for(Action action : aliceActions) {

            builder.addAction(alice, action);

        }

        builder.addPlayer(bob);

        for(Action action : bobActions) {

            builder.addAction(bob, action);

        }

        builder.addEdge(alice,alice);
        builder.addEdge(bob,bob);

        DefaultStrategicEstimatedGraphicalGame game = builder.build();

        Graph<Player> graph = game.getGraph();

        assertEquals(game.players(), new HashSet<Player>(Arrays.asList(alice, bob)));

        Player[] players = new Player[] {alice, bob};

        Outcome sample11 = Games.createOutcome(players, new Action[] {aliceActions[0], bobActions[0]});
        Outcome sample22 = Games.createOutcome(players, new Action[] {aliceActions[1], bobActions[1]});
        Outcome sample12 = Games.createOutcome(players, new Action[] {aliceActions[0], bobActions[1]});
        Outcome sample21 = Games.createOutcome(players, new Action[] {aliceActions[1], bobActions[0]});

        Payoff payoff11 = PayoffFactory.createPayoff(new Player[] {alice, bob}, new double[]{1.0,2.0});
        Payoff payoff22 = PayoffFactory.createPayoff(new Player[] {alice, bob}, new double[]{1.5,2.5});
        Payoff payoff12 = PayoffFactory.createPayoff(new Player[] {alice, bob}, new double[]{1.0,2.5});
        Payoff payoff21 = PayoffFactory.createPayoff(new Player[] {alice, bob}, new double[]{1.5,2.0});

        game.addSample(sample11,payoff11);
        game.addSample(sample22,payoff22);
        
        for(Outcome outcome : Games.traversal(game)) {
            if(outcome.equals(sample11)) {
                assertEquals(game.payoff(outcome), payoff11);
            }

            if(outcome.equals(sample12)) {
                assertEquals(game.payoff(outcome), payoff12);
            }

            if(outcome.equals(sample21)) {
                assertEquals(game.payoff(outcome), payoff21);
            }

            if(outcome.equals(sample22)) {
                assertEquals(game.payoff(outcome), payoff22);
            }
        }
    }
}
