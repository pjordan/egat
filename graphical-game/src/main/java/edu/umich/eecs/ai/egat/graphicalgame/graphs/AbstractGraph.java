/*
 * AbstractGraph.java
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

import edu.umich.eecs.ai.egat.graphicalgame.graphs.Graph;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public abstract class AbstractGraph<T> implements MutableGraph<T>, Cloneable {
    private Set<T> nodes;

    private int hashCode;
    
    protected AbstractGraph(Collection<? extends T> nodes) {
        this();
        
        for(T t : nodes) {
            addNode(t);
        }
    }

    protected AbstractGraph() {
        this.nodes = new LinkedHashSet<T>();
        afterNodesInitialization();
        updateHashCode();
    }

    protected void afterNodesInitialization() {            
    }

    public Set<T> nodes() {
        return nodes;
    }

    public boolean addNode(T node) {

        beforeAddNode(node);

        boolean status = nodes.add(node);

        if(status) {
            afterAddNode(node);
            updateHashCode();
        }        

        return status;
    }

    protected void beforeAddNode(T node) {
    }

    protected void afterAddNode(T node) {
    }

    public boolean removeNode(T node) {

        beforeRemoveNode(node);

        boolean status = nodes.remove(node);

        if(status) {
            afterRemoveNode(node);
            updateHashCode();
        }

        return status;
    }

    protected void beforeRemoveNode(T node) {
    }

    protected void afterRemoveNode(T node) {
    }

    @Override
    public AbstractGraph<T> clone() {
        try {
            AbstractGraph<T> clone = (AbstractGraph<T>)super.clone();

            clone.nodes = new LinkedHashSet<T>(nodes);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graph)) return false;
        if (o.hashCode()!=hashCode()) return false;

        Graph that = (Graph) o;

        if (!edgeLists().equals(that.edgeLists())) return false;
        if (!nodes().equals(that.nodes())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }


    protected final void updateHashCode() {
        hashCode = 31 * edgeLists().hashCode() + nodes().hashCode();
    }
}
