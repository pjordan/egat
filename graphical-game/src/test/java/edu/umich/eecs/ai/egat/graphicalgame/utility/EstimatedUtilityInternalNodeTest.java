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
