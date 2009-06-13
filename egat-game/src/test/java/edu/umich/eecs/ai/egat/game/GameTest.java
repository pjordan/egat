/*
 * GameTest.java
 *
 * Copyright (C) 2006-2009 Patrick R. Jordan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
