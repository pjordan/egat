/**
 * @author Patrick Jordan
 */
package edu.umich.eecs.ai.egat.gamexml;

import edu.umich.eecs.ai.egat.game.*;
import edu.umich.eecs.ai.egat.game.Action;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.io.InputStream;
import java.io.IOException;

public class GameFactory {
    private GameFactory() {
    }


    public static StrategicGame<PayoffValue> parseStrategicGame(InputStream inputStream) throws IOException, SAXException, ParserConfigurationException {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        StrategicGameHandler handler = new StrategicGameHandler();
        parser.parse(inputStream,handler);

        return handler.getGame();
    }

    private static Set<Action> createStrategicPlayerActions(Element player) throws JDOMException {
        final Set<Action> actions = new HashSet<Action>();
        final List list = XPath.newInstance("self::*/action").selectNodes(player);
        for (Object o : list) {
            if (o instanceof Element) {
                actions.add(Games.createAction(((Element) o).getAttributeValue("id")));
            }
        }

        return actions;
    }

    private static void createStrategicPayoffs(DefaultStrategicGame<PayoffValue> game, Element root) throws JDOMException {
        Map<String, Player> names = new HashMap<String, Player>();
        Map<Player, Map<String, Action>> actionNames = new HashMap<Player, Map<String, Action>>();


        for (Player p : game.players()) {
            names.put(p.getID(), p);

            actionNames.put(p, new HashMap<String, Action>());

            for (Action a : game.getActions(p)) {
                actionNames.get(p).put(a.getID(), a);
            }
        }

        loop:
        for (Element element : extractStrategicPayoffs(root)) {
            List<Object[]> list = extractStrategicPayoffEntries(element);
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

                    //Find the value
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

            game.putPayoff(o, p);
        }
    }

    private static List<Element> extractStrategicPayoffs(Element root) throws JDOMException {
        List<Element> payoffs = new LinkedList<Element>();

        for (Object object : XPath.newInstance("/nfg/payoffs/payoff").selectNodes(root)) {
            payoffs.add((Element) object);
        }
        return payoffs;
    }

    private static List<Object[]> extractStrategicPayoffEntries(Element payoff) {
        List<Object[]> entries = new LinkedList<Object[]>();

        for (Element outcome : (List<Element>) payoff.getChildren()) {
            String player = outcome.getAttributeValue("player");
            String action = outcome.getAttributeValue("action");
            Number number = new Double(outcome.getAttributeValue("value"));
            entries.add(new Object[]{player, action, number});
        }

        return entries;
    }

    public static SymmetricGame<PayoffValue> parseSymmetricGame(InputStream inputStream) throws SAXException, ParserConfigurationException, IOException {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        SymmetricGameHandler handler = new SymmetricGameHandler();
        parser.parse(inputStream,handler);

        return handler.getGame();


        /*SAXBuilder builder = new SAXBuilder();
        builder.setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException saxParseException) throws SAXException {
                throw saxParseException;
            }

            public void error(SAXParseException saxParseException) throws SAXException {
                throw saxParseException;
            }

            public void fatalError(SAXParseException saxParseException) throws SAXException {
                throw saxParseException;
            }
        });

        Document doc = builder.build(inputStream);


        return parseSymmetricGame(doc);*/
    }

    public static SymmetricGame<PayoffValue> parseSymmetricGame(Document doc) throws JDOMException {
        DefaultSymmetricGame<PayoffValue> game = new DefaultSymmetricGame<PayoffValue>();
        Element root = doc.getRootElement();

        final List playerList = XPath.newInstance("/nfg/players/player").selectNodes(root);
        for (Object o : playerList) {
            if (o instanceof Element) {
                game.addPlayer(Games.createPlayer(((Element) o).getAttributeValue("id")));
            }
        }

        final List actionList = XPath.newInstance("/nfg/actions/action").selectNodes(root);
        for (Object o : actionList) {
            if (o instanceof Element) {
                game.addAction(Games.createAction(((Element) o).getAttributeValue("id")));
            }
        }

        createSymmetricPayoffs(game, root);

        game.setName(root.getAttributeValue("name"));
        game.setDescription(root.getAttributeValue("description"));

        return game;
    }

    public static SymmetricGame<EmpiricalPayoffValue> parseEmpiricalSymmetricGame(InputStream inputStream) throws JDOMException, IOException {
            SAXBuilder builder = new SAXBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException saxParseException) throws SAXException {
                    throw saxParseException;
                }

                public void error(SAXParseException saxParseException) throws SAXException {
                    throw saxParseException;
                }

