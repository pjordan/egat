/*
 * Strategy.java
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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Player strategy in a {@link StrategicGame strategic simulation}.
 * <p/>
 * <p>The methods {@link #equals(Object)} and {@link #hashCode()}
 * should reflect the action-probability map.
 * This is tested by comparing {@link #entrySet()}'s.
 * </p>
 *
 * @author Patrick Jordan
 */
public interface Strategy {
    /**
     * Get the probability of the action being taken.
     *
     * @param action the action.
     * @return the probability of the action being taken.
     */
    Number getProbability(Action action);

    /**
     * Get the set of actions which are mapped in the strategy.
     *
     * @return the set of actions.
     */
    Set<Action> actions();

    /**
     * Get the action-probability entries.
     *
     * @return the action-probability entries.
     */
    Set<Map.Entry<Action, Number>> entrySet();

    /**
     * Get the collection of probabilities used in the strategy.
     *
     * @return the collection of probabilities.
     */
    Collection<Number> probabilities();
}
