package edu.umich.eecs.ai.egat.gamexml;

import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import edu.umich.eecs.ai.egat.game.*;

/**
 * @author Patrick Jordan
 */
public class TestGameXMLTest {
    public static void main(String[] args) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build("/Users/pjordan/downloads/game-22.xml");
        SymmetricGame game = (SymmetricGame) XMLNfgFactory.buildSymmetric(doc);

        Action[] actions = new Action[] {Games.createAction("MR05"),Games.createAction("TT05"),Games.createAction("PH05"),Games.createAction("PH06")};
        Player[] players = game.players().toArray(new Player[0]);

        // Normalize to a uniform distribution
        double[] distribution = new double[] {0.257,0.154,0.589,0.0};

        // Initialize base strategy
        Strategy strategy = TestGameXMLTest.buildStrategy(actions, distribution);


        // Create mixed profile for the expected payoff calculation
        Strategy[] playerStrategies = new Strategy[]{strategy,strategy,strategy};

        // Initialize deviation strategy
        Strategy deviation = TestGameXMLTest.buildStrategy(actions, new double[]{0,0,0,1.0});


        Payoff basePayoff = game.payoff(Games.createProfile(players,playerStrategies));

        playerStrategies[0] = deviation;
        Payoff deviationPayoff = game.payoff(Games.createProfile(players,playerStrategies));


        System.out.print(deviationPayoff.getPayoff(players[0]).getValue());
        System.out.print("<->");
        System.out.print(basePayoff.getPayoff(players[0]).getValue());
        System.out.println();

    }

    private static Strategy buildStrategy(Action[] actions, double[] distribution) {
        Number[] number = new Number[distribution.length];
        for (int i = 0; i < number.length; i++) {
            number[i] = distribution[i];
        }

        return Games.createStrategy(actions, number);
    }
}
