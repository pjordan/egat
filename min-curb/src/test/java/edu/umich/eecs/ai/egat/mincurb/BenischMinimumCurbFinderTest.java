package edu.umich.eecs.ai.egat.mincurb;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.util.Set;

import edu.umich.eecs.ai.egat.gamexml.SymmetricGameHandler;
import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Games;

/**
 * @author Patrick Jordan
 */
public class BenischMinimumCurbFinderTest {
    @Test
    public void testAllMinCurb() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( BenischSymmetricAllRationalizableFinderTest.class.getResourceAsStream("/symmetric.xml") , handler);

        BenischMinimumCurbFinder finder = new BenischMinimumCurbFinder();

        SymmetricGame game = handler.getGame();

        assertNotNull(finder);

        assertNotNull(game);


        Set<SymmetricGame> minCurbs = finder.findAllMinimumCurbs(game);


        assertNotNull(minCurbs);

        assertEquals(minCurbs.size(),1,0);
    }

    @Test
    public void testMinCurb() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( BenischSymmetricAllRationalizableFinderTest.class.getResourceAsStream("/symmetric.xml") , handler);

        BenischMinimumCurbFinder finder = new BenischMinimumCurbFinder();

        SymmetricGame game = handler.getGame();

        assertNotNull(finder);

        assertNotNull(game);


        SymmetricGame minCurb = finder.findMinimumCurb(game);


        assertNotNull(minCurb);

        assertEquals(minCurb.getActions().size(),1,0);
    }
}
