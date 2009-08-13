/*
 * StrategicBestFirstFormationSearch.java
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
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.ActionReducedStrategicGame;
import edu.umich.eecs.ai.egat.minform.StrategicRationalizableFinder;

import java.util.*;

/**
 * @author Patrick R. Jordan
 */
public class StrategicEpsilonGreedyFormationSearch extends EpsilonGreedyFormationSearch<StrategicGame, Map<Player, Set<Action>>> {
    private Player[] players;
    private Action[][] actions;
    private StrategicRationalizableFinder rationalizableFinder;
    private double tolerance;

    public StrategicEpsilonGreedyFormationSearch(StrategicGame base, StrategicRationalizableFinder rationalizableFinder) {
        this(base, rationalizableFinder, 1e-8);
    }

    public StrategicEpsilonGreedyFormationSearch(StrategicGame base, StrategicRationalizableFinder rationalizableFinder, double tolerance) {
        super(base);

        this.rationalizableFinder = rationalizableFinder;
        this.tolerance = tolerance;

        players = base.players().toArray(new Player[0]);

        actions = new Action[players.length][];

        for (int i = 0; i < players.length; i++) {

            actions[i] = base.getActions(players[i]).toArray(new Action[0]);

        }
    }

    protected void initialNodes(StrategicGame base,
                                Queue<FormationSearchNode<StrategicGame, Map<Player, Set<Action>>>> queue,
                                Map<Map<Player, Set<Action>>, FormationSearchNode<StrategicGame, Map<Player, Set<Action>>>> nodes,
                                int bound) {
        int total = 1;
        for (Action[] a : actions) {
            total *= a.length;
        }

        for (int i = 0; i < total; i++) {
            int remainder = i;

            Map<Player, Set<Action>> strategySpace = new HashMap<Player, Set<Action>>();

            StrategicGame game = base;

            for (int j = actions.length - 1; j >= 0; j--) {
                int offset = remainder % (actions[j].length);
                remainder /= actions[j].length;

                Player p = players[j];
                Action a = actions[j][offset];
                Set<Action> s = new HashSet<Action>();
                s.add(a);

                strategySpace.put(p, s);

                game = new ActionReducedStrategicGame(game, p, s);
            }

            if (1 <= bound) {
                double epsilon = rationalizableFinder.rationalizableEpsilon(game, base);
                epsilon = Math.round(epsilon/tolerance)*tolerance;

                FormationSearchNode<StrategicGame, Map<Player, Set<Action>>> node = new FormationSearchNode<StrategicGame, Map<Player, Set<Action>>>(game, strategySpace, epsilon, 1);

                queue.offer(node);
                nodes.put(strategySpace, node);
            }
        }
    }

    protected void expandNode(FormationSearchNode<StrategicGame, Map<Player, Set<Action>>> node,
                              Queue<FormationSearchNode<StrategicGame, Map<Player, Set<Action>>>> queue,
                              Map<Map<Player, Set<Action>>, FormationSearchNode<StrategicGame, Map<Player, Set<Action>>>> nodes,
                              int bound) {

        for (int i = 0; i < players.length; i++) {

            Set<Action> currentPlayerActions = node.getGame().getActions(players[i]);

            if(currentPlayerActions.size()!=actions[i].length) {

                for (int j = 0; j < actions[i].length; j++) {

                    if(!currentPlayerActions.contains(actions[i][j])) {

                        Set<Action> newActions = new HashSet<Action>(currentPlayerActions);

                        newActions.add(actions[i][j]);

                        Map<Player, Set<Action>> key = new HashMap<Player, Set<Action>>();

                        for(int k = 0; k < players.length; k++) {
                            key.put(players[k], k==i ? newActions : node.getGame().getActions(players[k]));
                        }

                        if(!nodes.containsKey(key)) {
                            FormationSearchNode<StrategicGame, Map<Player, Set<Action>>> child = createNode(key, bound);

                            if(child!=null) {
                                queue.offer(child);
                                nodes.put(key, child);
                            }
                        }
                    }
                }
            }
        }
    }

    protected FormationSearchNode<StrategicGame, Map<Player, Set<Action>>> createNode(Map<Player, Set<Action>> strategySpace, int bound) {
        FormationSearchNode<StrategicGame, Map<Player, Set<Action>>> node = null;
        StrategicGame game = getBase();

        int total = 1;
        for (Player p : strategySpace.keySet()) {
            Set<Action> playerActions = strategySpace.get(p);
            game = new ActionReducedStrategicGame(game, p, playerActions);
            total *= playerActions.size();
        }


        if (total <= bound) {
            double epsilon = rationalizableFinder.rationalizableEpsilon(game, getBase());
            epsilon = Math.round(epsilon/tolerance)*tolerance;

            node = new FormationSearchNode<StrategicGame, Map<Player, Set<Action>>>(game, strategySpace, epsilon, total);

        }

        return node;
    }
}