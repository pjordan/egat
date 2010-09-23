/*
 * DominanceUtilsTest.java
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
package egat.dominance;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import static egat.dominance.DominanceUtils.*;
import egat.gamexml.SymmetricGameHandler;
import egat.gamexml.StrategicGameHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author Patrick Jordan
 */
public class DominanceUtilsTest {
    @Test
    public void testIndexForPlayer() {
        Player[] players = new Player[] {Games.createPlayer("a"), Games.createPlayer("b")};

        Player playerB = Games.createPlayer("b");

        Player playerC = Games.createPlayer("c");

        assertEquals(indexForPlayer(playerB, players),1,0);

        assertEquals(indexForPlayer(playerC, players),-1,0);
    }

    @Test
    public void testCreatePlayerReducedStrategicSimulation() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        StrategicGame game = handler.getGame();

        assertNotNull(game);

        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");

        StrategicMultiAgentSystem reduced = createPlayerReducedStrategicSimulation(alice, game);

        assertTrue(reduced.players().contains(bob));
        assertFalse(reduced.players().contains(alice));

        int count = 0;

        for(Outcome outome : Games.traversal(reduced)) {
            count++;
        }

        assertEquals(count,2,0);
    }

    @Test
    public void testCreatePlayerReducedSymmetricSimulation() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/symmetric.xml") , handler);

        SymmetricGame game = handler.getGame();

        assertNotNull(game);

        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");

        SymmetricMultiAgentSystem reduced = createPlayerReducedSymmetricSimulation(alice, game);

        assertTrue(reduced.players().contains(bob));
        assertFalse(reduced.players().contains(alice));

        int count = 0;

        for(Outcome outome : Games.symmetricTraversal(reduced)) {
            count++;
        }

        assertEquals(count,2,0);
    }

    @Test
    public void testGetPayoffAction() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        StrategicGame game = handler.getGame();

        assertNotNull(game);

        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");

        Action c = Games.createAction("c");

        Outcome outcome = Games.createOutcome(new Player[] {bob}, new Action[] {Games.createAction("d")});

        Player[] players = game.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

        int index = indexForPlayer(alice,players);
        assertEquals(getPayoff(c,outcome,players,actions,game,index),0.0,0.0);
    }

    @Test
    public void testGetPayoffStrategy() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        StrategicGame game = handler.getGame();

        assertNotNull(game);

        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");

        Strategy c = Games.createPureStrategy("c");

        Outcome outcome = Games.createOutcome(new Player[] {bob}, new Action[] {Games.createAction("d")});

        Player[] players = game.players().toArray(new Player[0]);
        Strategy[] strategies = new Strategy[players.length];

        int index = indexForPlayer(alice,players);
        assertEquals(getPayoff(c,outcome,players,strategies,game,index),0.0,0.0);
    }
}
