/*
 * SymmetricOutcome.java
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

import java.util.Set;
import java.util.Map;

/**
 * The symmetric iutcome interface that provides a mapping between
 * {@link Player players} and their corresponding
 * {@link Action action}. {@link SymmetricGame Symmetric games}
 * use this mapping to determine a {@link SymmetricPayoff payoff}.
 * <p/>
 * <p>The methods {@link #equals(Object)} and {@link #hashCode()}
 * should reflect the player-action map.  This is
 * tested by comparing {@link #actionEntrySet()}'s.
 * </p>
 *
 * @author Patrick Jordan
 */
public interface SymmetricOutcome extends Outcome {
    /**
     * Get the count of the action.
     *
     * @param action the action.
     * @return the action, <code>0</code> if there is no mapping for the action.
     */
    Integer getCount(Action action);

    /**
     * Get the player-action entries.
     *
     * @return the player-action entries.
     */
    Set<Map.Entry<Action, Integer>> actionEntrySet();

    /**
     * Tests whether the outcomes are equal up to symmetry.
     * @param outcome the outcome to test for equality.
     * @return <code>true</code> if the outcomes are equal up to symmetry, <code>false</code> otherwise.
     */
    boolean symmetricEquals(SymmetricOutcome outcome);
}
