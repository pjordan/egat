/*
 * SimpleActionTest.java
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

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * @author Patrick Jordan
 */
public class SimpleActionTest {

    @Test
    public void testAction() {
        SimpleAction aAction = new SimpleAction("a");
        SimpleAction bAction = new SimpleAction("b");

        assertNotNull(aAction);
        assertNotNull(bAction);

        assertEquals(aAction, new SimpleAction("a"));
        assertEquals(aAction, aAction);
        assertEquals(bAction, new SimpleAction("b"));
        assertFalse(aAction.equals(bAction));
        assertFalse(aAction.equals(null));

        assertEquals(aAction.compareTo(bAction),"a".compareTo("b"));
        assertEquals(aAction.hashCode(),"a".hashCode());

        assertEquals(aAction.getID(),"a");
    }

    @Test(expected = NullPointerException.class)
    public void testNullIdAction() {
        new SimpleAction(null);        
    }
}
