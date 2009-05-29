package edu.umich.eecs.ai.egat.graphicalgame;

import edu.umich.eecs.ai.egat.graphicalgame.graphs.Graph;
import edu.umich.eecs.ai.egat.game.Player;

/**
 * @author Patrick Jordan
 */
public interface MutableGraphicalMultiAgentSystem extends GraphicalMultiAgentSystem {
    void setGraph(Graph<Player> graph);
}
