package edu.umich.eecs.ai.egat.game;

import junit.framework.TestCase;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class SymmetricGameTest extends TestCase {
    private Player player1;
    private Player player2;
    private Player player3;

    private Action c;
    private Action d;

    private Outcome cell11;
    private Outcome cell12;
    private Outcome cell21;
    private Outcome cell22;

    private DefaultSymmetricGame game;

    private Profile profile;

    protected void setUp() throws Exception {
        player1 = Games.createPlayer("row");
        player2 = Games.createPlayer("col");
        player3 = Games.createPlayer("eve");

        c = Games.createAction("c");
        d = Games.createAction("d");

        Set<Action> actions = new HashSet<Action>();

        actions.add(c);
        actions.add(d);

        game = new DefaultSymmetricGame("name", "description");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addAllActions(actions);


        Map<Action, PayoffValue> payoff11 = new HashMap<Action, PayoffValue>();
        payoff11.put(c, PayoffFactory.createPayoffValue(3.0));
        Map<Action, PayoffValue> payoff12 = new HashMap<Action, PayoffValue>();
        payoff12.put(c, PayoffFactory.createPayoffValue(0.0));
        payoff12.put(d, PayoffFactory.createPayoffValue(5.0));
        Map<Action, PayoffValue> payoff22 = new HashMap<Action, PayoffValue>();
        payoff22.put(d, PayoffFactory.createPayoffValue(1.0));

        game.putPayoff(cell11 = Games.createOutcome(new Player[]{player1, player2}, new Action[]{c, c}), payoff11);
        game.putPayoff(cell12 = Games.createOutcome(new Player[]{player1, player2}, new Action[]{c, d}), payoff12);
        cell21 = Games.createOutcome(new Player[]{player1, player2}, new Action[]{d, c});
        game.putPayoff(cell22 = Games.createOutcome(new Player[]{player1, player2}, new Action[]{d, d}), payoff22);

        Strategy strategy = Games.createStrategy(new Action[]{c, d}, new Number[]{new Double(0.5), new Double(0.5)});
        profile = Games.createProfile(new Player[]{player1, player2}, new Strategy[]{strategy, strategy});

    }

    public void testPlayerActions() {
        Set<Action> playerActions = new HashSet<Action>();

        playerActions.add(c);
        playerActions.add(d);

        assertEquals(game.getActions(player1), playerActions);
        assertEquals(game.getActions(player2), playerActions);
        assertEquals(game.getActions(), playerActions);
    }

    public void testPayoffs() {
        Payoff payoff11 = game.payoff(cell11);
        Payoff payoff12 = game.payoff(cell12);
        Payoff payoff21 = game.payoff(cell21);
        Payoff payoff22 = game.payoff(cell22);

        assertEquals(payoff11.getPayoff(player1).getValue(), 3.0, 0.1);
        assertEquals(payoff11.getPayoff(player2).getValue(), 3.0, 0.1);

        assertEquals(payoff12.getPayoff(player1).getValue(), 0.0, 0.1);
        assertEquals(payoff12.getPayoff(player2).getValue(), 5.0, 0.1);

        assertEquals(payoff21.getPayoff(player1).getValue(), 5.0, 0.1);
        assertEquals(payoff21.getPayoff(player2).getValue(), 0.0, 0.1);

        assertEquals(payoff22.getPayoff(player1).getValue(), 1.0, 0.1);
        assertEquals(payoff22.getPayoff(player2).getValue(), 1.0, 0.1);
    }

    public void testTraversal() {
        Set<Outcome> set = new HashSet<Outcome>();

        for (Outcome outcome : Games.symmetricTraversal(game)) {
            set.add(outcome);
        }

        assertEquals(3, set.size());
    }

    public void testMinPayoff() {
        assertEquals(0.0, PayoffFactory.minPayoff(game).doubleValue(), 0.1);
    }


    public void testNonexistentPayoff() {
        try {
            game.payoff(Games.createOutcome(new Player[]{player1, player2},
                    new Action[]{Games.createAction("a"), Games.createAction("b")}));
            assertTrue(false);
        } catch (NonexistentPayoffException e) {
            assertTrue(true);
        }
    }

    public void testMaxPayoff() {
        assertEquals(5.0, PayoffFactory.maxPayoff(game).doubleValue(), 0.1);
    }

    public void testMixedPayoff() {
        Payoff payoff = game.payoff(profile);

        assertEquals(9.0 / 4, payoff.getPayoff(player1).getValue(), 0.1);
        assertEquals(9.0 / 4, payoff.getPayoff(player2).getValue(), 0.1);
    }

}
