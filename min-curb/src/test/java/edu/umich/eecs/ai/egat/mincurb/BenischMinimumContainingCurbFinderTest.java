package edu.umich.eecs.ai.egat.mincurb;

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
public class BenischMinimumContainingCurbFinderTest {

    @Test
    public void testSymmetricReduction() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( BenischSymmetricAllRationalizableFinderTest.class.getResourceAsStream("/symmetric.xml") , handler);

        BenischMinimumContainingCurbFinder finder = new BenischMinimumContainingCurbFinder();

        SymmetricGame game = handler.getGame();

        assertNotNull(finder);

        assertNotNull(game);


        SymmetricGame reducedGameC = finder.findMinContainingCurb(Games.createAction("c"),game);
        SymmetricGame reducedGameD = finder.findMinContainingCurb(Games.createAction("d"),game);

        assertNotNull(reducedGameC);
        assertNotNull(reducedGameD);
    }
}
