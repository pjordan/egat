/*
 * StrategicBestFirstFormationSearchTest.java
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
package egat.minform.search;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.util.Set;

import egat.gamexml.StrategicGameHandler;
import egat.gamexml.SymmetricGameHandler;
import egat.minform.LpSolveSymmetricRationalizableFinder;

/**
 * @author Patrick R. Jordan
 */
public class SymmetricBestFirstFormationSearchTest {
    @Test
    public void test() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( SymmetricBestFirstFormationSearchTest.class.getResourceAsStream("/symmetric.xml") , handler);


        LpSolveSymmetricRationalizableFinder finder = new LpSolveSymmetricRationalizableFinder();

        SymmetricGame game = handler.getGame();

        SymmetricBestFirstFormationSearch search = new SymmetricBestFirstFormationSearch(game, finder);

        assertNotNull(finder);
        assertNotNull(game);
        assertNotNull(search);

        FormationSearchNode<SymmetricGame, Set<Action>> node1 = search.run(1);
        FormationSearchNode<SymmetricGame, Set<Action>> node2 = search.run(3);

        assertEquals(node1.getGame().getActions().size(),1);
        assertEquals(node2.getGame().getActions().size(),1);
    }

    
}