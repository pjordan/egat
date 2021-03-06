/*
 * EGamesTest.java
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
package egat.egame;

import org.junit.Test;
import org.junit.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Patrick Jordan
 */
public class EGamesTest {
    @Test
    public void testPrivateConstructor() {
      final Class<?> cls = egat.egame.EGames.class;
      final Constructor<?> c = cls.getDeclaredConstructors()[0];
      c.setAccessible(true);
        try {
            final Object n = c.newInstance((Object[])null);
        } catch (InstantiationException e) {
            Assert.fail();
        } catch (IllegalAccessException e) {
            Assert.fail();
        } catch (InvocationTargetException e) {
            Assert.fail();  
        }
    }
}
