package edu.umich.eecs.ai.egat.game;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Patrick Jordan
 */
public class AbstractIdentifiableTest {
    @Test
    public void testAction() {
        AbstractIdentifiable identifiable = new AbstractIdentifiable("a") {
            protected boolean checkClass(Object o) {
                return false;
            }
        };

        assertNotNull(identifiable);
    }    
}
