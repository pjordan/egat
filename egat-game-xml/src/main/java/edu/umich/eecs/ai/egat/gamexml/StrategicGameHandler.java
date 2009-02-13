/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.gamexml;

import edu.umich.eecs.ai.egat.game.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class StrategicGameHandler extends DefaultHandler {
    DefaultStrategicGame<PayoffValue> game;
    Player currentPlayer;
    Set<Action> currentActions;
    List<Player> payoffPlayers;
    List<Action> payoffActions;
    List<Number> payoffValues;
    Map<String,Player> playerNames;
    Map<String,Map<String,Action>> actionNames;

    int count;

    public StrategicGameHandler() {
        game = new DefaultStrategicGame<PayoffValue>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("nfg")) {
            handleStartNfg(attributes);
        } else if (qName.equals("player")) {
            handleStartPlayer(attributes);
        } else if (qName.equals("players")) {
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
        currentPlayer = Games.createPlayer(attributes.getValue("id"));
        game.addPlayer(currentPlayer);
        currentActions = new HashSet<Action>();

    }

    private void handleStartPlayers(Attributes attributes) throws SAXException {
    }

    private void handleStartAction(Attributes attributes) throws SAXException {
        currentActions.add(Games.createAction(attributes.getValue("id")));
    }

    private void handleStartPayoffs(Attributes attributes) throws SAXException {
        playerNames = new HashMap<String,Player>();
        actionNames = new HashMap<String,Map<String,Action>>();
        for(Player p : game.players()) {
            playerNames.put(p.getID(),p);
            actionNames.put(p.getID(),new HashMap<String,Action>());

            for(Action a : game.getActions(p)) {
                actionNames.get(p.getID()).put(a.getID(),a);
            }
        }
    }

    private void handleStartPayoff(Attributes attributes) throws SAXException {
        payoffPlayers = new LinkedList<Player>();
        payoffActions = new LinkedList<Action>();
        payoffValues  = new LinkedList<Number>();
    }

    private void handleStartOutcome(Attributes attributes) throws SAXException {
        payoffPlayers.add(playerNames.get(attributes.getValue("player")));
        payoffActions.add(actionNames.get(attributes.getValue("player")).get(attributes.getValue("action")));
        payoffValues.add(new Double(attributes.getValue("value")));
    }

    private void handleEndNfg() throws SAXException {
    }

    private void handleEndPlayer() throws SAXException {
        game.putActions(currentPlayer,currentActions);
        currentPlayer = null;
        currentActions = null;
    }

    private void handleEndPlayers() throws SAXException {
        game.build();
    }

    private void handleEndAction() throws SAXException {
    }

    private void handleEndPayoff() throws SAXException {
        Player[] players = payoffPlayers.toArray(new Player[0]);
        Action[] actions = payoffActions.toArray(new Action[0]);
        Number[] values = payoffValues.toArray(new Number[0]);
        double[] doubleValues = new double[values.length];

        for(int i = 0; i < values.length; i++)
            doubleValues[i] = values[i].doubleValue();
        Outcome o = Games.createOutcome(players, actions);
        Payoff p = PayoffFactory.createPayoff(players, doubleValues);
        game.putPayoff(o,p);

        payoffActions = null;
        payoffPlayers = null;
        payoffValues  = null;
    }

    private void handleEndPayoffs() throws SAXException {
        playerNames = null;
        actionNames = null;
    }

    private void handleEndOutcome() throws SAXException {

    }

    public MutableStrategicGame<PayoffValue> getGame() {
        return game;
    }
}
