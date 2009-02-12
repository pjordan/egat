package edu.umich.eecs.ai.egat.mincurb;

import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Action;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class BenischMinimumCurbFinder implements MinimumCurbFinder {
    private BenischMinimumContainingCurbFinder mccFinder;

    public BenischMinimumCurbFinder() {
        mccFinder = new BenischMinimumContainingCurbFinder();
    }

    public SymmetricGame findMinimumCurb(SymmetricGame game) {
        SymmetricGame currentCurb = game;

        List<Action> remainingActions = new ArrayList<Action>(game.getActions());

        boolean flag = true;

        while (flag) {
            flag = false;

            for (Action a : currentCurb.getActions()) {
                if (remainingActions.contains(a)) {
                    remainingActions.remove(a);

                    SymmetricGame candidate = mccFinder.findMinContainingCurb(a, currentCurb);

                    if (candidate.getActions().size() < currentCurb.getActions().size()) {
                        currentCurb = candidate;

                        flag = true;

                        break;
                    }
                }
            }
        }

        return currentCurb;
    }

    public Set<SymmetricGame> findAllMinimumCurbs(SymmetricGame game) {
        Map<Action, SymmetricGame> curbs = new HashMap<Action, SymmetricGame>();

        Set<SymmetricGame> minCurbs = new HashSet<SymmetricGame>();

        for (Action action : game.getActions()) {

            SymmetricGame curbGame = curbs.get(action);

            if (curbGame == null) {
                curbGame = game;
            }

            if (curbGame.getActions().contains(action)) {
                SymmetricGame reducedGame = mccFinder.findMinContainingCurb(action, curbGame);

                if (reducedGame.getActions().size() < curbGame.getActions().size()) {

                    for (Action a : curbGame.getActions()) {
                        curbs.put(a, reducedGame);
                    }

                    minCurbs.remove(curbGame);
                    minCurbs.add(reducedGame);
                }

            }
        }


        return minCurbs;
    }
}
