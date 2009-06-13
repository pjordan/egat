/*
 * Graph.java
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
package edu.umich.eecs.ai.egat.graphicalgame.graphs;

import java.util.Set;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public interface Graph<T> extends Cloneable {
    Set<T> nodes();
    boolean hasEdge(T from, T to);
    Map<T, Set<T>> edgeLists();
    Graph<T> clone();
}
