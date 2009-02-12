package edu.umich.eecs.ai.egat.mincurb;

import edu.umich.eecs.ai.egat.game.*;

import java.util.*;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

/**
 * @author Patrick Jordan
 */
public class BenischSymmetricAllRationalizableFinder implements SymmetricAllRationalizableFinder {

    public Set<Action> findRationalizable(Set<Action> actions, SymmetricGame game) {
        DefaultSymmetricSimulation subGame = new DefaultSymmetricSimulation();
        Set<Action> others = new HashSet<Action>(game.getActions());

        Player[] allPlayers = game.players().toArray(new Player[0]);
        Action[] playerActions = new Action[allPlayers.length];

        Set<Action> rationalizableActions = new HashSet<Action>();

        for (int i = 1; i < allPlayers.length; i++) {
            subGame.addPlayer(allPlayers[i]);
        }

        for (Action action : actions) {
            subGame.addAction(action);
            others.remove(action);
        }

        try {
            for (Action candidateAction : others) {
                if (isRationalizable(candidateAction, allPlayers, playerActions, subGame, game)) {
                    rationalizableActions.add(candidateAction);
                }
            }
        } catch (LpSolveException e) {
            throw new RuntimeException(e);
        }

        return rationalizableActions;
    }

    private boolean isRationalizable(Action candidateAction, Player[] allPlayers, Action[] playerActions, SymmetricSimulation subGame, SymmetricGame game) throws LpSolveException {
        LpSolve lp;

        int ret = 0;

        double slack = -1.0;

        List<SymmetricOutcome> subOutcomes = new ArrayList<SymmetricOutcome>();

        for (SymmetricOutcome o : Games.symmetricTraversal(subGame)) {
            subOutcomes.add(o);
        }

        int subGameSize = subOutcomes.size();


        /* create space large enough for one row */
        int[] colno = new int[subGameSize + 1];

        for (int i = 0; i < subGameSize + 1; i++) {
            colno[i] = i + 1;
        }

        double[] row = new double[subGameSize + 1];

        lp = LpSolve.makeLp(0, subGameSize+1);
        if (lp.getLp() == 0) {
            ret = 1; /* couldn't construct a new model... */
        } else {
            lp.setAddRowmode(true);  /* makes building the model faster if it is done rows by row */
        }


        if (ret == 0) {
            Arrays.fill(row,1.0);
            row[subGameSize] = 0.0;
            
            /* add the row to lpsolve */
            lp.addConstraintex(subGameSize+1, row, colno, LpSolve.EQ, 1.0);

            Arrays.fill(row,0.0);

            for(int i = 0 ; i < subGameSize; i++) {
                row[i] = 1.0;

                /* add the row to lpsolve */
                lp.addConstraintex(subGameSize+1, row, colno, LpSolve.GE, 0.0);

                row[i] = 0.0;
            }
        }

        row[subGameSize] = 1.0;
        
        for (Action a : game.getActions()) {
            if (!a.equals(candidateAction)) {
                for (int i = 0; i < subGameSize; i++) {
                    for (int j = 1; j < playerActions.length; j++) {
                        playerActions[j] = subOutcomes.get(i).getAction(allPlayers[j]);
                    }
                    playerActions[0] = candidateAction;
                    double payoffCandidate = game.payoff(Games.createSymmetricOutcome(allPlayers, playerActions)).getPayoff(allPlayers[0]).getValue();

                    playerActions[0] = a;
                    double payoffA = game.payoff(Games.createSymmetricOutcome(allPlayers, playerActions)).getPayoff(allPlayers[0]).getValue();

                    row[i] = payoffCandidate - payoffA;
                }

                if (ret == 0) {
                    /* add the row to lpsolve */
                    lp.addConstraintex(subGameSize+1, row, colno, LpSolve.GE, 0);
                }
            }
        }

        if (ret == 0) {
            lp.setAddRowmode(false); /* rowmode should be turned off again when done building the model */


            Arrays.fill(row, 0);

            row[subGameSize] = 1.0;

            /* set the objective in lpsolve */
            lp.setObjFnex(subGameSize+1, row, colno);
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
        if(lp.getLp() != 0) {
            lp.deleteLp();
        }

        return slack <= 0.0;
    }
}
