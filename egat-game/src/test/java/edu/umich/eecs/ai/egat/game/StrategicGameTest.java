package edu.umich.eecs.ai.egat.game;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author Patrick Jordan
 */
public class StrategicGameTest {
    private Player player1;
    private Player player2;
    private Player player3;

    private Action c;
    private Action d;

    private Outcome cell11;
    private Outcome cell12;
    private Outcome cell21;
    private Outcome cell22;

    private DefaultStrategicGame game;

    private Profile profile;

    @Before
    public void setUp() {
        player1 = Games.createPlayer("row");
        player2 = Games.createPlayer("col");
        player3 = Games.createPlayer("eve");

        c = Games.createAction("c");
        d = Games.createAction("d");
        
        Set<Action> player1Actions = new HashSet<Action>();
        Set<Action> player2Actions = new HashSet<Action>();

        player1Actions.add(c);
        player2Actions.add(c);
        player1Actions.add(d);
        player2Actions.add(d);

        game = new DefaultStrategicGame("name","description");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.putActions(player1,player1Actions);
        game.putActions(player2,player2Actions);

        game.build();
        game.putPayoff(cell11 = Games.createOutcome(new Player[]{player1,player2},new Action[]{c,c}),PayoffFactory.createPayoff(new Player[]{player1,player2},new double[]{3.0,3.0}));
        game.putPayoff(cell12 = Games.createOutcome(new Player[]{player1,player2},new Action[]{c,d}),PayoffFactory.createPayoff(new Player[]{player1,player2},new double[]{0.0,5.0}));
        game.putPayoff(cell21 = Games.createOutcome(new Player[]{player1,player2},new Action[]{d,c}),PayoffFactory.createPayoff(new Player[]{player1,player2},new double[]{5.0,0.0}));
        game.putPayoff(cell22 = Games.createOutcome(new Player[]{player1,player2},new Action[]{d,d}),PayoffFactory.createPayoff(new Player[]{player1,player2},new double[]{1.0,1.0}));

        Strategy strategy = Games.createStrategy(new Action[]{c,d},new Number[]{new Double(0.5),new Double(0.5)});
        profile = Games.createProfile(new Player[]{player1,player2},new Strategy[]{strategy,strategy});

    }

    @Test
    public void testPlayerActions() {
        Set<Action> playerActions = new HashSet<Action>();

        playerActions.add(c);
        playerActions.add(d);

        assertEquals(game.getActions(player1),playerActions);

        assertEquals(game.getActions(player2),playerActions);

        assertNull(game.getActions(player3));

    }

    @Test
    public void testPutPlayerActions() {
        Set<Action> playerActions = new HashSet<Action>();

        playerActions.add(c);
        playerActions.add(d);

        assertNull(game.getActions(player3));
    }

    @Test
    public void testPayoffs() {
        Payoff payoff11 = game.payoff(cell11);
        Payoff payoff12 = game.payoff(cell12);
        Payoff payoff21 = game.payoff(cell21);
        Payoff payoff22 = game.payoff(cell22);

        assertEquals(payoff11.getPayoff(player1).getValue(),3.0,0.1);
        assertEquals(payoff11.getPayoff(player2).getValue(),3.0,0.1);

        assertEquals(payoff12.getPayoff(player1).getValue(),0.0,0.1);
        assertEquals(payoff12.getPayoff(player2).getValue(),5.0,0.1);

        assertEquals(payoff21.getPayoff(player1).getValue(),5.0,0.1);
        assertEquals(payoff21.getPayoff(player2).getValue(),0.0,0.1);

        assertEquals(payoff22.getPayoff(player1).getValue(),1.0,0.1);
        assertEquals(payoff22.getPayoff(player2).getValue(),1.0,0.1);
    }

    @Test
    public void testTraversal() {
        Set<Outcome> set = new HashSet<Outcome>();

        for (Outcome outcome : Games.traversal(game)) {
            set.add(outcome);
        }

        assertEquals(4,set.size());
    }

    @Test
    public void testMinPayoff() {
        assertEquals(0.0,PayoffFactory.minPayoff(game).doubleValue(),0.1);
    }

    @Test
    public void testMaxPayoff() {
        assertEquals(5.0,PayoffFactory.maxPayoff(game).doubleValue(),0.1);
    }

    @Test
    public void testMixedPayoff() {
        Payoff payoff = game.payoff(profile);
        
        assertEquals(9.0/4,payoff.getPayoff(player1).getValue(),0.1);
        assertEquals(9.0/4,payoff.getPayoff(player2).getValue(),0.1);
    }

}
