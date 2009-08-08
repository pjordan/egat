/*
 * LpSolveStrategicRationalizableFinder.java
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
package edu.umich.eecs.ai.egat.minform;

import edu.umich.eecs.ai.egat.game.*;

import java.util.*;

import lpsolve.LpSolveException;
import lpsolve.LpSolve;

/**
 * @author Patrick R. Jordan
 */
public class LpSolveStrategicRationalizableFinder implements StrategicRationalizableFinder {
    public Set<Action> findRationalizable(Player player, StrategicMultiAgentSystem restrictedActions, StrategicGame game) {

        Set<Player> otherPlayers = new HashSet<Player>(game.players());

        otherPlayers.remove(player);

        StrategicMultiAgentSystem subGame = new PlayerReducedStrategicMultiAgentSystem(game, otherPlayers);

        Set<Action> others = new HashSet<Action>(game.getActions(player));

        Player[] allPlayers = game.players().toArray(new Player[0]);

        Action[] playerActions = new Action[allPlayers.length];

        Set<Action> rationalizableActions = new HashSet<Action>();


        for (Action action : restrictedActions.getActions(player)) {
            others.remove(action);
        }

        int playerIndex = findPlayerIndex(player, allPlayers);

        try {
            for (Action candidateAction : others) {
                if (isRationalizable(playerIndex, candidateAction, allPlayers, playerActions, subGame, game)) {
                    rationalizableActions.add(candidateAction);
                }
            }
        } catch (LpSolveException e) {
            throw new RuntimeException(e);
        }

        return rationalizableActions;
    }

    private int findPlayerIndex(Player player, Player[] players) {
        int playerIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if (player.equals(players[i])) {
                playerIndex = i;
                break;
            }
        }

