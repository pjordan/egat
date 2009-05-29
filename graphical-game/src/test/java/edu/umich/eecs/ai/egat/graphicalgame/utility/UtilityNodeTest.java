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
