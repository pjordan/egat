package edu.umich.eecs.ai.egat.game;


import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author Patrick Jordan
 */
public class ActionTest {
    private Action action1;
    private Action action2;
    private Action action3;

    @Before
    public void setUp() {
        action1 = Games.createAction("one");
        action2 = Games.createAction("two");
        action3 = Games.createAction("one");
    }

    @Test
    public void testEquality() {
        assertTrue(action1.equals(action3));
    }

    @Test
    public void testEqualityHashCode() {
        assertEquals(action1.hashCode(),action3.hashCode());
    }

    @Test
    public void testInEquality() {
        assertFalse(action1.equals(action2));
    }

    @Test
    public void testNullEquality() {
        assertFalse(action1.equals(null));
    }
}
