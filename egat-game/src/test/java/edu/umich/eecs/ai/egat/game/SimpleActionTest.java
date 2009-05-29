package edu.umich.eecs.ai.egat.game;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * @author Patrick Jordan
 */
public class SimpleActionTest {

    @Test
    public void testAction() {
        SimpleAction aAction = new SimpleAction("a");
        SimpleAction bAction = new SimpleAction("b");

        assertNotNull(aAction);
        assertNotNull(bAction);

        assertEquals(aAction, new SimpleAction("a"));
        assertEquals(aAction, aAction);
        assertEquals(bAction, new SimpleAction("b"));
        assertFalse(aAction.equals(bAction));
        assertFalse(aAction.equals(null));

        assertEquals(aAction.compareTo(bAction),"a".compareTo("b"));
        assertEquals(aAction.hashCode(),"a".hashCode());

        assertEquals(aAction.getID(),"a");
    }

    @Test(expected = NullPointerException.class)
    public void testNullIdAction() {
        new SimpleAction(null);        
    }
}
