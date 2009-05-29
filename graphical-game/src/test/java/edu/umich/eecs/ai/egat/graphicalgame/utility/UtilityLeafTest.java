package edu.umich.eecs.ai.egat.graphicalgame.utility;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Patrick Jordan
 */
public class UtilityLeafTest {

    @Test
    public void testAll() {
        UtilityLeaf leaf = new UtilityLeaf(1.0);
        assertNotNull(leaf);

        assertEquals(leaf.utility(null),1.0,0.0);
    }
}
