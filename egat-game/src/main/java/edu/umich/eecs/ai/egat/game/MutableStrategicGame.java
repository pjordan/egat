package edu.umich.eecs.ai.egat.game;

/**
 * Mutable strategic game is an extension of the {@link StrategicGame strategic game} interface that allows
 * the payoffs for a given {@link Outcome outcome} to be set.
 *
 * @param <T> the payoff value type.
 * @author Patrick Jordan
 */
public interface MutableStrategicGame<T extends PayoffValue> extends StrategicGame<T>,
                                                                     MutableMultiAgentSystem,
                                                                     MutableStrategicMultiAgentSystem {
    /**
     * Sets the payoff for a given outcome.
     * @param outcome the outcome whose payoff is to be set.
     * @param payoff the payoff for the outcome.
     */
    void putPayoff(Outcome outcome, Payoff<T> payoff);
}
