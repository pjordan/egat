package edu.umich.eecs.ai.egat.graphicalgame;

import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.graphicalgame.graphs.BasicSparseGraph;

import java.util.Set;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class StrategicEstimatedGraphicalGameBuilder {
    private EstimatedUtilityFunctionBuilder utilityFunctionBuilder;
    private DefaultStrategicEstimatedGraphicalGame game;

    public StrategicEstimatedGraphicalGameBuilder() {
        utilityFunctionBuilder = new EstimatedUtilityFunctionBuilder();
        game = new DefaultStrategicEstimatedGraphicalGame();
    }

    public boolean addEdge(Player from, Player to) {
        return game.getGraph().addEdge(from, to);
    }

    public boolean removeEdge(Player from, Player to) {
        return game.getGraph().removeEdge(from, to);
    }

    public void addPlayer(Player node) {
        game.addPlayer(node);
    }

    public void removePlayer(Player node) {
        game.removePlayer(node);
    }

    public Set<Player> players() {
        return game.players();
    }

    public Map<Player, Set<Player>> edgeLists() {
        return game.getGraph().edgeLists();
    }

    public DefaultStrategicEstimatedGraphicalGame build() {

        for(Player player : players()) {
            game.setUtilityFunction(player, utilityFunctionBuilder.build(player, game));
        }

        game.build();

        return game;
    }

    public void addAction(Player player, Action action) {
        game.addAction(player, action);
    }

    public void addRemove(Player player, Action action) {
        game.removeAction(player, action);
    }
}
