package edu.umich.eecs.ai.egat.graphicalgame;

import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.StrategicMultiAgentSystem;
import edu.umich.eecs.ai.egat.graphicalgame.graphs.Graph;

/**
 * @author Patrick Jordan
 */
public interface GraphicalMultiAgentSystem extends StrategicMultiAgentSystem {
    Graph<Player> getGraph();
}
