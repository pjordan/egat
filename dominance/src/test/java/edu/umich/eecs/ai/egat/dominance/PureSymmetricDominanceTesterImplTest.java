package edu.umich.eecs.ai.egat.dominance;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;

import edu.umich.eecs.ai.egat.gamexml.StrategicGameHandler;
import edu.umich.eecs.ai.egat.gamexml.SymmetricGameHandler;
import edu.umich.eecs.ai.egat.game.*;
import static edu.umich.eecs.ai.egat.dominance.DominanceUtils.createPlayerReducedStrategicSimulation;

/**
 * @author Patrick Jordan
 */
public class PureSymmetricDominanceTesterImplTest {

    @Test
    public void testIsDominatedAction() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/symmetric.xml") , handler);

        SymmetricGame game = handler.getGame();

        assertNotNull(game);

        PureSymmetricDominanceTesterImpl pureSymmetricDominanceTester = new PureSymmetricDominanceTesterImpl();

        assertNotNull(game);

        Action c = Games.createAction("c");
        Action d = Games.createAction("d");

        assertTrue(pureSymmetricDominanceTester.isDominated(c,game));
        assertFalse(pureSymmetricDominanceTester.isDominated(d,game));
    }


    @Test
    public void testIsDominatedStrategy() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/symmetric.xml") , handler);

        SymmetricGame game = handler.getGame();

        assertNotNull(game);

        PureSymmetricDominanceTesterImpl pureSymmetricDominanceTester = new PureSymmetricDominanceTesterImpl();

        assertNotNull(game);

        Strategy c = Games.createPureStrategy("c");
        Strategy d = Games.createPureStrategy("d");

        assertTrue(pureSymmetricDominanceTester.isDominated(c,game));
        assertFalse(pureSymmetricDominanceTester.isDominated(d,game));
    }
}