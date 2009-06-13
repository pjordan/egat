/*
 * Graphs.java
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
import java.util.HashSet;

public class Graphs {
    private Graphs() {
    }

    public static <T> Graph<T> meet(Graph<T> ... graphs) {
        Set<T> nodes = new HashSet<T>();

        for(Graph<T> graph : graphs) {
            nodes.addAll(graph.nodes());
        }

        MutableGraph<T> meet = new BasicSparseGraph<T>();

        for(T node : nodes)  {
            meet.addNode(node);
        }

        for(T node1 : nodes) {
            for(T node2 : nodes) {
                boolean edge = true;

                for(Graph<T> graph : graphs) {
                    if(!graph.hasEdge(node1, node2)) {
                       edge = false;

                        break;
                    }
                }

                if(edge) {
                    meet.addEdge(node1, node2);
                }
            }
        }

        return meet;
    }

    public static <T> Graph<T> join(Graph<T> ... graphs) {
        Set<T> nodes = new HashSet<T>();

        for(Graph<T> graph : graphs) {
            nodes.addAll(graph.nodes());
        }

        MutableGraph<T> join = new BasicSparseGraph<T>();

        for(T node : nodes)  {
            join.addNode(node);
        }

        for(T node1 : nodes) {
            for(T node2 : nodes) {
                boolean edge = false;

                for(Graph<T> graph : graphs) {
                    if(graph.hasEdge(node1, node2)) {
                       edge = true;

                        break;
                    }
                }

                if(edge) {
                    join.addEdge(node1, node2);
                }
            }
        }

        return join;
    }
}
