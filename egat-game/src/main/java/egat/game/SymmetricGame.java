/*
 * SymmetricGame.java
 *
 * Copyright (C) 2006-2010 Patrick R. Jordan
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

/**
 * SymmetricGame is a marker interface for a symmetric simulation.
 *
 * @param <T> the payoff value type.
 *
 * @author Patrick Jordan
 */
public interface SymmetricGame<T extends PayoffValue> extends StrategicGame<T>, SymmetricMultiAgentSystem {

    /**
     * Get the payoff of the outcome.
     * @param outcome the outcome.
     * @return the payoff of the outcome, <code>null</code> if the outcome is
     * invalid.
     */
    SymmetricPayoff<T> payoff(Outcome outcome) throws NonexistentPayoffException;
}
