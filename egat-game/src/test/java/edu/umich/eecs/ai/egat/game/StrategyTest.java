package edu.umich.eecs.ai.egat.game;

import junit.framework.TestCase;

/**
 * @author Patrick Jordan
 */
public class StrategyTest extends TestCase {
    private Action action1;
    private Action action2;
    private Action action3;
    private Strategy strategy;

    protected void setUp() throws Exception {
        action1 = Games.createAction("one");
        action2 = Games.createAction("two");
        action3 = Games.createAction("three");
        strategy = Games.createStrategy(new Action[]{action1,action2},new Number[]{0.25,0.75});
    }

    public void testEquality() {
        assertEquals(0.25,strategy.getProbability(action1).doubleValue(),0.01);
        assertEquals(0.75,strategy.getProbability(action2).doubleValue(),0.01);
    }

    public void testInvalidAction() {
        assertEquals(0.00,strategy.getProbability(action3).doubleValue(),0.01);
    }
}
