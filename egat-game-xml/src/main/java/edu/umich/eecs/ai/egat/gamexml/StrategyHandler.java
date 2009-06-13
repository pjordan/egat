/*
 * StrategyHandler.java
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
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.Games;
import edu.umich.eecs.ai.egat.game.Strategy;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Patrick Jordan
 */
public class StrategyHandler extends DefaultHandler {
    private List<Action> actionList;
    private List<Number> probabilityList;
    private Strategy strategy;

    public StrategyHandler() {
        actionList = new ArrayList<Action>();
        probabilityList = new ArrayList<Number>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("strategy")) {
            handleStartStrategy(attributes);
        } else if (qName.equals("action")) {
            handleStartAction(attributes);
        }
    }

    private void handleStartAction(Attributes attributes) {
        Action action = Games.createAction(attributes.getValue("id"));
        Number probability = new Double(attributes.getValue("probability"));

        actionList.add(action);
        probabilityList.add(probability);
    }

    private void handleStartStrategy(Attributes attributes) {

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("strategy")) {
            handleEndStrategy();
        } else if (qName.equals("action")) {
            handleEndAction();
        }
    }

    private void handleEndAction() {
            
    }

    private void handleEndStrategy() {
        strategy = Games.createStrategy(actionList.toArray(new Action[0]), probabilityList.toArray(new Number[0]));

        actionList.clear();
        probabilityList.clear();
    }

    public Strategy getStrategy() {
        return strategy;
    }
}
