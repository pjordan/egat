/*
 * Payoff.java
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
 * Payoff interface that provides a mapping between {@link Player simulation players}
 *  and their corresponding {@link Number value}.
 * {@link StrategicGame Strategic games} use this mapping to determine a
 * {@link Payoff payoff}.
 * <p/>
 * <p>The methods {@link #equals(Object)} and {@link #hashCode()} should reflect
 * the player-value map.  This is tested by comparing {@link #entrySet()}'s.
 * </p>
 *
 * @author Patrick Jordan
 */
public interface Payoff<T extends PayoffValue> {
    /**
     * Get the player payoff.
     *
     * @param player the player.
     * @return the player payoff.
     */
    T getPayoff(Player player);

    /**
     * Get the set of players which are mapped in the payoff.
     *
     * @return the set of players.
     */
    Set<Player> players();

    /**
     * Get the player-value entries.
     *
     * @return the player-value entries.
     */
    Set<Map.Entry<Player, T>> entrySet();

    /**
     * Get the collection of values received by the players.
     *
     * @return the collection of values.
     */
    Collection<T> values();
}
