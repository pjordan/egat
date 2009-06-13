/*
 * AbstractStrategicGame.java
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
    public final Payoff payoff(final Profile profile) throws NonexistentPayoffException {
        return Games.computeStrategicPayoffUsingReduction(profile, this);
    }
}
