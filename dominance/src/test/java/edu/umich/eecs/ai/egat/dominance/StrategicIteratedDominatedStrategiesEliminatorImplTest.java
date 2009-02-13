package edu.umich.eecs.ai.egat.dominance;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;

import edu.umich.eecs.ai.egat.gamexml.StrategicGameHandler;
import edu.umich.eecs.ai.egat.game.DefaultStrategicGame;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Games;

/**
 * @author Patrick Jordan
 */
public class StrategicIteratedDominatedStrategiesEliminatorImplTest {
    @Test
    public void testIEDS() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        DefaultStrategicGame game = (DefaultStrategicGame) handler.getGame();

        assertNotNull(game);

        StrategicIteratedDominatedStrategiesEliminatorImpl eliminator = new StrategicIteratedDominatedStrategiesEliminatorImpl();

        eliminator.eliminateDominatedStrategies(game);

        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");

        assertEquals(game.getActions(alice).size(),1,0);
        assertEquals(game.getActions(bob).size(),1,0);

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        game = (DefaultStrategicGame) handler.getGame();

        eliminator = new StrategicIteratedDominatedStrategiesEliminatorImpl(new PureStrategicDominanceTesterImpl());

        eliminator.eliminateDominatedStrategies(game);

        assertEquals(game.getActions(alice).size(),1,0);
        assertEquals(game.getActions(bob).size(),1,0);
    }
}
