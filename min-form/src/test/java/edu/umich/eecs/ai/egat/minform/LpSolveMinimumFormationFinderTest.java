package edu.umich.eecs.ai.egat.minform;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import edu.umich.eecs.ai.egat.gamexml.SymmetricGameHandler;
import edu.umich.eecs.ai.egat.game.SymmetricGame;

/**
 * @author Patrick Jordan
 */
public class LpSolveMinimumFormationFinderTest {
    @Test
    public void testAllMinForm() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( LpSolveRationalizableFinderTest.class.getResourceAsStream("/symmetric.xml") , handler);

        LpSolveMinimumFormationFinder finder = new LpSolveMinimumFormationFinder();

        SymmetricGame game = handler.getGame();

        assertNotNull(finder);

        assertNotNull(game);


        Set<SymmetricGame> minCurbs = finder.findAllMinimumFormations(game);


        assertNotNull(minCurbs);

        assertEquals(minCurbs.size(),1,0);
    }

    @Test
    public void testMinForm() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( LpSolveRationalizableFinderTest.class.getResourceAsStream("/symmetric.xml") , handler);

        LpSolveMinimumFormationFinder finder = new LpSolveMinimumFormationFinder();

        SymmetricGame game = handler.getGame();

        assertNotNull(finder);

        assertNotNull(game);


        SymmetricGame minCurb = finder.findMinimumFormation(game);


        assertNotNull(minCurb);

        assertEquals(minCurb.getActions().size(),1,0);
    }

    @Test
    public void testLargeMinForm() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( new GZIPInputStream(LpSolveRationalizableFinderTest.class.getResourceAsStream("/tac-travel.xml.gz") ) , handler);

        LpSolveMinimumFormationFinder finder = new LpSolveMinimumFormationFinder();

        SymmetricGame game = handler.getGame();

        assertNotNull(finder);

        assertNotNull(game);


        SymmetricGame minCurb = finder.findMinimumFormation(game);


        assertNotNull(minCurb);

        assertEquals(minCurb.getActions().size(),27,0);
    }
}
