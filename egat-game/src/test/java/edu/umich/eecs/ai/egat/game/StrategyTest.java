/*
 * StrategyTest.java
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
package edu.umich.eecs.ai.egat.game;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author Patrick Jordan
 */
public class StrategyTest  {
    private Action action1;
    private Action action2;
    private Action action3;
    private Strategy strategy;

    @Before
    public void setUp() {
        action1 = Games.createAction("one");
        action2 = Games.createAction("two");
        action3 = Games.createAction("three");
        strategy = Games.createStrategy(new Action[]{action1,action2},new Number[]{0.25,0.75});
    }

    @Test
    public void testEquality() {
        assertEquals(0.25,strategy.getProbability(action1).doubleValue(),0.01);
        assertEquals(0.75,strategy.getProbability(action2).doubleValue(),0.01);
    }

    @Test
    public void testInvalidAction() {
        assertEquals(0.00,strategy.getProbability(action3).doubleValue(),0.01);
    }
}
