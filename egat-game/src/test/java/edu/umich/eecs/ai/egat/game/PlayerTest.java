/*
 * PlayerTest.java
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
public class PlayerTest {
    private Player player1;
    private Player player2;
    private Player player3;

    @Before
    public void setUp() {
        player1 = Games.createPlayer("one");
        player2 = Games.createPlayer("two");
        player3 = Games.createPlayer("one");
    }

    @Test
    public void testEquality() {
        assertTrue(player1.equals(player3));
    }

    @Test
    public void testEqualityHashCode() {
        assertEquals(player1.hashCode(), player3.hashCode());
    }

    @Test
    public void testInEquality() {
        assertFalse(player1.equals(player2));
    }

    @Test
    public void testNullEquality() {
        assertFalse(player1.equals(null));
    }
}
