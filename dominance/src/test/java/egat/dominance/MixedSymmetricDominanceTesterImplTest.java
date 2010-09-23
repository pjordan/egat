/*
 * MixedSymmetricDominanceTesterImplTest.java
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
package egat.dominance;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;

import egat.gamexml.StrategicGameHandler;
import egat.gamexml.SymmetricGameHandler;

/**
 * @author Patrick Jordan
 */
public class MixedSymmetricDominanceTesterImplTest {

    @Test
    public void testIsDominatedAction() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/symmetric.xml") , handler);

        SymmetricGame game = handler.getGame();

        assertNotNull(game);

        MixedSymmetricDominanceTesterImpl mixedSymmetricDominanceTester = new MixedSymmetricDominanceTesterImpl();

        assertNotNull(game);

        Action c = Games.createAction("c");
        Action d = Games.createAction("d");

        assertTrue(mixedSymmetricDominanceTester.isDominated(c,game));
        assertFalse(mixedSymmetricDominanceTester.isDominated(d,game));
    }


    @Test
    public void testIsDominatedStrategy() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/symmetric.xml") , handler);

        SymmetricGame game = handler.getGame();

        assertNotNull(game);

        MixedSymmetricDominanceTesterImpl mixedSymmetricDominanceTester = new MixedSymmetricDominanceTesterImpl();

        assertNotNull(game);

        Strategy c = Games.createPureStrategy("c");
        Strategy d = Games.createPureStrategy("d");

        assertTrue(mixedSymmetricDominanceTester.isDominated(c,game));
        assertFalse(mixedSymmetricDominanceTester.isDominated(d,game));
    }
}