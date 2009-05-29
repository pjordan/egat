package edu.umich.eecs.ai.egat.graphicalgame.graphs;

import java.util.Set;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public interface SparseGraph<T> extends Graph<T> {
    Map<T, Set<T>> edgeLists();
}
