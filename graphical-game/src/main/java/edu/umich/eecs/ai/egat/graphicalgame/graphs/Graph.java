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
