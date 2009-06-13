/*
 * ProfileHandler.java
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

import edu.umich.eecs.ai.egat.game.*;

import java.util.List;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Patrick Jordan
 */
public class ProfileHandler extends DefaultHandler {
    private List<Player> playerList;
    private List<Strategy> strategyList;
    private List<Action> actionList;
    private List<Number> probabilityList;
    private Profile profile;

    public ProfileHandler() {
        actionList = new ArrayList<Action>();
        probabilityList = new ArrayList<Number>();
        playerList= new ArrayList<Player>();
        strategyList = new ArrayList<Strategy>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("profile")) {
            handleStartProfile(attributes);
        } else if (qName.equals("strategy")) {
            handleStartStrategy(attributes);
        } else if (qName.equals("action")) {
            handleStartAction(attributes);
        }
    }

    private void handleStartProfile(Attributes attributes) {

    }

    private void handleStartAction(Attributes attributes) {
        Action action = Games.createAction(attributes.getValue("id"));
        Number probability = new Double(attributes.getValue("probability"));

        actionList.add(action);
        probabilityList.add(probability);
    }

    private void handleStartStrategy(Attributes attributes) {
        Player player = Games.createPlayer(attributes.getValue("player"));

        playerList.add(player);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("profile")) {
            handleEndProfile();
        } else if (qName.equals("strategy")) {
            handleEndStrategy();
        } else if (qName.equals("action")) {
            handleEndAction();
        }
    }

    private void handleEndProfile() {
        Player[] players = playerList.toArray(new Player[0]);
        Strategy[] strategies = strategyList.toArray(new Strategy[0]);

        profile = Games.createProfile(players,strategies);
    }

    private void handleEndAction() {

    }

    private void handleEndStrategy() {
        Strategy strategy = Games.createStrategy(actionList.toArray(new Action[0]), probabilityList.toArray(new Number[0]));

        actionList.clear();
        probabilityList.clear();

        strategyList.add(strategy);
    }

    public Profile getProfile() {
        return profile;
    }
}
