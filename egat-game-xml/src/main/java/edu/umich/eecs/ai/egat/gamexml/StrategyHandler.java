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

    protected class StrategyEntry {
        private Action action;
        private double probability;

        public StrategyEntry(Action action, double probability) {
            this.action = action;
            this.probability = probability;
        }

        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            this.action = action;
        }

        public double getProbability() {
            return probability;
        }

        public void setProbability(double probability) {
            this.probability = probability;
        }
    }
}