                public void fatalError(SAXParseException saxParseException) throws SAXException {
                    throw saxParseException;
                }
            });

            Document doc = builder.build(inputStream);


            return parseEmpiricalSymmetricGame(doc);
        }

    public static SymmetricGame<EmpiricalPayoffValue> parseEmpiricalSymmetricGame(Document doc) throws JDOMException {
            DefaultSymmetricGame<EmpiricalPayoffValue> game = new DefaultSymmetricGame<EmpiricalPayoffValue>();
            Element root = doc.getRootElement();

            final List playerList = XPath.newInstance("/nfg/players/player").selectNodes(root);
            for (Object o : playerList) {
                if (o instanceof Element) {
                    game.addPlayer(Games.createPlayer(((Element) o).getAttributeValue("id")));
                }
            }

            final List actionList = XPath.newInstance("/nfg/actions/action").selectNodes(root);
            for (Object o : actionList) {
                if (o instanceof Element) {
                    game.addAction(Games.createAction(((Element) o).getAttributeValue("id")));
                }
            }

            createEmpiricalSymmetricPayoffs(game, root);

            game.setName(root.getAttributeValue("name"));
            game.setDescription(root.getAttributeValue("description"));

            return game;
        }

    private static void createSymmetricPayoffs(DefaultSymmetricGame<PayoffValue> game, Element root) throws JDOMException {
        Player[] players = game.players().toArray(new Player[0]);
        Map<String,Action> actions = new HashMap<String,Action>();

        for(Action a : game.getActions()) {
            actions.put(a.getID(),a);
        }

        for (Element element : extractSymmetricPayoffs(root)) {
            Outcome key = extractSymmetricPayoffOutcome(element,players,actions);
            Map<Action, PayoffValue> value = extractSymmetricPayoffMap(element,actions);
            game.putPayoff(key, value);
        }
    }

    private static void createEmpiricalSymmetricPayoffs(DefaultSymmetricGame<EmpiricalPayoffValue> game, Element root) throws JDOMException {
        Player[] players = game.players().toArray(new Player[0]);
        Map<String,Action> actions = new HashMap<String,Action>();

        for(Action a : game.getActions()) {
            actions.put(a.getID(),a);
        }

        for (Element element : extractSymmetricPayoffs(root)) {
            Outcome key = extractSymmetricPayoffOutcome(element,players,actions);
            Map<Action, EmpiricalPayoffValue> value = extractEmpiricalSymmetricPayoffMap(element,actions);
            game.putPayoff(key, value);
        }
    }

    private static List<Element> extractSymmetricPayoffs(Element root) throws JDOMException {
        List<Element> payoffs = new LinkedList<Element>();


        for (Object object : XPath.newInstance("/nfg/payoffs/payoff").selectNodes(root)) {
            payoffs.add((Element) object);
        }


        return payoffs;
    }

    private static Map<Action, PayoffValue> extractSymmetricPayoffMap(Element payoff, Map<String, Action> actions) {
        Map<Action, PayoffValue> payoffMap = new HashMap<Action, PayoffValue>();

        for (Element outcome : (List<Element>) payoff.getChildren()) {
            Action action = actions.get(outcome.getAttributeValue("action"));
            PayoffValue value = PayoffFactory.createPayoffValue(new Double(outcome.getAttributeValue("value")));
            payoffMap.put(action, value);
        }

        return payoffMap;
    }

    private static Map<Action, EmpiricalPayoffValue> extractEmpiricalSymmetricPayoffMap(Element payoff, Map<String, Action> actions) {
            Map<Action, EmpiricalPayoffValue> payoffMap = new HashMap<Action, EmpiricalPayoffValue>();

            for (Element outcome : (List<Element>) payoff.getChildren()) {
                Action action = actions.get(outcome.getAttributeValue("action"));
                double mean = Double.parseDouble(outcome.getAttributeValue("mean"));
                double std = Double.parseDouble(outcome.getAttributeValue("std"));
                int count = Integer.parseInt(outcome.getAttributeValue("samples"));
                EmpiricalPayoffValue value = PayoffFactory.createEmpiricalPayoffValue(mean,std,count);
                payoffMap.put(action, value);
            }

            return payoffMap;
        }

    private static Outcome extractSymmetricPayoffOutcome(Element payoff, Player[] players, Map<String, Action> actions) {
        Action[] array = new Action[players.length];

        int index = 0;
        for (Element outcome : (List<Element>) payoff.getChildren()) {
            String actionId = outcome.getAttributeValue("action");
            Action action = actions.get(actionId);

            int length = Integer.parseInt(outcome.getAttributeValue("count"));
            for (int i = 0; i < length && index < array.length; i++)
                array[index++] = action;
        }

        return Games.createSymmetricOutcome(players, array);
    }


}