        return playerIndex;
    }

    private boolean isRationalizable(int player, Action candidateAction, Player[] allPlayers, Action[] playerActions, StrategicMultiAgentSystem subGame, StrategicGame game) throws LpSolveException {
        return rationalizableSlack(player, candidateAction, allPlayers, playerActions, subGame, game) <= 0.0;
    }

    public Action findMaxRationalizable(Player player, StrategicMultiAgentSystem restrictedActions, StrategicGame game) {
        Set<Action> others = new HashSet<Action>(game.getActions(player));

        for (Action action : restrictedActions.getActions(player)) {
            others.remove(action);
        }

        return findMaxRationalizable(player, restrictedActions, others, game);
    }

    public Action findMaxRationalizable(Player player, StrategicMultiAgentSystem restrictedActions, Set<Action> otherActions, StrategicGame game) {

        Player[] allPlayers = game.players().toArray(new Player[0]);
        Action[] playerActions = new Action[allPlayers.length];

        Action maxRationalizable = null;
        double maxSlack = Double.NEGATIVE_INFINITY;

        Set<Player> otherPlayers = new HashSet<Player>(game.players());

        otherPlayers.remove(player);

        StrategicMultiAgentSystem subGame = new PlayerReducedStrategicMultiAgentSystem(restrictedActions, otherPlayers);


        int playerIndex = findPlayerIndex(player, allPlayers);

        try {
            for (Action candidateAction : otherActions) {
                double slack = -rationalizableSlack(playerIndex, candidateAction, allPlayers, playerActions, subGame, game);
                if (maxRationalizable == null || maxSlack < slack) {
                    maxRationalizable = candidateAction;
                    maxSlack = slack;
                }
            }
        } catch (LpSolveException e) {
            throw new RuntimeException(e);
        }

        return maxRationalizable;
    }

    public double rationalizableSlack(Player player, Action candidateAction, StrategicMultiAgentSystem restrictedActions, StrategicGame game) {

        Player[] allPlayers = game.players().toArray(new Player[0]);
        Action[] playerActions = new Action[allPlayers.length];

        Set<Player> otherPlayers = new HashSet<Player>(game.players());

        otherPlayers.remove(player);

        StrategicMultiAgentSystem subGame = new PlayerReducedStrategicMultiAgentSystem(restrictedActions, otherPlayers);

        double slack = Double.NaN;

        int playerIndex = findPlayerIndex(player, allPlayers);

        try {
            slack = -rationalizableSlack(playerIndex, candidateAction, allPlayers, playerActions, subGame, game);
        } catch (LpSolveException e) {
            throw new RuntimeException(e);
        }

        return slack;
    }

    private double rationalizableSlack(int player, Action candidateAction, Player[] allPlayers, Action[] playerActions, StrategicMultiAgentSystem subGame, StrategicGame game) throws LpSolveException {
        LpSolve lp;

        int ret = 0;

        double slack = Double.NaN;

        List<Outcome> subOutcomes = new ArrayList<Outcome>();

        for (Outcome o : Games.traversal(subGame)) {
            subOutcomes.add(o);
        }

        int subGameSize = subOutcomes.size();


        /* create space large enough for one row */
        int[] colno = new int[subGameSize + 1];

        for (int i = 0; i < subGameSize + 1; i++) {
            colno[i] = i + 1;
        }

        double[] row = new double[subGameSize + 1];

        lp = LpSolve.makeLp(0, subGameSize + 1);
        if (lp.getLp() == 0) {
            ret = 1; /* couldn't construct a new model... */
        } else {
            lp.setAddRowmode(true);  /* makes building the model faster if it is done rows by row */
        }

        if (ret == 0) {
            Arrays.fill(row, 1.0);
            row[subGameSize] = 0.0;

            /* add the row to lpsolve */
            lp.addConstraintex(subGameSize + 1, row, colno, LpSolve.EQ, 1.0);

            Arrays.fill(row, 0.0);

            for (int i = 0; i < subGameSize; i++) {
                row[i] = 1.0;

                /* add the row to lpsolve */
                lp.addConstraintex(subGameSize + 1, row, colno, LpSolve.GE, 0.0);

                row[i] = 0.0;
            }

            lp.setLowbo(subGameSize + 1, Double.NEGATIVE_INFINITY);
        }

        row[subGameSize] = 1.0;

        for (Action a : game.getActions(allPlayers[player])) {
            if (!a.equals(candidateAction)) {
                for (int i = 0; i < subGameSize; i++) {
                    for (int j = 0; j < playerActions.length; j++) {
                        if (j != player) {
                            playerActions[j] = subOutcomes.get(i).getAction(allPlayers[j]);
                        }
                    }
                    playerActions[player] = candidateAction;
                    double payoffCandidate = game.payoff(Games.createOutcome(allPlayers, playerActions)).getPayoff(allPlayers[player]).getValue();

                    playerActions[player] = a;
                    double payoffA = game.payoff(Games.createOutcome(allPlayers, playerActions)).getPayoff(allPlayers[player]).getValue();

                    row[i] = payoffCandidate - payoffA;
                }

                if (ret == 0) {
                    /* add the row to lpsolve */
                    lp.addConstraintex(subGameSize + 1, row, colno, LpSolve.GE, 0);
                }
            }
        }

        if (ret == 0) {
            lp.setAddRowmode(false); /* rowmode should be turned off again when done building the model */

            Arrays.fill(row, 0);

            row[subGameSize] = 1.0;

            /* set the objective in lpsolve */
            lp.setObjFnex(subGameSize + 1, row, colno);
        }

        if (ret == 0) {
            /* set the object direction to maximize */
            lp.setMinim();

            /* I only want to see important messages on screen while solving */
            lp.setVerbose(LpSolve.IMPORTANT);

            /* Now let lpsolve calculate a solution */
            ret = lp.solve();
            if (ret == LpSolve.OPTIMAL) {
                ret = 0;

                slack = lp.getObjective();
            }
        }

        /* clean up such that all used memory by lpsolve is freed */
        if (lp.getLp() != 0) {
            lp.deleteLp();
        }

        return slack;
    }

    public double rationalizableDelta(Player player, StrategicMultiAgentSystem restrictedGame, StrategicGame game) {
        double delta = 0.0;

        Set<Action> restricted = restrictedGame.getActions(player);

        for (Action a : game.getActions(player)) {

            if (!restricted.contains(a)) {

                delta = Math.max(delta, rationalizableSlack(player, a, restrictedGame, game));

            }

        }

        return delta;
    }

    public double rationalizableEpsilon(StrategicMultiAgentSystem restrictedGame, StrategicGame game) {
        double epsilon = 0.0;

        for (Player p : game.players()) {
            epsilon = Math.max(epsilon, rationalizableDelta(p, restrictedGame, game));

        }

        return epsilon;
    }
}
