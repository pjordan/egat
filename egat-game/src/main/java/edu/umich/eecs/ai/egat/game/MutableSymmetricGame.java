package edu.umich.eecs.ai.egat.game;

import java.util.Map;

/**
 * Mutable symmetric game is an extension of the {@link SymmetricGame symmetric game} interface that allows
 * the payoffs for a given {@link Outcome outcome} to be set.
 *
 * @param <T> the payoff value type.
 * @author Patrick Jordan
 */
public interface MutableSymmetricGame<T extends PayoffValue> extends MutableMultiAgentSystem,
                                                                     SymmetricGame<T>,
                                                                     MutableSymmetricMultiAgentSystem {
    /**
     * Sets the payoff for a given outcome.
     * @param outcome the outcome whose payoff is to be set.
     * @param values the mapping for the values.
     */
    void putPayoff(Outcome outcome, Map<Action, T> values);
}
