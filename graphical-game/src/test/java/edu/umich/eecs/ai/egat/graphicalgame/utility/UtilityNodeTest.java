/*
 * UtilityNodeTest.java
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
package edu.umich.eecs.ai.egat.graphicalgame.utility;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Games;
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.Outcome;

/**
 * @author Patrick Jordan
 */
public class UtilityNodeTest {
    @Test
    public void testAll() {
        Player alice = Games.createPlayer("alice");
        Action a = Games.createAction("a");
        Action b = Games.createAction("b");

        UtilityInternalNode node = new UtilityInternalNode(alice);

        assertNotNull(node);
        
        UtilityLeaf aLeaf = new UtilityLeaf(1.0);
        UtilityLeaf bLeaf = new UtilityLeaf(2.0);

        Outcome aOutcome = Games.createOutcome(new Player[] {alice}, new Action[]{a});
        Outcome bOutcome = Games.createOutcome(new Player[] {alice}, new Action[]{b});

        node.setUtilityChild(a,aLeaf);
        node.setUtilityChild(b,bLeaf);

        assertEquals(node.utility(aOutcome),1.0,0.0);
        assertEquals(node.utility(bOutcome),2.0,0.0);
    }

    @Test(expected=NullPointerException.class)
    public void testNullPlayerConstructor() {
        new UtilityInternalNode(null);
    }
}
