/*
 * SymmetricConstrainedReplicatorDynamicsTest.java
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
package edu.umich.eecs.ai.egat.replicatordynamics;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import edu.umich.eecs.ai.egat.gamexml.SymmetricGameHandler;
import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.Strategy;
import edu.umich.eecs.ai.egat.game.Games;

/**
 * @author Patrick R. Jordan
 */
public class SymmetricConstrainedPatternSearchTest {
    @Test
    public void testLP() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( new GZIPInputStream(SymmetricConstrainedPatternSearchTest.class.getResourceAsStream("/tac-travel.xml.gz") ) , handler);


        SymmetricGame game = handler.getGame();

        SymmetricConstrainedPatternSearch rd = new SymmetricConstrainedPatternSearch(1e-12,5000);

        Action[] actions = game.getActions().toArray(new Action[0]);
        Set<Action> restricted = new HashSet<Action>();

        restricted.add(Games.createAction("28"));
        restricted.add(Games.createAction("16"));
        restricted.add(Games.createAction("38"));
        restricted.add(Games.createAction("42"));
        restricted.add(Games.createAction("25"));
        restricted.add(Games.createAction("45"));

        Strategy strategy = rd.run(game, null,restricted);
        assertNotNull(strategy);
    }
}