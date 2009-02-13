package edu.umich.eecs.ai.egat.dominance;

import edu.umich.eecs.ai.egat.game.*;
import static edu.umich.eecs.ai.egat.dominance.DominanceUtils.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

/**
 * @author Patrick Jordan
 */
public class MixedSymmetricDominanceTesterImpl implements SymmetricDominanceTester {
    public static final double SLACK_TOLERANCE = 0.999999;

    public boolean isDominated(Action action, SymmetricGame game) {
        Player[] players = game.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

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
        int[] colno = new int[playerActions.length];

        for (int i = 0; i < playerActions.length; i++) {
            colno[i] = i + 1;
        }

        double[] row = new double[playerActions.length];

        try {
            LpSolve lp = LpSolve.makeLp(0, playerActions.length);

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
            }


            for (int i = 0; i < subGameSize; i++) {
                double payoffAction = getPayoff(action, subOutcomes.get(i), players, actions, game, playerIndex);

                for (int j = 0 ; j < playerActions.length; j++) {

                    double payoffDeviant = getPayoff(playerActions[j], subOutcomes.get(i), players, actions, game, playerIndex);

                    row[j] = payoffDeviant;
                }

                if (ret == 0) {
                    /* add the row to lpsolve */
                    lp.addConstraintex(playerActions.length, row, colno, LpSolve.GE, payoffAction);
                }
            }


            if (ret == 0) {
                lp.setAddRowmode(false); /* rowmode should be turned off again when done building the model */


                Arrays.fill(row, 1.0);

                /* set the objective in lpsolve */
                lp.setObjFnex(playerActions.length, row, colno);
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
        int[] colno = new int[playerActions.length];

        for (int i = 0; i < playerActions.length; i++) {
            colno[i] = i + 1;
        }

        double[] row = new double[playerActions.length];

        try {
            LpSolve lp = LpSolve.makeLp(0, playerActions.length);

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
            }


            for (int i = 0; i < subGameSize; i++) {
                double payoffAction = getPayoff(strategy, subOutcomes.get(i), players, strategies, game, playerIndex);

                for (int j = 0 ; j < playerActions.length; j++) {

                    double payoffDeviant = getPayoff(Games.createPureStrategy(playerActions[j]), subOutcomes.get(i), players, strategies, game, playerIndex);

                    row[j] = payoffDeviant;
                }

                if (ret == 0) {
                    /* add the row to lpsolve */
                    lp.addConstraintex(playerActions.length, row, colno, LpSolve.GE, payoffAction);
                }
            }


            if (ret == 0) {
                lp.setAddRowmode(false); /* rowmode should be turned off again when done building the model */


                Arrays.fill(row, 1.0);

                /* set the objective in lpsolve */
                lp.setObjFnex(playerActions.length, row, colno);
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

    protected static List<SymmetricOutcome> createSubgameOutcomes(Player player, SymmetricSimulation simulation) {
        List<SymmetricOutcome> subOutcomes = new ArrayList<SymmetricOutcome>();

        for (SymmetricOutcome o : Games.symmetricTraversal(createPlayerReducedSymmetricSimulation(player, simulation))) {
            subOutcomes.add(o);
        }

        return subOutcomes;
    }
}
