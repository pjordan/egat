package edu.umich.eecs.ai.egat.egame;

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
      final Class<?> cls = edu.umich.eecs.ai.egat.egame.EGames.class;
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
