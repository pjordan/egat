package edu.umich.eecs.ai.egat.graphicalgame.utility;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Patrick Jordan
 */
public class EstimatedUtilityLeafTest {

    @Test
    public void testAll() {
        EstimatedUtilityLeaf leaf = new EstimatedUtilityLeaf();

        assertNotNull(leaf);
        assertEquals(leaf.sampleCount(null),0,0);
        assertTrue(Double.isNaN(leaf.meanUtility(null)));
        assertEquals(leaf.utility(null),0.0,0.0);

        leaf.addSample(null,1.0);
        assertEquals(leaf.sampleCount(null),1,0);
        assertEquals(leaf.meanUtility(null),1.0,0.0);
        assertEquals(leaf.utility(null),1.0,0.0);

        leaf.addSample(null,1.0);
        assertEquals(leaf.sampleCount(null),2,0);
        assertEquals(leaf.meanUtility(null),1.0,0.0);
        assertEquals(leaf.utility(null),1.0,0.0);
    }
}
