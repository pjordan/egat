package edu.umich.eecs.ai.egat.minform;

import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.SymmetricGame;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public interface SymmetricRationalizableFinder {
    Set<Action> findRationalizable(Set<Action> actions, SymmetricGame game);

    Action findMaxRationalizable(Set<Action> actions, SymmetricGame game);

    Action findMaxRationalizable(Set<Action> actions, Set<Action> others, SymmetricGame game);

    double rationalizableSlack(Action candidateAction, Set<Action> actions, SymmetricGame game);

    double rationalizableRestrictedSlack(Action candidateAction, Set<Action> actions, SymmetricGame game);
}
