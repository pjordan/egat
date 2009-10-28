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
import edu.umich.eecs.ai.egat.game.*;

/**
 * @author Patrick R. Jordan
 */
public class SymmetricConstrainedNaiveSearchTest {
    @Test
    public void testLP() throws SAXException, ParserConfigurationException, IOException {
        /*SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( new GZIPInputStream(SymmetricConstrainedNaiveSearchTest.class.getResourceAsStream("/tac-travel.xml.gz") ) , handler);


        SymmetricGame game = handler.getGame();

        SymmetricConstrainedNaiveSearch rd = new SymmetricConstrainedNaiveSearch(1e-12,1);

        Action[] actions = game.getActions().toArray(new Action[0]);
        Set<Action> restricted = new HashSet<Action>();

        restricted.add(actions[0]);
        restricted.add(actions[1]);
        restricted.add(actions[2]);

        //Strategy strategy = rd.run(game, null,restricted);
        //assertNotNull(strategy);*/
    }

    @Test
    public void testMapping() throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        SymmetricGameHandler handler = new SymmetricGameHandler();


        parser.parse( new GZIPInputStream(SymmetricConstrainedNaiveSearchTest.class.getResourceAsStream("/tac-travel.xml.gz") ) , handler);


        SymmetricGame game = handler.getGame();


        //Set<Action> restricted = new HashSet<Action>();

        //restricted.add(Games.createAction("28"));
        //restricted.add(Games.createAction("16"));
        //restricted.add(Games.createAction("38"));
        //restricted.add(Games.createAction("42"));
        //restricted.add(Games.createAction("25"));
        //restricted.add(Games.createAction("45"));

        Action[] actions = game.getActions().toArray(new Action[0]);
        Set<Action> restricted = new HashSet<Action>();

        restricted.add(actions[0]);
        restricted.add(actions[1]);
        restricted.add(actions[2]);

        Player[] players = game.players().toArray(new Player[0]);
        //Action[] actions = game.getActions().toArray(new Action[0]);

        PayoffMatrix pm = createPayoffMatrix(game, players, actions);


        double[] distribution = new double[actions.length];

        for(double a1 = 0.0; a1 <= 1.0; a1 += 0.005) {
            //boolean first = true;
            //for(double a2 = 0.0; a2 <= 1.0; a2 += 0.005) {
            //    if(!first) {
            //        System.out.append(",");
            //    } else {
            //        first = false;
                //}
                //if(a1+a2 > 1.0) {
                //    System.out.append("NaN");
                //} else {
                    distribution[0] = a1;
                    distribution[1] = 1-a1;
                    //distribution[2] = 1.0-a1-a2;

                    System.out.append(""+pm.regret(distribution));

                    distribution[0] = 0;
                    distribution[1] = 0;
                    //distribution[2] = 0;
                //}
            //}
            System.out.println();
        }

    } 

    private PayoffMatrix createPayoffMatrix(SymmetricGame game, Player[] players, Action[] actions) {

        Action[] playerActions = new Action[players.length];

        PayoffMatrix pm = new PayoffMatrix(players.length, actions.length);

        int[] indices = new int[players.length];

        for (int i = 0; i < pm.getTotalSize(); i++) {
            pm.expand(i, indices);

            for (int j = 0; j < players.length; j++) {
                playerActions[j] = actions[indices[j]];
            }

            Outcome outcome = Games.createOutcome(players, playerActions);

            Payoff p = game.payoff(outcome);
            pm.setPayoff(i, p.getPayoff(players[0]).getValue());
        }


        return pm;
    }
}