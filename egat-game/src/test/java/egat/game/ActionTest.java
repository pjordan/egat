/*
 * ActionTest.java
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
package egat.game;


import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author Patrick Jordan
 */
public class ActionTest {
    private Action action1;
    private Action action2;
    private Action action3;

    @Before
    public void setUp() {
        action1 = Games.createAction("one");
        action2 = Games.createAction("two");
        action3 = Games.createAction("one");
    }

    @Test
    public void testEquality() {
        assertTrue(action1.equals(action3));
    }

    @Test
    public void testEqualityHashCode() {
        assertEquals(action1.hashCode(),action3.hashCode());
    }

    @Test
    public void testInEquality() {
        assertFalse(action1.equals(action2));
    }

    @Test
    public void testNullEquality() {
        assertFalse(action1.equals(null));
    }
}
