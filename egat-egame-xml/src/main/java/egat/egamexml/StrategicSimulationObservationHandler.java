/*
 * StrategicSimulationObservationHandler.java
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
package egat.egamexml;

import egat.game.*;
import egat.egame.AbstractStrategicSimulationObserver;
import egat.egame.StrategicSimulationObserver;

import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Patrick Jordan
 */
public class StrategicSimulationObservationHandler extends DefaultHandler {
    DefaultStrategicMultiAgentSystem simulation;
    StrategicSimulationObserver observer;
    Player currentPlayer;
    Set<Action> currentActions;
    List<Player> observationPlayers;
    List<Action> observationActions;
    List<Number> observationValues;
    Map<String,Player> playerNames;
    Map<String,Map<String,Action>> actionNames;

    int count;

    public StrategicSimulationObservationHandler() {
        simulation = new DefaultStrategicMultiAgentSystem();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("observer")) {
            handleStartOberver(attributes);
        } else if (qName.equals("simulation")) {
            handleStartSimulation(attributes);
        } else if (qName.equals("player")) {
            handleStartPlayer(attributes);
        } else if (qName.equals("players")) {
            handleStartPlayers(attributes);
        } else if (qName.equals("action")) {
            handleStartAction(attributes);
        } else if (qName.equals("observations")) {
            handleStartObservations(attributes);
        } else if (qName.equals("observation")) {
            handleStartObservation(attributes);
        } else if (qName.equals("outcome")) {
            handleStartOutcome(attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("observer")) {
            handleEndObserver();
        } else if (qName.equals("simulation")) {
            handleEndSimulation();
        } else if (qName.equals("player")) {
            handleEndPlayer();
        } else if (qName.equals("players")) {
            handleEndPlayers();
        } else if (qName.equals("action")) {
            handleEndAction();
        } else if (qName.equals("observation")) {
            handleEndObservation();
        } else if (qName.equals("observations")) {
            handleEndObservations();
        } else if (qName.equals("outcome")) {
            handleEndOutcome();
        }
    }

    private void handleStartOberver(Attributes attributes) throws SAXException {
        
    }

    private void handleStartSimulation(Attributes attributes) throws SAXException {
        simulation = new DefaultStrategicMultiAgentSystem();
        simulation.setName(attributes.getValue("name"));
        simulation.setDescription(attributes.getValue("description"));
    }

    private void handleStartPlayer(Attributes attributes) throws SAXException {
        currentPlayer = Games.createPlayer(attributes.getValue("id"));
        simulation.addPlayer(currentPlayer);
        currentActions = new HashSet<Action>();

    }

    private void handleStartPlayers(Attributes attributes) throws SAXException {
    }

    private void handleStartAction(Attributes attributes) throws SAXException {
        currentActions.add(Games.createAction(attributes.getValue("id")));
    }

    private void handleStartObservations(Attributes attributes) throws SAXException {
        playerNames = new HashMap<String,Player>();
        actionNames = new HashMap<String,Map<String,Action>>();
        for(Player p : simulation.players()) {
            playerNames.put(p.getID(),p);
            actionNames.put(p.getID(),new HashMap<String,Action>());

            for(Action a : simulation.getActions(p)) {
                actionNames.get(p.getID()).put(a.getID(),a);
            }
        }
    }

    private void handleStartObservation(Attributes attributes) throws SAXException {
        observationPlayers = new LinkedList<Player>();
        observationActions = new LinkedList<Action>();
        observationValues = new LinkedList<Number>();
    }

    private void handleStartOutcome(Attributes attributes) throws SAXException {
        observationPlayers.add(playerNames.get(attributes.getValue("player")));
        observationActions.add(actionNames.get(attributes.getValue("player")).get(attributes.getValue("action")));
        observationValues.add(new Double(attributes.getValue("value")));
    }

    private void handleEndObserver() throws SAXException {
    }

    private void handleEndSimulation() throws SAXException {
        observer = new AbstractStrategicSimulationObserver(simulation);
    }

    private void handleEndPlayer() throws SAXException {
        simulation.putActions(currentPlayer,currentActions);
        currentPlayer = null;
        currentActions = null;
    }

    private void handleEndPlayers() throws SAXException {

    }

    private void handleEndAction() throws SAXException {
    }

    private void handleEndObservation() throws SAXException {
        Player[] players = observationPlayers.toArray(new Player[0]);
        Action[] actions = observationActions.toArray(new Action[0]);
        Number[] values = observationValues.toArray(new Number[0]);
        double[] doubleValues = new double[values.length];

        for(int i = 0; i < values.length; i++) {
            doubleValues[i] = values[i].doubleValue();
        }

        Outcome o = Games.createOutcome(players, actions);
        Payoff p = PayoffFactory.createPayoff(players, doubleValues);
        observer.observe(o,p);

        observationActions = null;
        observationPlayers = null;
        observationValues = null;
    }

    private void handleEndObservations() throws SAXException {
        playerNames = null;
        actionNames = null;
    }

    private void handleEndOutcome() throws SAXException {

    }

    public StrategicMultiAgentSystem getSimulation() {
        return simulation;
    }


    public StrategicSimulationObserver getObserver() {
        return observer;
    }
}
