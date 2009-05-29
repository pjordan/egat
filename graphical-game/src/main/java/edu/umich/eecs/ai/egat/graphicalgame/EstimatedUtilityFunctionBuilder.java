package edu.umich.eecs.ai.egat.graphicalgame;

import edu.umich.eecs.ai.egat.graphicalgame.utility.EstimatedUtilityLeaf;
import edu.umich.eecs.ai.egat.graphicalgame.utility.EstimatedUtilityInternalNode;
import edu.umich.eecs.ai.egat.graphicalgame.utility.EstimatedUtilityNode;
import edu.umich.eecs.ai.egat.graphicalgame.graphs.Graph;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.StrategicMultiAgentSystem;

import java.util.Stack;

/**
 * @author Patrick Jordan
 */
public class EstimatedUtilityFunctionBuilder {

    public EstimatedUtilityNode build(Player player, StrategicMultiAgentSystem simulation, Graph<Player> graph) {

        Stack<Player> stack = new Stack<Player>();


        for(Player otherPlayer : simulation.players()) {

            if(graph.hasEdge(player, otherPlayer)) {

                stack.push(otherPlayer);

            }

        }

        return recursiveBuild(stack,simulation);

    }

    public EstimatedUtilityNode build(Player player, GraphicalMultiAgentSystem simulation) {

        return build(player, simulation, simulation.getGraph());

    }

    private EstimatedUtilityNode recursiveBuild(Stack<Player> stack, StrategicMultiAgentSystem simulation) {

        if(stack.isEmpty()) {

            return new EstimatedUtilityLeaf();

        } else {

            Stack<Player> copy = (Stack<Player>) stack.clone();

            Player player = copy.pop();

            EstimatedUtilityInternalNode node = new EstimatedUtilityInternalNode(player);
            
            for(Action action : simulation.getActions(player)) {

                node.setEstimatedUtilityChild(action, recursiveBuild(copy, simulation) );

            }

            return node;

        }

    }
}
