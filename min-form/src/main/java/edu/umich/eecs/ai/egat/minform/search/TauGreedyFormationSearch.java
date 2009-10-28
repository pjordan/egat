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
package edu.umich.eecs.ai.egat.minform.search;

import edu.umich.eecs.ai.egat.game.StrategicGame;
import edu.umich.eecs.ai.egat.game.MultiAgentSystem;

import java.util.Queue;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * @author Patrick R. Jordan
 */
public abstract class TauGreedyFormationSearch<T extends StrategicGame, M extends MultiAgentSystem, S> implements FormationSearch<T, M, S> {
    private T base;

    public TauGreedyFormationSearch(T base) {
        this.base = base;
    }

    public FormationSearchNode<T, S> run(int bound) {
        FormationSearchNode<T, S> best = null;


        FormationSearchNode<T, S> node = initialNode(base, bound);

        while (node != null) {

            if (best == null || node.compareTo(best) < 0) {
                best = node;
            }

            if (best.getValue() < Double.MIN_VALUE) {
                return best;
            }


            node = expandNode(node, bound);
        }

        return best;
    }

    public FormationSearchNode<T, S> run(M bound) {
        FormationSearchNode<T, S> best = null;


        FormationSearchNode<T, S> node = initialNode(base, bound);

        while (node != null) {

            if (best == null || node.compareTo(best) < 0) {
                best = node;
            }

            if (best.getValue() < Double.MIN_VALUE) {
                return best;
            }


            node = expandNode(node, bound);
        }

        return best;
    }


    protected abstract FormationSearchNode<T, S> initialNode(T base, M bound);

    protected abstract FormationSearchNode<T, S> expandNode(FormationSearchNode<T, S> node, M bound);

    protected abstract FormationSearchNode<T, S> initialNode(T base, int bound);

    protected abstract FormationSearchNode<T, S> expandNode(FormationSearchNode<T, S> node, int bound);

    public T getBase() {
        return base;
    }
}