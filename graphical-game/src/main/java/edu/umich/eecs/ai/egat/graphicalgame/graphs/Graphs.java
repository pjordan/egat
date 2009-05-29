/**
 * Created by IntelliJ IDEA.
 * User: pjordan
 * Date: Apr 6, 2009
 * Time: 7:40:37 AM
 * To change this template use File | Settings | File Templates.
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
