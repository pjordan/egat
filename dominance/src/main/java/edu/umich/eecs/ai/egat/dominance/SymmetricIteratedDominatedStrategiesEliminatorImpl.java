package edu.umich.eecs.ai.egat.dominance;

import edu.umich.eecs.ai.egat.game.*;

/**
 * @author Patrick Jordan
 */
public class SymmetricIteratedDominatedStrategiesEliminatorImpl implements SymmetricIteratedDominatedStrategiesEliminator {
    private SymmetricDominanceTester strategicDominanceTester;

    public SymmetricIteratedDominatedStrategiesEliminatorImpl() {
        this(new MixedSymmetricDominanceTesterImpl());
    }

    public SymmetricIteratedDominatedStrategiesEliminatorImpl(SymmetricDominanceTester strategicDominanceTester) {
        this.strategicDominanceTester = strategicDominanceTester;
    }

    public MutableSymmetricGame eliminateDominatedStrategies(MutableSymmetricGame game) {
        boolean flag = true;

        while (flag) {
            flag = false;

            Action[] actions = game.getActions().toArray(new Action[0]);
            for (Action action : actions) {
                if (strategicDominanceTester.isDominated(action, game)) {
                    flag = true;

                    game.removeAction(action);
                }
            }
        }

        return game;
    }
}
