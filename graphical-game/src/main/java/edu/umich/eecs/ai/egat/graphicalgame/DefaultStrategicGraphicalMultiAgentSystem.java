/*
 * DefaultStrategicGraphicalMultiAgentSystem.java
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
package edu.umich.eecs.ai.egat.graphicalgame;

import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.DefaultStrategicMultiAgentSystem;
import edu.umich.eecs.ai.egat.game.DefaultMultiAgentSystem;
import edu.umich.eecs.ai.egat.graphicalgame.graphs.Graph;
import edu.umich.eecs.ai.egat.graphicalgame.graphs.BasicSparseGraph;
import edu.umich.eecs.ai.egat.graphicalgame.graphs.MutableGraph;

/**
 * @author Patrick Jordan
 */
public class DefaultStrategicGraphicalMultiAgentSystem extends DefaultStrategicMultiAgentSystem implements GraphicalMultiAgentSystem {
    private MutableGraph<Player> graph;

    public DefaultStrategicGraphicalMultiAgentSystem() {
        this.graph = new BasicSparseGraph<Player>();
    }

    public DefaultStrategicGraphicalMultiAgentSystem(final String name) {
        super(name);
        this.graph = new BasicSparseGraph<Player>();
    }

    public DefaultStrategicGraphicalMultiAgentSystem(final String name, final String description) {
        super(name, description);
        this.graph = new BasicSparseGraph<Player>();
    }

    public MutableGraph<Player> getGraph() {
        return graph;
    }

    @Override
    protected final void afterAddPlayer(final Player player) {
        beforeAddPlayerToGraph(player);

        if(graph.addNode(player)) {
            afterAddPlayerToGraph(player);
        }
    }

    protected void beforeAddPlayerToGraph(Player player) {
    }

    protected void afterAddPlayerToGraph(Player player) {
    }


    @Override
    protected final void afterRemovePlayer(final Player player) {
        beforeRemovePlayerFromGraph(player);
        if(graph.removeNode(player)) {
            afterRemovePlayerFromGraph(player);
        }
    }

    protected void beforeRemovePlayerFromGraph(Player player) {        
    }

    protected void afterRemovePlayerFromGraph(Player player) {
    }

    @Override
    public DefaultStrategicGraphicalMultiAgentSystem clone() throws CloneNotSupportedException {
        DefaultStrategicGraphicalMultiAgentSystem mas = (DefaultStrategicGraphicalMultiAgentSystem) super.clone();

        mas.graph = graph.clone();

        return mas;
    }
}
