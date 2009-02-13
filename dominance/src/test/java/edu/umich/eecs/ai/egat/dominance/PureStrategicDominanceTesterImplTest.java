package edu.umich.eecs.ai.egat.dominance;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;

import edu.umich.eecs.ai.egat.gamexml.StrategicGameHandler;
import edu.umich.eecs.ai.egat.game.*;
import static edu.umich.eecs.ai.egat.dominance.DominanceUtils.createPlayerReducedStrategicSimulation;

/**
 * @author Patrick Jordan
 */
public class PureStrategicDominanceTesterImplTest {

    @Test
    public void testIsDominatedAction() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        StrategicGame game = handler.getGame();

        assertNotNull(game);

        PureStrategicDominanceTesterImpl pureStrategicDominanceTester = new PureStrategicDominanceTesterImpl();

        assertNotNull(game);

        Player alice = Games.createPlayer("alice");
        Action c = Games.createAction("c");
        Action d = Games.createAction("d");

        assertTrue(pureStrategicDominanceTester.isDominated(alice,c,game));
        assertFalse(pureStrategicDominanceTester.isDominated(alice,d,game));
    }


    @Test
    public void testIsDominatedStrategy() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        StrategicGame game = handler.getGame();

        assertNotNull(game);

        PureStrategicDominanceTesterImpl pureStrategicDominanceTester = new PureStrategicDominanceTesterImpl();

        assertNotNull(game);

        Player alice = Games.createPlayer("alice");
        Strategy c = Games.createPureStrategy("c");
        Strategy d = Games.createPureStrategy("d");

        assertTrue(pureStrategicDominanceTester.isDominated(alice,c,game));
        assertFalse(pureStrategicDominanceTester.isDominated(alice,d,game));
    }
}
