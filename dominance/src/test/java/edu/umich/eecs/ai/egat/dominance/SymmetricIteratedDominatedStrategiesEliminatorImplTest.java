/*
 * SymmetricIteratedDominatedStrategiesEliminatorImplTest.java
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
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;

import edu.umich.eecs.ai.egat.gamexml.SymmetricGameHandler;
import edu.umich.eecs.ai.egat.game.DefaultSymmetricGame;
import edu.umich.eecs.ai.egat.game.SymmetricGame;

/**
 * @author Patrick Jordan
 */
public class SymmetricIteratedDominatedStrategiesEliminatorImplTest {
    @Test
    public void testIEDS() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/symmetric.xml") , handler);

        DefaultSymmetricGame game = (DefaultSymmetricGame) handler.getGame();

        assertNotNull(game);

        SymmetricIteratedDominatedStrategiesEliminatorImpl eliminator = new SymmetricIteratedDominatedStrategiesEliminatorImpl();

        SymmetricGame reduced = eliminator.eliminateDominatedStrategies(game);

        assertEquals(reduced.getActions().size(),1,0);

        parser.parse( DominanceUtilsTest.class.getResourceAsStream("/symmetric.xml") , handler);

        game = (DefaultSymmetricGame) handler.getGame();

        eliminator = new SymmetricIteratedDominatedStrategiesEliminatorImpl(new PureSymmetricDominanceTesterImpl());

        reduced = eliminator.eliminateDominatedStrategies(game);

        assertEquals(reduced.getActions().size(),1,0);
    }
}
