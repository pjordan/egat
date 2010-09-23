/*
 * BasicSparseGraph.java
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
package egat.graphicalgame.graphs;

import java.util.*;
import java.io.Serializable;

/**
 * @author Patrick Jordan
 */
public class BasicSparseGraph<T> extends AbstractGraph<T> implements SparseGraph<T>, Serializable {

    private Map<T, Set<T>> edgeLists;

    public BasicSparseGraph() {
        super();
    }

    public BasicSparseGraph(Collection<? extends T> nodes) {
        super(nodes);
    }

    public BasicSparseGraph(SparseGraph<T> sparseGraph) {
        this(sparseGraph.nodes());

        for(T from : nodes()) {
            for(T to : nodes()) {
                if(sparseGraph.hasEdge(from,to)) {
                    addEdge(from,to);
                }
            }
        }
    }

    @Override
    protected final void afterNodesInitialization() {
        this.edgeLists = new HashMap<T, Set<T>>();
    }

    @Override
    protected final void afterAddNode(T node) {
        edgeLists.put(node,new LinkedHashSet<T>());
        updateHashCode();
    }

    @Override
    protected final void afterRemoveNode(T node) {
        edgeLists.remove(node);
        updateHashCode();
    }

    public boolean addEdge(T from, T to) {
        beforeAddEdge(from,to);

        if(nodes().contains(from) && nodes().contains(to)) {
            boolean status = edgeLists.get(from).add(to);

            afterAddEdge(from, to);
            updateHashCode();
            
            return status;
        }
        
        return false;
    }


    protected void beforeAddEdge(T from, T to) {
    }

    protected void afterAddEdge(T from, T to) {
    }

    public boolean removeEdge(T from, T to) {
        beforeRemoveEdge(from,to);

        Set<T> list = edgeLists.get(from);

        if(list!=null) {
            boolean status = list.remove(to);

            if(status) {
                afterRemoveEdge(from, to);
                updateHashCode();
            }

            return status;
        }
        
        return false;
    }

    protected void beforeRemoveEdge(T from, T to) {
    }

    protected void afterRemoveEdge(T from, T to) {
    }

    public boolean hasEdge(T from, T to) {
        Set<T> list = edgeLists.get(from);
        return list != null && list.contains(to);
    }

    public Map<T, Set<T>> edgeLists() {
        return edgeLists;
    }

    @Override
    public BasicSparseGraph<T> clone() {
        BasicSparseGraph<T> clone = (BasicSparseGraph<T>)super.clone();

        clone.edgeLists = new HashMap<T, Set<T>>();

        for(Map.Entry<T, Set<T>> entry : edgeLists.entrySet()) {
            clone.edgeLists.put(entry.getKey(), new LinkedHashSet<T>(entry.getValue()));
        }
        
        return clone;
    }
}
