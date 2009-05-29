package edu.umich.eecs.ai.egat.dominance;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;

import edu.umich.eecs.ai.egat.gamexml.StrategicGameHandler;
import edu.umich.eecs.ai.egat.game.*;

/**
 * @author Patrick Jordan
 */
public class MixedStrategicDominanceTesterImplTest {
    @Test
    public void testIsDominatedAction() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        StrategicGame game = handler.getGame();

        assertNotNull(game);

        MixedStrategicDominanceTesterImpl mixedStrategicDominanceTesterImpl = new MixedStrategicDominanceTesterImpl();

        assertNotNull(game);

        Player alice = Games.createPlayer("alice");
        Action c = Games.createAction("c");
        Action d = Games.createAction("d");

        assertTrue(mixedStrategicDominanceTesterImpl.isDominated(alice,c,game));
        assertFalse(mixedStrategicDominanceTesterImpl.isDominated(alice,d,game));        
    }


    @Test
    public void testIsDominatedStrategy() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        StrategicGame game = handler.getGame();

        assertNotNull(game);

        MixedStrategicDominanceTesterImpl mixedStrategicDominanceTesterImpl = new MixedStrategicDominanceTesterImpl();

        assertNotNull(game);

        Player alice = Games.createPlayer("alice");
        Strategy c = Games.createPureStrategy("c");
        Strategy d = Games.createPureStrategy("d");

        assertTrue(mixedStrategicDominanceTesterImpl.isDominated(alice,c,game));
        assertFalse(mixedStrategicDominanceTesterImpl.isDominated(alice,d,game));
    }
}
