package edu.umich.eecs.ai.egat.game;

/**
 * The abstract strategic game class defines a helper method for computing the profile payoffs of
 * strategic games.
 *
 * @param <T> the payoff value type.
 *
 * @author Patrick Jordan
 */
public abstract class AbstractStrategicGame<T extends PayoffValue> extends DefaultMultiAgentSystem
                                                                   implements StrategicGame<T> {

    /**
     * Creates an abstract strategic game.
     * @param name the name of the game.
     * @param description the description of the game.
     */
    public AbstractStrategicGame(final String name, final String description) {
        super(name, description);
    }

    /**
     * Creates an abstract strategic game.
     * @param name the name of the game.
     */
    public AbstractStrategicGame(final String name) {
        super(name);
    }

    /**
     * Creates and abstract strategic game.
     */
    protected AbstractStrategicGame() {
    }

    /**
     * Computes the payoff of a given profile.
     * @param profile the profile.
     * @return the payoff of a given profile.
     */
    public final Payoff payoff(final Profile profile) {
        return Games.computeStrategicPayoffUsingReduction(profile, this);
    }
}
