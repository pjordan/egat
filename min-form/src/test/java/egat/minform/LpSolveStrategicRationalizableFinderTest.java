/*
 * LpSolveRationalizableFinderTest.java
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

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

import egat.gamexml.SymmetricGameHandler;
import egat.gamexml.StrategicGameHandler;

/**
 * @author Patrick Jordan
 */
public class LpSolveStrategicRationalizableFinderTest {

    @Test
    public void testLP() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();


        parser.parse( LpSolveStrategicRationalizableFinderTest.class.getResourceAsStream("/strategic.xml") , handler);


        LpSolveStrategicRationalizableFinder finder = new LpSolveStrategicRationalizableFinder();

        StrategicGame game = handler.getGame();

        assertNotNull(finder);
        assertNotNull(game);

        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");
        Action c = Games.createAction("c");
        Action d = Games.createAction("d");


        Set<Action> initalActions = new HashSet<Action>();
        initalActions.add(c);

        Set<Action> otherActions = new HashSet<Action>();
        otherActions.add(d);

        ActionReducedStrategicMultiAgentSystem reducedGame = new ActionReducedStrategicMultiAgentSystem(game, alice, initalActions);
        reducedGame = new ActionReducedStrategicMultiAgentSystem(reducedGame, bob, initalActions);

        Set<Action> rationalized = finder.findRationalizable(alice, reducedGame, game);


        assertNotNull(rationalized);
        assertEquals(rationalized.size(), 1, 0);

        assertEquals(2.0, finder.rationalizableSlack(alice, d, reducedGame, game), 0.01);
        assertEquals(-2.0, finder.rationalizableSlack(alice, c, reducedGame, game), 0.01);
        assertEquals(2.0, finder.rationalizableEpsilon(reducedGame, game), 0.01);

        reducedGame = new ActionReducedStrategicMultiAgentSystem(game, alice, otherActions);
        reducedGame = new ActionReducedStrategicMultiAgentSystem(reducedGame, bob, otherActions);
        rationalized = finder.findRationalizable(alice, reducedGame, game);
        
        assertNotNull(rationalized);
        assertEquals(rationalized.size(), 0, 0);

        assertEquals(1.0, finder.rationalizableSlack(alice, d, reducedGame, game), 0.01);
        assertEquals(-1.0, finder.rationalizableSlack(alice, c, reducedGame, game), 0.01);
        assertEquals(0.0, finder.rationalizableEpsilon(reducedGame, game), 0.01);
    }
}