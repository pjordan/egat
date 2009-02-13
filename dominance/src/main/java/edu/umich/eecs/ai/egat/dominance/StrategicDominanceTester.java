package edu.umich.eecs.ai.egat.dominance;

import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.StrategicGame;
import edu.umich.eecs.ai.egat.game.Strategy;
import edu.umich.eecs.ai.egat.game.Player;

/**
 * @author Patrick Jordan
 */
public interface StrategicDominanceTester {
    boolean isDominated(Player player, Action action, StrategicGame game);
    boolean isDominated(Player player, Strategy strategy, StrategicGame game);
}
