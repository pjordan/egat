/*
 * SymmetricBestFirstFormationSearch.java
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

import edu.umich.eecs.ai.egat.game.*;
import edu.umich.eecs.ai.egat.minform.SymmetricRationalizableFinder;

import java.util.Set;
import java.util.Queue;
import java.util.Map;
import java.util.HashSet;

/**
 * @author Patrick R. Jordan
 */
public class SymmetricTauGreedyFormationSearch extends TauGreedyFormationSearch<SymmetricGame, SymmetricMultiAgentSystem, Set<Action>> {
    private Action[] actions;
    private SymmetricRationalizableFinder rationalizableFinder;
    private double tolerance;

    public SymmetricTauGreedyFormationSearch(SymmetricGame base, SymmetricRationalizableFinder rationalizableFinder) {
        this(base, rationalizableFinder, 1e-8);
    }

    public SymmetricTauGreedyFormationSearch(SymmetricGame base, SymmetricRationalizableFinder rationalizableFinder, double tolerance) {
        super(base);

        this.rationalizableFinder = rationalizableFinder;
        this.tolerance = tolerance;

        actions = base.getActions().toArray(new Action[0]);
    }

    protected FormationSearchNode<SymmetricGame, Set<Action>> initialNode(SymmetricGame base, int bound) {
        FormationSearchNode<SymmetricGame, Set<Action>> bestNode = null;

        for (int i = 0; i < actions.length; i++) {

            Set<Action> strategySpace = new HashSet<Action>();

            strategySpace.add(actions[i]);

            SymmetricGame game = new ActionReducedSymmetricGame(base, strategySpace);

            if (1 <= bound) {
                double epsilon = rationalizableFinder.rationalizableEpsilon(game, base);
                epsilon = Math.round(epsilon / tolerance) * tolerance;

                FormationSearchNode<SymmetricGame, Set<Action>> node = new FormationSearchNode<SymmetricGame, Set<Action>>(game, strategySpace, epsilon, 1);

                if (bestNode == null || node.getValue() < bestNode.getValue()) {
                    bestNode = node;
                }
            }
        }

        return bestNode;
    }

    protected FormationSearchNode<SymmetricGame, Set<Action>> expandNode(FormationSearchNode<SymmetricGame, Set<Action>> node, int bound) {


        FormationSearchNode<SymmetricGame, Set<Action>> maxNode = null;
        double maxTau = Double.NEGATIVE_INFINITY;

        Set<Action> currentPlayerActions = node.getGame().getActions();

        if (currentPlayerActions.size() != actions.length) {

            for (Action action : actions) {

                if (!currentPlayerActions.contains(action)) {

                    double tau = rationalizableFinder.rationalizableTau(action, node.getGame(), getBase());

                    if (tau > maxTau) {
                        Set<Action> key = new HashSet<Action>(currentPlayerActions);

                        key.add(action);


                        FormationSearchNode<SymmetricGame, Set<Action>> child = createNode(key, bound);

                        if (child != null) {
                            maxNode = child;
                            maxTau = tau;
                        }

                    }
                }
            }
        }

        return maxNode;
    }


    protected FormationSearchNode<SymmetricGame, Set<Action>> createNode(Set<Action> strategySpace, int bound) {
        FormationSearchNode<SymmetricGame, Set<Action>> node = null;
        SymmetricGame game = new ActionReducedSymmetricGame(getBase(), strategySpace);

        int total = calculateGameSize(strategySpace.size(), game.players().size());

        if (total <= bound) {
            double epsilon = rationalizableFinder.rationalizableEpsilon(game, getBase());
            epsilon = Math.round(epsilon / tolerance) * tolerance;

            node = new FormationSearchNode<SymmetricGame, Set<Action>>(game, strategySpace, epsilon, total);

        }

        return node;
    }

    protected FormationSearchNode<SymmetricGame, Set<Action>> createNode(Set<Action> strategySpace) {
        SymmetricGame game = new ActionReducedSymmetricGame(getBase(), strategySpace);

        int total = calculateGameSize(strategySpace.size(), game.players().size());


        double epsilon = rationalizableFinder.rationalizableEpsilon(game, getBase());
        epsilon = Math.round(epsilon / tolerance) * tolerance;

        FormationSearchNode<SymmetricGame, Set<Action>> node = new FormationSearchNode<SymmetricGame, Set<Action>>(game, strategySpace, epsilon, total);


        return node;
    }

    protected int calculateGameSize(int strategies, int players) {
        int n = strategies + players - 1;

        int total = 1;

        int max = Math.max(players, strategies - 1);
        int min = n - max;

        for (int i = max + 1; i <= n; i++) {
            total *= i;
        }

        for (int i = 2; i <= min; i++) {
            total /= i;
        }

        return total;
    }

    protected FormationSearchNode<SymmetricGame, Set<Action>> initialNode(SymmetricGame base, SymmetricMultiAgentSystem bound) {
        FormationSearchNode<SymmetricGame, Set<Action>> bestNode = null;

        for (Action action : actions) {
            if (bound.getActions().contains(action)) {
                Set<Action> strategySpace = new HashSet<Action>();

                strategySpace.add(action);

                SymmetricGame game = new ActionReducedSymmetricGame(base, strategySpace);


                double epsilon = rationalizableFinder.rationalizableEpsilon(game, base);
                epsilon = Math.round(epsilon / tolerance) * tolerance;

                FormationSearchNode<SymmetricGame, Set<Action>> node = new FormationSearchNode<SymmetricGame, Set<Action>>(game, strategySpace, epsilon, 1);

                if (bestNode == null || node.getValue() < bestNode.getValue()) {
                    bestNode = node;
                }
            }
        }

        return bestNode;
    }

    protected FormationSearchNode<SymmetricGame, Set<Action>> expandNode(FormationSearchNode<SymmetricGame, Set<Action>> node, SymmetricMultiAgentSystem bound) {


        FormationSearchNode<SymmetricGame, Set<Action>> maxNode = null;
        double maxTau = Double.NEGATIVE_INFINITY;

        Set<Action> currentPlayerActions = node.getGame().getActions();

        if (currentPlayerActions.size() != actions.length) {

            for (Action action : actions) {

                if (bound.getActions().contains(action) && !currentPlayerActions.contains(action)) {

                    double tau = rationalizableFinder.rationalizableTau(action, node.getGame(), getBase());

                    if (tau > maxTau) {
                        Set<Action> key = new HashSet<Action>(currentPlayerActions);

                        key.add(action);


                        FormationSearchNode<SymmetricGame, Set<Action>> child = createNode(key);

                        if (child != null) {
                            maxNode = child;
                            maxTau = tau;
                        }

                    }
                }
            }
        }

        return maxNode;
    }


}