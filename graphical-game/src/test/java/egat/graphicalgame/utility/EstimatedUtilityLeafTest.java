/*
 * EstimatedUtilityLeafTest.java
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
package egat.graphicalgame.utility;

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
