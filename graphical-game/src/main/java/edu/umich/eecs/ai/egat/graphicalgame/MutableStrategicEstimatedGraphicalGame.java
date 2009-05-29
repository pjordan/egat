package edu.umich.eecs.ai.egat.graphicalgame;

import edu.umich.eecs.ai.egat.game.MutableStrategicMultiAgentSystem;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.graphicalgame.utility.EstimatedUtilityFunction;

/**
 * @author Patrick Jordan
 */
public interface MutableStrategicEstimatedGraphicalGame extends StrategicEstimatedGraphicalGame, MutableStrategicMultiAgentSystem {
    public void setUtilityFunction(Player player, EstimatedUtilityFunction estimatedUtilityFunction);
}
