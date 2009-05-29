package edu.umich.eecs.ai.egat.minform;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

import edu.umich.eecs.ai.egat.gamexml.SymmetricGameHandler;
import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Action;

/**
 * @author Patrick Jordan
 */
public class LpSolveRationalizableFinderTest {

    @Test
    public void testLP() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( LpSolveRationalizableFinderTest.class.getResourceAsStream("/symmetric.xml") , handler);


        LpSolveSymmetricRationalizableFinder finder = new LpSolveSymmetricRationalizableFinder();

        SymmetricGame game = handler.getGame();

        assertNotNull(finder);
        assertNotNull(game);

        Action[] actions = game.getActions().toArray(new Action[0]);

        Set<Action> initalActions = new HashSet<Action>();
        initalActions.add(actions[0]);

        Set<Action> otherActions = new HashSet<Action>();
        otherActions.add(actions[1]);

        Set<Action> rationalized = finder.findRationalizable(otherActions, game);


        assertNotNull(rationalized);
        assertEquals(rationalized.size(), 1, 0);

        assertEquals(2.0, finder.rationalizableSlack(actions[0], otherActions, game), 0.01);

        rationalized = finder.findRationalizable(initalActions, game);

        assertNotNull(rationalized);
        assertEquals(rationalized.size(), 0, 0);


    }
}
