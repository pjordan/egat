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
final class GameImpl extends DefaultStrategicGame<PayoffValue> {
    public GameImpl(final Element root) {

        build(root);
        createPayoffs(root);

        setName(root.getAttributeValue("name"));
        setDescription(root.getAttributeValue("description"));
    }


    private static Set<Action> createPlayerActions(Element player) {
        final Set<Action> actions = new HashSet<Action>();

        try {
            final List list = XPath.newInstance("self::*/action").selectNodes(player);
            for (Object o : list) {
                if (o instanceof Element) {
                    actions.add(Games.createAction(((Element) o).getAttributeValue("id")));
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        return actions;
    }

    private void build(Element root) {
        try {
            final List list = XPath.newInstance("/nfg/players/player").selectNodes(root);
            for (Object o : list) {
                if (o instanceof Element) {
                    Player player = new PlayerImpl(((Element) o).getAttributeValue("id"));
                    addPlayer(player);
                    putActions(player, createPlayerActions((Element) o));
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    private void createPayoffs(Element root) {
        Map<String, Player> names = new HashMap<String, Player>();
        Map<Player, Map<String, Action>> actionNames = new HashMap<Player, Map<String, Action>>();


        for (Player p : players()) {
            names.put(p.getID(), p);

            actionNames.put(p, new HashMap<String, Action>());

            for (Action a : getActions(p)) {
                actionNames.get(p).put(a.getID(), a);
            }
        }

        loop:
        for (Element element : extractPayoffs(root)) {
            List<Object[]> list = extractPayoffEntries(element);
            Player[] playerArray = new Player[list.size()];
            Action[] actionArray = new Action[playerArray.length];
            double[] numberArray = new double[playerArray.length];

            int i = 0;
            for (Object[] array : list) {
                try {
                    //Find the player
                    Player player = names.get(array[0]);

                    //Find the action
                    Action action = actionNames.get(player).get(array[1]);

                    //Finf the value
                    Number number = (Number) array[2];

                    playerArray[i] = player;
                    actionArray[i] = action;
                    numberArray[i] = number.doubleValue();
                } catch (NumberFormatException e) {
                    break loop;
                } catch (NullPointerException e) {
                    break loop;
                }

                i++;
            }

            Outcome o = Games.createOutcome(playerArray, actionArray);
            Payoff p = PayoffFactory.createPayoff(playerArray, numberArray);

            putPayoff(o, p);
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

    private static List<Object[]> extractPayoffEntries(Element payoff) {
        List<Object[]> entries = new LinkedList<Object[]>();

        for (Element outcome : (List<Element>) payoff.getChildren()) {
            String player = outcome.getAttributeValue("player");
            String action = outcome.getAttributeValue("action");
            Number number = new Double(outcome.getAttributeValue("value"));
            entries.add(new Object[]{player, action, number});
        }

        return entries;
    }
}
