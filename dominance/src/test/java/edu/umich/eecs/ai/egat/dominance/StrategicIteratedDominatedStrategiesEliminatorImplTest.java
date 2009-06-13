/*
 * StrategicIteratedDominatedStrategiesEliminatorImplTest.java
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
package edu.umich.eecs.ai.egat.dominance;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;

import edu.umich.eecs.ai.egat.gamexml.StrategicGameHandler;
import edu.umich.eecs.ai.egat.game.DefaultStrategicGame;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Games;

/**
 * @author Patrick Jordan
 */
public class StrategicIteratedDominatedStrategiesEliminatorImplTest {
    @Test
    public void testIEDS() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        StrategicGameHandler handler = new StrategicGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        DefaultStrategicGame game = (DefaultStrategicGame) handler.getGame();

        assertNotNull(game);

        StrategicIteratedDominatedStrategiesEliminatorImpl eliminator = new StrategicIteratedDominatedStrategiesEliminatorImpl();

        eliminator.eliminateDominatedStrategies(game);

        Player alice = Games.createPlayer("alice");
        Player bob = Games.createPlayer("bob");

        assertEquals(game.getActions(alice).size(),1,0);
        assertEquals(game.getActions(bob).size(),1,0);

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/strategic.xml") , handler);

        game = (DefaultStrategicGame) handler.getGame();

        eliminator = new StrategicIteratedDominatedStrategiesEliminatorImpl(new PureStrategicDominanceTesterImpl());

        eliminator.eliminateDominatedStrategies(game);

        assertEquals(game.getActions(alice).size(),1,0);
        assertEquals(game.getActions(bob).size(),1,0);
    }
}
