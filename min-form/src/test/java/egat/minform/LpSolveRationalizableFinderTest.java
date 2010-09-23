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

        SymmetricMultiAgentSystem otherReducedGame = new ActionReducedSymmetricGame(game, otherActions);
        SymmetricMultiAgentSystem initialReducedGame = new ActionReducedSymmetricGame(game, initalActions);

        Set<Action> rationalized = finder.findRationalizable(otherActions, game);


        assertNotNull(rationalized);
        assertEquals(rationalized.size(), 1, 0);

        assertEquals(2.0, finder.rationalizableSlack(actions[0], otherActions, game), 0.01);
        assertEquals(2.0, finder.rationalizableEpsilon(otherReducedGame, game), 0.01);

        rationalized = finder.findRationalizable(initalActions, game);

        assertNotNull(rationalized);
        assertEquals(rationalized.size(), 0, 0);
        assertEquals(0.0, finder.rationalizableEpsilon(initialReducedGame, game), 0.01);


    }
}
