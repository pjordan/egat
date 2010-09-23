/*
 * Identifiable.java
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
 * The {@link Identifiable} interface for a multi-agent system.  The contract for {@link Identifiable}
 * requires that two identifiables of a given class are equal if {@link # getID()} returns
 * equal strings for both identifiables.
 *
 * @param <T> the identifiable type.
 *
 * @author Patrick Jordan
 */
public interface Identifiable<T extends Identifiable> extends Comparable<T> {
    /**
     * Returns the identifier for this identifiable.
     * @return the identifier for this identifiable.
     */
    String getID();
}
