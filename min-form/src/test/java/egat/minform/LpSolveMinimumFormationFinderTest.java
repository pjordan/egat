/*
 * LpSolveMinimumFormationFinderTest.java
 *
 * Copyright (C) 2006-2009 Patrick R. Jordan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package egat.minform;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import egat.gamexml.SymmetricGameHandler;
import egat.game.SymmetricGame;

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
