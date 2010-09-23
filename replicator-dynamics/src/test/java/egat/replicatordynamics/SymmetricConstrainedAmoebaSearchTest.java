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
package egat.replicatordynamics;

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

import egat.gamexml.SymmetricGameHandler;
import egat.game.SymmetricGame;
import egat.game.Action;
import egat.game.Strategy;

/**
 * @author Patrick R. Jordan
 */
public class SymmetricConstrainedAmoebaSearchTest {
    @Test
    public void testLP() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( new GZIPInputStream(SymmetricConstrainedAmoebaSearchTest.class.getResourceAsStream("/tac-travel.xml.gz") ) , handler);


        SymmetricGame game = handler.getGame();

        SymmetricConstrainedFeasibleAmoebaSearch rd = new SymmetricConstrainedFeasibleAmoebaSearch(1e-12,5000);

        Action[] actions = game.getActions().toArray(new Action[0]);
        Set<Action> restricted = new HashSet<Action>();

        restricted.add(actions[0]);
        restricted.add(actions[1]);
        restricted.add(actions[2]);
        //restricted.add(actions[3]);
        //restricted.add(actions[4]);
        //restricted.add(actions[5]);
        //restricted.add(Games.createAction("16"));
        //restricted.add(Games.createAction("38"));
        //restricted.add(Games.createAction("42"));
        //restricted.add(Games.createAction("25"));
        //restricted.add(Games.createAction("45"));

        Strategy strategy = rd.run(game, null,restricted,true);
        assertNotNull(strategy);
        StrategySearchResult result = rd.runForResult(game, null,restricted,true);
        assertNotNull(result);
    }
}