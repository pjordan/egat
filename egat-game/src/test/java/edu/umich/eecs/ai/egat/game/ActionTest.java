package edu.umich.eecs.ai.egat.game;

import junit.framework.TestCase;

/**
 * @author Patrick Jordan
 */
public class ActionTest extends TestCase {
    private Action action1;
    private Action action2;
    private Action action3;

    protected void setUp() throws Exception {
        action1 = Games.createAction("one");
        action2 = Games.createAction("two");
        action3 = Games.createAction("one");
    }

    public void testEquality() {
        assertTrue(action1.equals(action3));
    }

    public void testEqualityHashCode() {
        assertEquals(action1.hashCode(),action3.hashCode());
    }

    public void testInEquality() {
        assertFalse(action1.equals(action2));
    }
    
    public void testNullEquality() {
        assertFalse(action1.equals(null));
    }
}
