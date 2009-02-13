package edu.umich.eecs.ai.egat.game;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public interface MutableStrategicGame<T extends PayoffValue> extends StrategicGame<T>, MutableGame, MutableStrategicSimulation {
    void putPayoff(Outcome outcome, Payoff<T> payoff);
}
