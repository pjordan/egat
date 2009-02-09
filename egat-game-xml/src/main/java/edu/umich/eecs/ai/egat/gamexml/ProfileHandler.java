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
