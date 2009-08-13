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
package edu.umich.eecs.ai.egat.minform.search;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import edu.umich.eecs.ai.egat.gamexml.StrategicGameHandler;
import edu.umich.eecs.ai.egat.minform.LpSolveStrategicRationalizableFinder;
import edu.umich.eecs.ai.egat.game.*;

/**
 * @author Patrick R. Jordan
 */
public class StrategicBestFirstFormationSearchTest {
    
    @Test
    public void test() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();


        parser.parse( StrategicBestFirstFormationSearchTest.class.getResourceAsStream("/strategic.xml") , handler);


        LpSolveStrategicRationalizableFinder finder = new LpSolveStrategicRationalizableFinder();

        StrategicGame game = handler.getGame();

        StrategicBestFirstFormationSearch search = new StrategicBestFirstFormationSearch(game, finder);

        assertNotNull(finder);
        assertNotNull(game);
        assertNotNull(search);

        FormationSearchNode<StrategicGame, Map<Player,Set<Action>>> node1 = search.run(1);
        FormationSearchNode<StrategicGame, Map<Player,Set<Action>>> node2 = search.run(4);

        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");
        assertEquals(node1.getGame().getActions(alice).size(),1);
        assertEquals(node1.getGame().getActions(bob).size(),1);

        assertEquals(node2.getGame().getActions(alice).size(),1);
        assertEquals(node2.getGame().getActions(bob).size(),1);
    }
}
