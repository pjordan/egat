package edu.umich.eecs.ai.egat.game;

import junit.framework.TestCase;

/**
 * @author Patrick Jordan
 */
public class GameTest extends TestCase {
    private Player player1;
    private Player player2;
    private Player player3;
    private DefaultGame game;

    protected void setUp() throws Exception {
        player1 = Games.createPlayer("row");
        player2 = Games.createPlayer("col");
        player3 = Games.createPlayer("row");

        game = new DefaultGame("name","description");
    }

    public void testNameEquility() {
        assertEquals(game.getName(),"name");
    }

    public void testDescriptionEquility() {
        assertEquals(game.getDescription(),"description");
    }

    public void testPlayersNullity() {
        assertNotNull(game.players());
    }

    public void testAddPlayers() {
        DefaultGame emptyGame = new DefaultGame("name","description");

        assertEquals(emptyGame.players().size(),0);

        emptyGame.addPlayer(player1);

        assertEquals(emptyGame.players().size(),1);

        emptyGame.addPlayer(player2);

        assertEquals(emptyGame.players().size(),2);

        emptyGame.addPlayer(player3);

        assertEquals(emptyGame.players().size(),2);
    }

    public void testRemovePlayers() {
        DefaultGame emptyGame = new DefaultGame("name","description");

        assertEquals(emptyGame.players().size(),0);

        emptyGame.addPlayer(player1);

        assertEquals(emptyGame.players().size(),1);

        emptyGame.removePlayer(player1);

        assertEquals(emptyGame.players().size(),0);        
    }
}
