package edu.umich.eecs.ai.egat.game;

import java.util.Collection;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public interface MutableSymmetricGame<T extends PayoffValue> extends MutableGame, SymmetricGame<T>, MutableSymmetricSimulation {
    void putPayoff(Outcome outcome, Map<Action,T> values);
}
