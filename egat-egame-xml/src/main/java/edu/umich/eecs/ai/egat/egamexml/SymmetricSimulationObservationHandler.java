package edu.umich.eecs.ai.egat.egamexml;

import edu.umich.eecs.ai.egat.game.*;
import edu.umich.eecs.ai.egat.egame.SymmetricSimulationObserver;
import edu.umich.eecs.ai.egat.egame.AbstractSymmetricSimulationObserver;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class SymmetricSimulationObservationHandler extends DefaultHandler {
    SymmetricSimulationObserver observer;
    DefaultSymmetricMultiAgentSystem simulation;
    Map<String, Player> playerNames;
    Map<String, Action> actionNames;
    Player[] players;
    Action[] actions;
    Map<Action, PayoffValue> actionMap;
    int index;
    int count;

    public SymmetricSimulationObservationHandler() {
        actionMap = new HashMap<Action,PayoffValue>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("observer")) {
            handleStartObserver(attributes);
        } else if (qName.equals("simulation")) {
            handleStartSimulation(attributes);
        } else if (qName.equals("player")) {
            handleStartPlayer(attributes);
        } else if (qName.equals("players")) {
            handleStartPlayers(attributes);
        } else if (qName.equals("actions")) {
            handleStartActions(attributes);
        } else if (qName.equals("action")) {
            handleStartAction(attributes);
        } else if (qName.equals("observations")) {
            handleStartObservations(attributes);
        } else if (qName.equals("observation")) {
            handleStartObservation(attributes);
        } else if (qName.equals("outcome")) {
            handleStartOutcome(attributes);
        } else {
            throw new RuntimeException("invalid element");
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
        } else if (qName.equals("actions")) {
            handleEndActions();
        } else if (qName.equals("observation")) {
            handleEndObservation();
        } else if (qName.equals("observations")) {
            handleEndObservations();
        } else {
            handleEndOutcome();
        } 
    }

    private void handleStartObserver(Attributes attributes) throws SAXException {

    }

    private void handleStartSimulation(Attributes attributes) throws SAXException {
        simulation = new DefaultSymmetricMultiAgentSystem();
        simulation.setName(attributes.getValue("name"));
        simulation.setDescription(attributes.getValue("description"));
    }

    private void handleStartPlayer(Attributes attributes) throws SAXException {
        simulation.addPlayer(Games.createPlayer(attributes.getValue("id")));
    }

    private void handleStartActions(Attributes attributes) throws SAXException {
    }

    private void handleStartPlayers(Attributes attributes) throws SAXException {
    }

    private void handleStartAction(Attributes attributes) throws SAXException {
        simulation.addAction(Games.createAction(attributes.getValue("id")));
    }

    private void handleStartObservations(Attributes attributes) throws SAXException {
        playerNames = new HashMap<String, Player>();
        actionNames = new HashMap<String, Action>();
        for (Player p : simulation.players()) {
            playerNames.put(p.getID(), p);
        }

        for (Action a : simulation.getActions()) {
            actionNames.put(a.getID(), a);
        }
        players = simulation.players().toArray(new Player[0]);
        actions = new Action[players.length];
    }

    private void handleStartObservation(Attributes attributes) throws SAXException {
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

    private void handleEndObserver() throws SAXException {
    }

    private void handleEndSimulation() throws SAXException {
        observer = new AbstractSymmetricSimulationObserver(simulation);
    }

    private void handleEndPlayer() throws SAXException {

    }

    private void handleEndPlayers() throws SAXException {

    }

    private void handleEndAction() throws SAXException {
    }

    private void handleEndActions() throws SAXException {
    }

    private void handleEndObservation() throws SAXException {
        SymmetricOutcome o = Games.createSymmetricOutcome(players, actions);
        observer.observe(o,PayoffFactory.createSymmetricPayoff(actionMap,o));
    }

    private void handleEndObservations() throws SAXException {

    }

    private void handleEndOutcome() throws SAXException {

    }

    public SymmetricMultiAgentSystem getSimulation() {
        return simulation;
    }


    public SymmetricSimulationObserver getObserver() {
        return observer;
    }
}
