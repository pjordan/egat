package edu.umich.eecs.ai.egat.game;

import junit.framework.TestCase;

/**
 * @author Patrick Jordan
 */
public class PlayerTest extends TestCase {
    private Player player1;
    private Player player2;
    private Player player3;

    protected void setUp() throws Exception {
        player1 = Games.createPlayer("one");
        player2 = Games.createPlayer("two");
        player3 = Games.createPlayer("one");
    }

    public void testEquality() {
        assertTrue(player1.equals(player3));
    }

    public void testEqualityHashCode() {
        assertEquals(player1.hashCode(), player3.hashCode());
    }

    public void testInEquality() {
        assertFalse(player1.equals(player2));
    }

    public void testNullEquality() {
        assertFalse(player1.equals(null));
    }
}
