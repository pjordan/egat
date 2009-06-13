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
