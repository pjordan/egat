/*
 * EstimatedUtilityFunctionBuilder.java
 *
 * Copyright (C) 2006-2009 Patrick R. Jordan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package egat.graphicalgame;

import egat.graphicalgame.utility.EstimatedUtilityLeaf;
import egat.graphicalgame.utility.EstimatedUtilityInternalNode;
import egat.graphicalgame.utility.EstimatedUtilityNode;
import egat.graphicalgame.graphs.Graph;
import egat.game.Player;
import egat.game.Action;
import egat.game.StrategicMultiAgentSystem;

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
