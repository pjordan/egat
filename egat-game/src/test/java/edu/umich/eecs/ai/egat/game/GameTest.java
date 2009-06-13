package edu.umich.eecs.ai.egat.game;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * @author Patrick Jordan
 */
public class GameTest {
    private Player player1;
    private Player player2;
    private Player player3;
    private MutableMultiAgentSystem game;

    @Before
    public void setUp() {
        player1 = Games.createPlayer("row");
        player2 = Games.createPlayer("col");
        player3 = Games.createPlayer("row");

        game = new DefaultMultiAgentSystem("name","description");
    }

    @Test
    public void testNameEquility() {
        assertEquals(game.getName(),"name");
    }

    @Test
    public void testDescriptionEquility() {
        assertEquals(game.getDescription(),"description");
    }

    @Test
    public void testPlayersNullity() {
        assertNotNull(game.players());
    }

    @Test
    public void testAddPlayers() {
        MutableMultiAgentSystem emptyGame = new DefaultMultiAgentSystem("name","description");

        assertEquals(emptyGame.players().size(),0);

        emptyGame.addPlayer(player1);

        assertEquals(emptyGame.players().size(),1);

        emptyGame.addPlayer(player2);

        assertEquals(emptyGame.players().size(),2);

        emptyGame.addPlayer(player3);

        assertEquals(emptyGame.players().size(),2);
    }

    @Test
    public void testRemovePlayers() {
        MutableMultiAgentSystem emptyGame = new DefaultMultiAgentSystem("name","description");

        assertEquals(emptyGame.players().size(),0);

        emptyGame.addPlayer(player1);

        assertEquals(emptyGame.players().size(),1);

        emptyGame.removePlayer(player1);

        assertEquals(emptyGame.players().size(),0);        
    }
}
