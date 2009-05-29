package edu.umich.eecs.ai.egat.minform;

import edu.umich.eecs.ai.egat.game.*;

import java.util.*;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

/**
 * @author Patrick Jordan
 */
public class LpSolveSymmetricRationalizableFinder implements SymmetricRationalizableFinder {

    public Set<Action> findRationalizable(Set<Action> actions, SymmetricGame game) {
        MutableSymmetricMultiAgentSystem subGame = new DefaultSymmetricMultiAgentSystem();
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

    public Action findMaxRationalizable(Set<Action> actions, SymmetricGame game) {
        Set<Action> others = new HashSet<Action>(game.getActions());

        for (Action action : actions) {
            others.remove(action);
        }

        return findMaxRationalizable(actions,others,game);
    }

    public Action findMaxRationalizable(Set<Action> actions, Set<Action> remaining, SymmetricGame game) {
        MutableSymmetricMultiAgentSystem subGame = new DefaultSymmetricMultiAgentSystem();        

        Player[] allPlayers = game.players().toArray(new Player[0]);
        Action[] playerActions = new Action[allPlayers.length];

        Action maxRationalizable = null;
        double maxSlack = Double.NEGATIVE_INFINITY;

        for (int i = 1; i < allPlayers.length; i++) {
            subGame.addPlayer(allPlayers[i]);
        }

        for (Action action : actions) {
            subGame.addAction(action);
        }

        try {
            for (Action candidateAction : remaining) {
                double slack = -rationalizableSlack(candidateAction, allPlayers, playerActions, subGame, game);
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

    private boolean isRationalizable(Action candidateAction, Player[] allPlayers, Action[] playerActions, SymmetricMultiAgentSystem subGame, SymmetricGame game) throws LpSolveException {
        return rationalizableSlack(candidateAction,allPlayers,playerActions,subGame,game) <= 0.0;
    }


    public double rationalizableSlack(Action candidateAction, Set<Action> actions, SymmetricGame game) {
        MutableSymmetricMultiAgentSystem subGame = new DefaultSymmetricMultiAgentSystem();

        Player[] allPlayers = game.players().toArray(new Player[0]);
        Action[] playerActions = new Action[allPlayers.length];
        
        for (int i = 1; i < allPlayers.length; i++) {
            subGame.addPlayer(allPlayers[i]);
        }

        for (Action action : actions) {
            subGame.addAction(action);
        }

        double slack = Double.NaN;

        try {
            slack = -rationalizableSlack(candidateAction, allPlayers, playerActions, subGame, game);
        } catch (LpSolveException e) {
            throw new RuntimeException(e);
        }

        return slack;
    }

    public double rationalizableRestrictedSlack(Action candidateAction, Set<Action> actions, SymmetricGame game) {
        MutableSymmetricMultiAgentSystem subGame = new DefaultSymmetricMultiAgentSystem();

        Player[] allPlayers = game.players().toArray(new Player[0]);
        Action[] playerActions = new Action[allPlayers.length];

        for (int i = 1; i < allPlayers.length; i++) {
            subGame.addPlayer(allPlayers[i]);
        }

        for (Action action : actions) {
            subGame.addAction(action);
        }

        double slack = Double.NaN;

        try {
            slack = -rationalizableRestrictedSlack(candidateAction, allPlayers, playerActions, subGame, game);
        } catch (LpSolveException e) {
            throw new RuntimeException(e);
        }

        return slack;
    }

    private double rationalizableSlack(Action candidateAction, Player[] allPlayers, Action[] playerActions, SymmetricMultiAgentSystem subGame, SymmetricGame game) throws LpSolveException {
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

            lp.setLowbo(subGameSize+1,Double.NEGATIVE_INFINITY);
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

        return slack;
    }

    private double rationalizableRestrictedSlack(Action candidateAction, Player[] allPlayers, Action[] playerActions, SymmetricMultiAgentSystem subGame, SymmetricGame game) throws LpSolveException {
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
            lp.addConstraintex(subGameSize, row, colno, LpSolve.EQ, 1.0);

            Arrays.fill(row,0.0);

            for(int i = 0 ; i < subGameSize; i++) {
                row[i] = 1.0;

                /* add the row to lpsolve */
                lp.addConstraintex(subGameSize, row, colno, LpSolve.GE, 0.0);

                row[i] = 0.0;
            }

            lp.setLowbo(subGameSize+1,Double.NEGATIVE_INFINITY);
        }

        row[subGameSize] = 1.0;

        for (Action a : subGame.getActions()) {
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

        return slack;
    }
}
