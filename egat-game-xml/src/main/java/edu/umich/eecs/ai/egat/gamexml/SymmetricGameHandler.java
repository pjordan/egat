/*
 * SymmetricGameHandler.java
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
package edu.umich.eecs.ai.egat.gamexml;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import edu.umich.eecs.ai.egat.game.*;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class SymmetricGameHandler extends DefaultHandler {
    DefaultSymmetricGame<PayoffValue> game;
    Map<String, Player> playerNames;
    Map<String, Action> actionNames;
    Player[] players;
    Action[] actions;
    Map<Action, PayoffValue> actionMap;
    int index;
    int count;

    public SymmetricGameHandler() {
        game = new DefaultSymmetricGame<PayoffValue>();
        actionMap = new HashMap<Action,PayoffValue>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("nfg")) {
            handleStartNfg(attributes);
        } else if (qName.equals("player")) {
            handleStartPlayer(attributes);
        } else if (qName.equals("players")) {
            handleStartPlayers(attributes);
        } else if (qName.equals("actions")) {
            handleStartPlayers(attributes);
        } else if (qName.equals("action")) {
            handleStartAction(attributes);
        } else if (qName.equals("payoffs")) {
            handleStartPayoffs(attributes);
        } else if (qName.equals("payoff")) {
            handleStartPayoff(attributes);
        } else if (qName.equals("outcome")) {
            handleStartOutcome(attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("nfg")) {
            handleEndNfg();
        } else if (qName.equals("player")) {
            handleEndPlayer();
        } else if (qName.equals("players")) {
            handleEndPlayers();
        } else if (qName.equals("action")) {
            handleEndAction();
        } else if (qName.equals("actions")) {
            handleEndActions();
        } else if (qName.equals("payoff")) {
            handleEndPayoff();
        } else if (qName.equals("payoffs")) {
            handleEndPayoffs();
        } else if (qName.equals("outcome")) {
            handleEndOutcome();
        }
    }

    private void handleStartNfg(Attributes attributes) throws SAXException {
        game.setName(attributes.getValue("name"));
        game.setDescription(attributes.getValue("description"));
    }

    private void handleStartPlayer(Attributes attributes) throws SAXException {
        game.addPlayer(Games.createPlayer(attributes.getValue("id")));
    }

    private void handleStartActions(Attributes attributes) throws SAXException {
    }

    private void handleStartPlayers(Attributes attributes) throws SAXException {
    }

    private void handleStartAction(Attributes attributes) throws SAXException {
        game.addAction(Games.createAction(attributes.getValue("id")));
    }

    private void handleStartPayoffs(Attributes attributes) throws SAXException {
        playerNames = new HashMap<String, Player>();
        actionNames = new HashMap<String, Action>();
        for (Player p : game.players()) {
            playerNames.put(p.getID(), p);
        }

        for (Action a : game.getActions()) {
            actionNames.put(a.getID(), a);
        }
        players = game.players().toArray(new Player[0]);
        actions = new Action[players.length];
    }

    private void handleStartPayoff(Attributes attributes) throws SAXException {
        actionMap = new HashMap<Action,PayoffValue>();
        index = 0;
    }

    private void handleStartOutcome(Attributes attributes) throws SAXException {
        Action a = actionNames.get(attributes.getValue("action"));

        int count = Integer.parseInt(attributes.getValue("count"));
        for(int i = 0; i < count; i++)
            actions[index++] = a;

        actionMap.put(a, PayoffFactory.createPayoffValue(Double.parseDouble(attributes.getValue("value"))));
    }

    private void handleEndNfg() throws SAXException {
    }

    private void handleEndPlayer() throws SAXException {

    }

    private void handleEndPlayers() throws SAXException {

    }

    private void handleEndAction() throws SAXException {
    }

    private void handleEndActions() throws SAXException {
    }

    private void handleEndPayoff() throws SAXException {
        SymmetricOutcome o = Games.createSymmetricOutcome(players, actions);
        game.putPayoff(o,actionMap);
    }

    private void handleEndPayoffs() throws SAXException {

    }

    private void handleEndOutcome() throws SAXException {

    }

    public MutableSymmetricGame<PayoffValue> getGame() {
        return game;
    }
}
