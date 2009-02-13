package edu.umich.eecs.ai.egat.dominance;

import edu.umich.eecs.ai.egat.game.MutableStrategicGame;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Action;

/**
 * @author Patrick Jordan
 */
public class StrategicIteratedDominatedStrategiesEliminatorImpl implements StrategicIteratedDominatedStrategiesEliminator {
    private StrategicDominanceTester strategicDominanceTester;

    public StrategicIteratedDominatedStrategiesEliminatorImpl() {
        this(new MixedStrategicDominanceTesterImpl());
    }

    public StrategicIteratedDominatedStrategiesEliminatorImpl(StrategicDominanceTester strategicDominanceTester) {
        this.strategicDominanceTester = strategicDominanceTester;
    }

    public MutableStrategicGame eliminateDominatedStrategies(MutableStrategicGame game) {
        boolean flag = true;

        while(flag) {
            flag = false;

            for(Player player : game.players()) {
                Action[] actions = game.getActions(player).toArray(new Action[0]);
                for(Action action : actions) {
                    if(strategicDominanceTester.isDominated(player,action,game)) {
                        flag = true;

                        game.removeAction(player, action);
                    }
                }
            }
        }

        return game;
    }
}
