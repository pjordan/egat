package edu.umich.eecs.ai.egat.graphicalgame.graphs;

import edu.umich.eecs.ai.egat.graphicalgame.graphs.Graph;

/**
 * @author Patrick Jordan
 */
public interface MutableGraph<T> extends Graph<T> {
    boolean addNode(T node);
    boolean removeNode(T node);
    boolean addEdge(T from, T to);
    boolean removeEdge(T from, T to);
    MutableGraph<T> clone();
}
