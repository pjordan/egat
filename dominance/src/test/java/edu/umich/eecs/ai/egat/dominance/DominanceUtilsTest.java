package edu.umich.eecs.ai.egat.dominance;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import edu.umich.eecs.ai.egat.game.*;

import static edu.umich.eecs.ai.egat.dominance.DominanceUtils.*;
import edu.umich.eecs.ai.egat.gamexml.SymmetricGameHandler;
import edu.umich.eecs.ai.egat.gamexml.StrategicGameHandler;

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

        StrategicSimulation reduced = createPlayerReducedStrategicSimulation(alice, game);

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

        SymmetricSimulation reduced = createPlayerReducedSymmetricSimulation(alice, game);

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
