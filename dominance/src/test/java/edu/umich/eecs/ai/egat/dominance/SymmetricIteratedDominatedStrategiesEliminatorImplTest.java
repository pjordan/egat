package edu.umich.eecs.ai.egat.dominance;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;

import edu.umich.eecs.ai.egat.gamexml.SymmetricGameHandler;
import edu.umich.eecs.ai.egat.game.DefaultSymmetricGame;

/**
 * @author Patrick Jordan
 */
public class SymmetricIteratedDominatedStrategiesEliminatorImplTest {
    @Test
    public void testIEDS() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/symmetric.xml") , handler);

        DefaultSymmetricGame game = (DefaultSymmetricGame) handler.getGame();

        assertNotNull(game);

        SymmetricIteratedDominatedStrategiesEliminatorImpl eliminator = new SymmetricIteratedDominatedStrategiesEliminatorImpl();

        eliminator.eliminateDominatedStrategies(game);

        assertEquals(game.getActions().size(),1,0);

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/symmetric.xml") , handler);

        game = (DefaultSymmetricGame) handler.getGame();

        eliminator = new SymmetricIteratedDominatedStrategiesEliminatorImpl(new PureSymmetricDominanceTesterImpl());

        eliminator.eliminateDominatedStrategies(game);

        assertEquals(game.getActions().size(),1,0);
    }
}
