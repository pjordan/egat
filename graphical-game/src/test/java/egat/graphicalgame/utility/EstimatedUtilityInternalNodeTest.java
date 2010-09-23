/*
 * EstimatedUtilityInternalNodeTest.java
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
import egat.game.Player;
import egat.game.Games;
import egat.game.Action;
import egat.game.Outcome;

/**
 * @author Patrick Jordan
 */
public class EstimatedUtilityInternalNodeTest {
    @Test
    public void testAll() {
        Player alice = Games.createPlayer("alice");
        Action a = Games.createAction("a");
        Action b = Games.createAction("b");

        EstimatedUtilityInternalNode node = new EstimatedUtilityInternalNode(alice);

        assertNotNull(node);

        EstimatedUtilityLeaf aLeaf = new EstimatedUtilityLeaf();
        EstimatedUtilityLeaf bLeaf = new EstimatedUtilityLeaf();

        node.setEstimatedUtilityChild(a,aLeaf);
        node.setEstimatedUtilityChild(b,bLeaf);

        Outcome aOutcome = Games.createOutcome(new Player[] {alice}, new Action[]{a});
        Outcome bOutcome = Games.createOutcome(new Player[] {alice}, new Action[]{b});

        
        node.addSample(aOutcome,1.0);
        node.addSample(aOutcome,1.0);
        node.addSample(bOutcome,2.0);

        assertEquals(node.utility(aOutcome),1.0,0.0);
        assertEquals(node.utility(bOutcome),2.0,0.0);

        assertEquals(node.sampleCount(aOutcome),2,0);
        assertEquals(node.sampleCount(bOutcome),1,0);

        assertEquals(node.meanUtility(aOutcome),1.0,0.0);
        assertEquals(node.meanUtility(bOutcome),2.0,0.0);

        assertEquals(node.meanUtility(aOutcome),1.0,0.0);
        assertEquals(node.meanUtility(bOutcome),2.0,0.0);

    }

    @Test(expected=NullPointerException.class)
    public void testNullPlayerConstructor() {
        new EstimatedUtilityInternalNode(null);
    }
}
