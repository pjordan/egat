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
package egat.minform.search;

import egat.game.Action;
import egat.game.SymmetricGame;
import egat.game.ActionReducedSymmetricGame;
import egat.game.SymmetricMultiAgentSystem;
import egat.minform.SymmetricRationalizableFinder;

import java.util.Set;
import java.util.Queue;
import java.util.Map;
import java.util.HashSet;

/**
 * @author Patrick R. Jordan
 */
public class SymmetricBestFirstFormationSearch extends BestFirstFormationSearch<SymmetricGame, SymmetricMultiAgentSystem, Set<Action>> {
    private Action[] actions;
    private SymmetricRationalizableFinder rationalizableFinder;
    private double tolerance;

    public SymmetricBestFirstFormationSearch(SymmetricGame base, SymmetricRationalizableFinder rationalizableFinder, int maxQueueSize, double tolerance) {
        super(base, maxQueueSize);
        this.rationalizableFinder = rationalizableFinder;
        this.tolerance = tolerance;

        actions = base.getActions().toArray(new Action[0]);
    }

    public SymmetricBestFirstFormationSearch(SymmetricGame base, SymmetricRationalizableFinder rationalizableFinder) {
        this(base, rationalizableFinder, 1e-8);
    }

    public SymmetricBestFirstFormationSearch(SymmetricGame base, SymmetricRationalizableFinder rationalizableFinder, int maxQueueSize) {
        this(base, rationalizableFinder, maxQueueSize, 1e-8);
    }

    public SymmetricBestFirstFormationSearch(SymmetricGame base, SymmetricRationalizableFinder rationalizableFinder, double tolerance) {
        this(base, rationalizableFinder, Integer.MAX_VALUE, tolerance);
    }

    protected void initialNodes(SymmetricGame base,
                                Queue<FormationSearchNode<SymmetricGame, Set<Action>>> queue,
                                Map<Set<Action>, FormationSearchNode<SymmetricGame, Set<Action>>> nodes,
                                int bound) {

        for (int i = 0; i < actions.length; i++) {

            Set<Action> strategySpace = new HashSet<Action>();

            strategySpace.add(actions[i]);

            SymmetricGame game = new ActionReducedSymmetricGame(base, strategySpace);

            if (1 <= bound) {
                double epsilon = rationalizableFinder.rationalizableEpsilon(game, base);
                epsilon = Math.round(epsilon / tolerance) * tolerance;

                FormationSearchNode<SymmetricGame, Set<Action>> node = new FormationSearchNode<SymmetricGame, Set<Action>>(game, strategySpace, epsilon, 1);

                queue.offer(node);
                nodes.put(strategySpace, node);
            }
        }
    }

    protected void expandNode(FormationSearchNode<SymmetricGame, Set<Action>> node,
                              Queue<FormationSearchNode<SymmetricGame, Set<Action>>> queue,
                              Map<Set<Action>, FormationSearchNode<SymmetricGame, Set<Action>>> nodes,
                              int bound) {

        Set<Action> currentPlayerActions = node.getGame().getActions();

        if (currentPlayerActions.size() != actions.length) {

            for (Action action : actions) {

                if (!currentPlayerActions.contains(action)) {

                    Set<Action> key = new HashSet<Action>(currentPlayerActions);

                    key.add(action);


                    if (!nodes.containsKey(key)) {
                        double tau = rationalizableFinder.rationalizableTau(action, node.getGame(), getBase());

                        if (tau > 0) {
                            FormationSearchNode<SymmetricGame, Set<Action>> child = createNode(key, bound);

                            if (child != null) {
                                queue.offer(child);
                                nodes.put(key, child);
                            }
                        }
                    }
                }
            }
        }
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

    protected void initialNodes(SymmetricGame base, Queue<FormationSearchNode<SymmetricGame, Set<Action>>> queue,
                                Map<Set<Action>, FormationSearchNode<SymmetricGame, Set<Action>>> nodes,
                                SymmetricMultiAgentSystem bound) {

        for (Action action : actions) {
            if (bound.getActions().contains(action)) {
                Set<Action> strategySpace = new HashSet<Action>();

                strategySpace.add(action);

                SymmetricGame game = new ActionReducedSymmetricGame(base, strategySpace);


                double epsilon = rationalizableFinder.rationalizableEpsilon(game, base);
                epsilon = Math.round(epsilon / tolerance) * tolerance;

                FormationSearchNode<SymmetricGame, Set<Action>> node = new FormationSearchNode<SymmetricGame, Set<Action>>(game, strategySpace, epsilon, 1);

                queue.offer(node);
                nodes.put(strategySpace, node);
            }
        }
    }

    protected void expandNode(FormationSearchNode<SymmetricGame, Set<Action>> node,
                              Queue<FormationSearchNode<SymmetricGame, Set<Action>>> queue,
                              Map<Set<Action>, FormationSearchNode<SymmetricGame, Set<Action>>> nodes,
                              SymmetricMultiAgentSystem bound) {

        Set<Action> currentPlayerActions = node.getGame().getActions();

        if (currentPlayerActions.size() != bound.getActions().size()) {

            for (Action action : actions) {

                if (bound.getActions().contains(action) && !currentPlayerActions.contains(action)) {

                    Set<Action> key = new HashSet<Action>(currentPlayerActions);

                    key.add(action);


                    if (!nodes.containsKey(key)) {
                        double tau = rationalizableFinder.rationalizableTau(action, node.getGame(), getBase());

                        if (tau > 0) {
                            FormationSearchNode<SymmetricGame, Set<Action>> child = createNode(key);

                            if (child != null) {
                                queue.offer(child);
                                nodes.put(key, child);
                            }
                        }
                    }
                }
            }
        }
    }
}
