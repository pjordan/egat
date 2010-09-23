/*
 * MixedSymmetricDominanceTesterImpl.java
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
package egat.dominance;

import static egat.dominance.DominanceUtils.*;

import java.util.*;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

/**
 * @author Patrick Jordan
 */
public class MixedSymmetricDominanceTesterImpl implements SymmetricDominanceTester {
    public static final double SLACK_TOLERANCE = 0.0;

    public boolean isDominated(Action action, SymmetricGame game) {
        Player[] players = game.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

        double slack = Double.POSITIVE_INFINITY;

        Set<Action> playerActionSet = new HashSet<Action>(game.getActions());
        playerActionSet.remove(action);
        Action[] playerActions = playerActionSet.toArray(new Action[0]);

        if(playerActions.length<1) {
            return false;
        }

        Player player = players[0];
        List<SymmetricOutcome> subOutcomes = createSubgameOutcomes(player, game);

        int subGameSize = subOutcomes.size();

        int ret = 0;

        int playerIndex = 0;

        /* create space large enough for one row */
        int[] colno = new int[playerActions.length+1];

        for (int i = 0; i < playerActions.length+1; i++) {
            colno[i] = i + 1;
        }

        double[] row = new double[playerActions.length+1];

        try {
            LpSolve lp = LpSolve.makeLp(0, playerActions.length+1);

            if (lp.getLp() == 0) {
                ret = 1; /* couldn't construct a new model... */
            } else {
                lp.setAddRowmode(true);  /* makes building the model faster if it is done rows by row */
            }


            if (ret == 0) {
                Arrays.fill(row, 0.0);

                for (int i = 0; i < playerActions.length; i++) {
                    row[i] = 1.0;

                    /* add the row to lpsolve */
                    lp.addConstraintex(playerActions.length, row, colno, LpSolve.GE, 0.0);

                    row[i] = 0.0;
                }

                lp.setLowbo(playerActions.length+1,Double.NEGATIVE_INFINITY);
            }


            row[playerActions.length] = 1.0;


            for (int i = 0; i < subGameSize; i++) {
                double payoffAction = getPayoff(action, subOutcomes.get(i), players, actions, game, playerIndex);

                for (int j = 0 ; j < playerActions.length; j++) {

                    double payoffDeviant = getPayoff(playerActions[j], subOutcomes.get(i), players, actions, game, playerIndex);

                    row[j] = payoffDeviant;
                }

                if (ret == 0) {
                    /* add the row to lpsolve */
                    lp.addConstraintex(playerActions.length+1, row, colno, LpSolve.GE, payoffAction);
                }
            }


            if (ret == 0) {
                lp.setAddRowmode(false); /* rowmode should be turned off again when done building the model */


                Arrays.fill(row,1.0);
                row[playerActions.length] = 0.0;

                /* add the row to lpsolve */
                lp.addConstraintex(playerActions.length, row, colno, LpSolve.EQ, 1.0);

                Arrays.fill(row, 0);

                row[playerActions.length] = 1.0;

                /* set the objective in lpsolve */
                lp.setObjFnex(playerActions.length+1, row, colno);

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
            if (lp.getLp() != 0)

            {
                lp.deleteLp();
            }
        } catch (LpSolveException e) {
            throw new RuntimeException(e);
        }

        return slack < SLACK_TOLERANCE;
    }

    public boolean isDominated(Strategy strategy, SymmetricGame game) {
        Player[] players = game.players().toArray(new Player[0]);
        Strategy[] strategies = new Strategy[players.length];

        double slack = Double.POSITIVE_INFINITY;

        Action[] playerActions = game.getActions().toArray(new Action[0]);

        if(playerActions.length<2) {
            return false;
        }
        
        Player player = players[0];

        List<SymmetricOutcome> subOutcomes = createSubgameOutcomes(player, game);

        int subGameSize = subOutcomes.size();

        int ret = 0;

        int playerIndex = 0;

        /* create space large enough for one row */
        int[] colno = new int[playerActions.length+1];

        for (int i = 0; i < playerActions.length+1; i++) {
            colno[i] = i + 1;
        }

        double[] row = new double[playerActions.length+1];

        try {
            LpSolve lp = LpSolve.makeLp(0, playerActions.length+1);

            if (lp.getLp() == 0) {
                ret = 1; /* couldn't construct a new model... */
            } else {
                lp.setAddRowmode(true);  /* makes building the model faster if it is done rows by row */
            }


            if (ret == 0) {
                Arrays.fill(row, 0.0);

                for (int i = 0; i < playerActions.length; i++) {
                    row[i] = 1.0;

                    /* add the row to lpsolve */
                    lp.addConstraintex(playerActions.length, row, colno, LpSolve.GE, 0.0);

                    row[i] = 0.0;
                }

                lp.setLowbo(playerActions.length+1,Double.NEGATIVE_INFINITY);
            }


            row[playerActions.length] = 1.0;


            for (int i = 0; i < subGameSize; i++) {
                double payoffAction = getPayoff(strategy, subOutcomes.get(i), players, strategies, game, playerIndex);

                for (int j = 0 ; j < playerActions.length; j++) {

                    double payoffDeviant = getPayoff(Games.createPureStrategy(playerActions[j]), subOutcomes.get(i), players, strategies, game, playerIndex);

                    row[j] = payoffDeviant;
                }

                if (ret == 0) {
                    /* add the row to lpsolve */
                    lp.addConstraintex(playerActions.length+1, row, colno, LpSolve.GE, payoffAction);
                }
            }


            if (ret == 0) {
                lp.setAddRowmode(false); /* rowmode should be turned off again when done building the model */


                Arrays.fill(row,1.0);
                row[playerActions.length] = 0.0;

                /* add the row to lpsolve */
                lp.addConstraintex(playerActions.length, row, colno, LpSolve.EQ, 1.0);

                Arrays.fill(row, 0);

                row[playerActions.length] = 1.0;

                /* set the objective in lpsolve */
                lp.setObjFnex(playerActions.length+1, row, colno);
                
            }

            if (ret == 0)

            {
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
            if (lp.getLp() != 0)

            {
                lp.deleteLp();
            }
        } catch (LpSolveException e) {
            throw new RuntimeException(e);
        }

        return slack < SLACK_TOLERANCE;
    }

    protected static List<SymmetricOutcome> createSubgameOutcomes(Player player, SymmetricMultiAgentSystem simulation) {
        List<SymmetricOutcome> subOutcomes = new ArrayList<SymmetricOutcome>();

        for (SymmetricOutcome o : Games.symmetricTraversal(createPlayerReducedSymmetricSimulation(player, simulation))) {
            subOutcomes.add(o);
        }

        return subOutcomes;
    }
}
