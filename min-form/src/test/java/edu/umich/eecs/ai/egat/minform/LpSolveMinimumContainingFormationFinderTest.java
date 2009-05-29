package edu.umich.eecs.ai.egat.minform;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;

import edu.umich.eecs.ai.egat.gamexml.SymmetricGameHandler;
import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Games;

import java.io.IOException;


/**
 * @author Patrick Jordan
 */
public class LpSolveMinimumContainingFormationFinderTest {

    @Test
    public void testSymmetricReduction() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( LpSolveRationalizableFinderTest.class.getResourceAsStream("/symmetric.xml") , handler);

        LpSolveMinimumContainingFormationFinder finder = new LpSolveMinimumContainingFormationFinder();

        SymmetricGame game = handler.getGame();

        assertNotNull(finder);

        assertNotNull(game);


        SymmetricGame reducedGameC = finder.findMinContainingFormation(Games.createAction("c"),game);
        SymmetricGame reducedGameD = finder.findMinContainingFormation(Games.createAction("d"),game);

        assertNotNull(reducedGameC);
        assertNotNull(reducedGameD);
    }
}
