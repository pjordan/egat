/*
 * BestFirstFormationSearch.java
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
package egat.minform.search;

import egat.game.StrategicGame;
import egat.game.MultiAgentSystem;

import java.util.*;

/**
 * @author Patrick R. Jordan
 */
public abstract class BestFirstFormationSearch<T extends StrategicGame, M extends MultiAgentSystem, S> implements FormationSearch<T, M, S> {
    private T base;
    private int maxQueueSize;

    protected BestFirstFormationSearch(T base, int maxQueueSize) {
        this.base = base;
        this.maxQueueSize = maxQueueSize;
    }

    public BestFirstFormationSearch(T base) {
        this(base, Integer.MAX_VALUE);
    }

    public FormationSearchNode<T, S> run(int bound) {
        FormationSearchNode<T, S> best = null;

        PriorityQueue<FormationSearchNode<T, S>> queue = new PriorityQueue<FormationSearchNode<T, S>>();
        Map<S, FormationSearchNode<T, S>> nodes = new HashMap<S, FormationSearchNode<T, S>>();

        initialNodes(base, queue, nodes, bound);


        maintainQueue(queue);

        while (!queue.isEmpty()) {
            FormationSearchNode<T, S> node = queue.poll();

            if (best == null || node.compareTo(best) < 0) {
                best = node;
            }

            if (best.getValue() < Double.MIN_VALUE) {
                return best;
            }

            expandNode(node, queue, nodes, bound);

            maintainQueue(queue);
        }

        return best;
    }

    public FormationSearchNode<T, S> run(M bound) {
        FormationSearchNode<T, S> best = null;

        PriorityQueue<FormationSearchNode<T, S>> queue = new PriorityQueue<FormationSearchNode<T, S>>();
        Map<S, FormationSearchNode<T, S>> nodes = new HashMap<S, FormationSearchNode<T, S>>();

        initialNodes(base, queue, nodes, bound);


        maintainQueue(queue);

        while (!queue.isEmpty()) {
            FormationSearchNode<T, S> node = queue.poll();

            if (best == null || node.compareTo(best) < 0) {
                best = node;
            }

            if (best.getValue() < Double.MIN_VALUE) {
                return best;
            }

            expandNode(node, queue, nodes, bound);

            maintainQueue(queue);
        }

        return best;
    }

    public FormationSearchNode<T, S> run(M bound, Set<S> exclusionList) {
        FormationSearchNode<T, S> best = null;

        PriorityQueue<FormationSearchNode<T, S>> queue = new PriorityQueue<FormationSearchNode<T, S>>();
        Map<S, FormationSearchNode<T, S>> nodes = new HashMap<S, FormationSearchNode<T, S>>();

        initialNodes(base, queue, nodes, bound);


        maintainQueue(queue, exclusionList);

        while (!queue.isEmpty()) {
            FormationSearchNode<T, S> node = queue.poll();

            if (best == null || node.compareTo(best) < 0) {
                best = node;
            }

            if (best.getValue() < Double.MIN_VALUE) {
                return best;
            }

            expandNode(node, queue, nodes, bound);

            maintainQueue(queue, exclusionList);
        }

        return best;
    }

    protected abstract void initialNodes(T base, Queue<FormationSearchNode<T, S>> queue, Map<S, FormationSearchNode<T, S>> nodes, M bound);

    protected abstract void expandNode(FormationSearchNode<T, S> node, Queue<FormationSearchNode<T, S>> queue, Map<S, FormationSearchNode<T, S>> nodes, M bound);

    protected abstract void initialNodes(T base, Queue<FormationSearchNode<T, S>> queue, Map<S, FormationSearchNode<T, S>> nodes, int bound);

    protected abstract void expandNode(FormationSearchNode<T, S> node, Queue<FormationSearchNode<T, S>> queue, Map<S, FormationSearchNode<T, S>> nodes, int bound);

    public T getBase() {
        return base;
    }

    private void maintainQueue(Queue<FormationSearchNode<T, S>> queue) {
        if (queue.size() > maxQueueSize) {

            List<FormationSearchNode<T, S>> list = new ArrayList<FormationSearchNode<T, S>>(queue);
            Collections.sort(list);

            queue.removeAll(list.subList(maxQueueSize, queue.size()));
        }
    }

    private void maintainQueue(Queue<FormationSearchNode<T, S>> queue, Set<S> exclusionList) {

        List<FormationSearchNode<T, S>> list = new ArrayList<FormationSearchNode<T, S>>(queue);

        for(FormationSearchNode<T, S> node : list) {
            if(exclusionList.contains(node.getKey())) {
                queue.remove(node);
            }
        }


        if (queue.size() > maxQueueSize) {

            list = new ArrayList<FormationSearchNode<T, S>>(queue);
            Collections.sort(list);

            queue.removeAll(list.subList(maxQueueSize, queue.size()));
        }
    }
}
