/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.gamexml;

import edu.umich.eecs.ai.egat.game.*;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import java.util.*;

/**
 * @author Patrick Jordan
 */
final class SymmetricGameImpl extends DefaultSymmetricGame<PayoffValue> {
    public SymmetricGameImpl(final Element root) {

        createPlayers(root);
        createActions(root);
        createPayoffs(root);

        setName(root.getAttributeValue("name"));
        setDescription(root.getAttributeValue("description"));
    }

    private void createActions(Element root) {
        try {
            final List list = XPath.newInstance("/nfg/actions/action").selectNodes(root);
            for (Object o : list) {
                if (o instanceof Element) {
                    addAction(Games.createAction(((Element) o).getAttributeValue("id")));
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        }

    }

    private void createPlayers(Element root) {
        try {
            final List list = XPath.newInstance("/nfg/players/player").selectNodes(root);
            for (Object o : list) {
                if (o instanceof Element) {
                    addPlayer(Games.createPlayer(((Element) o).getAttributeValue("id")));
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    private void createPayoffs(Element root) {
        Player[] players = players().toArray(new Player[0]);
        Map<String,Action> actions = new HashMap<String,Action>();

        for(Action a : getActions()) {
            actions.put(a.getID(),a);
        }

        for (Element element : extractPayoffs(root)) {
            Outcome key = extractPayoffOutcome(element,players,actions);
            Map<Action, PayoffValue> value = extractPayoffMap(element,actions);
            putPayoff(key, value);
        }
    }

    private static List<Element> extractPayoffs(Element root) {
        List<Element> payoffs = new LinkedList<Element>();

        try {
            for (Object object : XPath.newInstance("/nfg/payoffs/payoff").selectNodes(root)) {
                payoffs.add((Element) object);
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        return payoffs;
    }

    private static Map<Action, PayoffValue> extractPayoffMap(Element payoff, Map<String,Action> actions) {
        Map<Action, PayoffValue> payoffMap = new HashMap<Action, PayoffValue>();

        for (Element outcome : (List<Element>) payoff.getChildren()) {
            Action action = actions.get(outcome.getAttributeValue("action"));
            PayoffValue value = PayoffFactory.createPayoffValue(new Double(outcome.getAttributeValue("value")));
            payoffMap.put(action, value);
        }

        return payoffMap;
    }

    private Outcome extractPayoffOutcome(Element payoff, Player[] players, Map<String,Action> actions) {
        Action[] array = new Action[players.length];

        int index = 0;
        for (Element outcome : (List<Element>) payoff.getChildren()) {
            String actionId = outcome.getAttributeValue("action");
            Action action = actions.get(actionId);

            int length = Integer.parseInt(outcome.getAttributeValue("count"));
            for (int i = 0; i < length && index<array.length; i++)
                array[index++] = action;
        }

        return Games.createSymmetricOutcome(players,array);
    }
}
