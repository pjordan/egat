package edu.umich.eecs.ai.egat.minform;

import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Action;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class LpSolveMinimumFormationFinder implements MinimumFormationFinder {
    private LpSolveMinimumContainingFormationFinder mccFinder;

    public LpSolveMinimumFormationFinder() {
        mccFinder = new LpSolveMinimumContainingFormationFinder();
    }

    public SymmetricGame findMinimumFormation(SymmetricGame game) {
        SymmetricGame currentFormation = game;

        List<Action> remainingActions = new ArrayList<Action>(game.getActions());

        boolean flag = true;

        while (flag) {
            flag = false;

            for (Action a : currentFormation.getActions()) {
                if (remainingActions.contains(a)) {
                    remainingActions.remove(a);

                    SymmetricGame candidate = mccFinder.findMinContainingFormation(a, currentFormation);

                    if (candidate.getActions().size() < currentFormation.getActions().size()) {
                        currentFormation = candidate;

                        flag = true;

                        break;
                    }
                }
            }
        }

        return currentFormation;
    }

    public Set<SymmetricGame> findAllMinimumFormations(SymmetricGame game) {
        Map<Action, SymmetricGame> formations = new HashMap<Action, SymmetricGame>();

        Set<SymmetricGame> minFormations = new HashSet<SymmetricGame>();

        for (Action action : game.getActions()) {

            SymmetricGame formationGame = formations.get(action);

            if (formationGame == null) {
                formationGame = game;
            }

            if (formationGame.getActions().contains(action)) {
                SymmetricGame reducedGame = mccFinder.findMinContainingFormation(action, formationGame);

                if (reducedGame.getActions().size() < formationGame.getActions().size()) {

                    for (Action a : formationGame.getActions()) {
                        formations.put(a, reducedGame);
                    }

                    minFormations.remove(formationGame);
                    minFormations.add(reducedGame);
                }

            }
        }


        return minFormations;
    }
}
