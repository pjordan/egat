package edu.umich.eecs.ai.egat.mincurb;

import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.Strategy;

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

    protected SymmetricGame findMinimumCurb(Action seed, SymmetricGame game) {
        SymmetricGame currentCurb = mccFinder.findMinContainingCurb(seed, game);
        
        List<Action> remainingActions = new ArrayList<Action>(game.getActions());
        remainingActions.remove(seed);

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
        Map<Set<Action>, SymmetricGame> games = new HashMap<Set<Action>, SymmetricGame>();

        Set<SymmetricGame> minCurbs = new HashSet<SymmetricGame>();

        minCurbs.add(game);

        for (Action action : game.getActions()) {

            SymmetricGame curbGame = curbs.get(action);

            if (curbGame == null) {
                curbGame = game;
            }

            if (curbGame.getActions().contains(action)) {
                SymmetricGame reducedGame = findMinimumCurb(action, curbGame);

                if(!games.containsKey(reducedGame.getActions())) {
                    games.put(reducedGame.getActions(), reducedGame);        
                }

                reducedGame = games.get(reducedGame.getActions());


                if (reducedGame.getActions().size() < curbGame.getActions().size()) {
                    if (minCurbs.contains(curbGame)) {
                        minCurbs.remove(curbGame);
                    }

                    if (!minCurbs.contains(reducedGame)) {
                        minCurbs.add(reducedGame);
                    }

                    for (Action a : reducedGame.getActions()) {
                        if (curbs.get(a) == null || reducedGame.getActions().size() < curbs.get(a).getActions().size()) {
                            curbs.put(a, reducedGame);
                        }
                    }
                }
            }
        }


        return minCurbs;
    }
}
