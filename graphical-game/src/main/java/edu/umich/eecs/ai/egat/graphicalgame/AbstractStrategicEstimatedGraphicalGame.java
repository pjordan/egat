package edu.umich.eecs.ai.egat.graphicalgame;

import edu.umich.eecs.ai.egat.game.*;
import edu.umich.eecs.ai.egat.graphicalgame.utility.EstimatedUtilityFunction;
import edu.umich.eecs.ai.egat.graphicalgame.utility.EstimatedUtilityLeaf;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public abstract class AbstractStrategicEstimatedGraphicalGame extends DefaultStrategicGraphicalMultiAgentSystem implements MutableStrategicEstimatedGraphicalGame {

    private Map<Player, Set<Action>> playerActions;

    private Map<Player, EstimatedUtilityFunction> playerUtilityFunctions;

    public AbstractStrategicEstimatedGraphicalGame() {
        playerActions = new HashMap<Player, Set<Action>>();
        playerUtilityFunctions = new HashMap<Player, EstimatedUtilityFunction>();
    }

    public AbstractStrategicEstimatedGraphicalGame(final String name) {
        super(name);
        playerActions = new HashMap<Player, Set<Action>>();
        playerUtilityFunctions = new HashMap<Player, EstimatedUtilityFunction>();
    }

    public AbstractStrategicEstimatedGraphicalGame(final String name, final String description) {
        super(name, description);
        playerActions = new HashMap<Player, Set<Action>>();
        playerUtilityFunctions = new HashMap<Player, EstimatedUtilityFunction>();
    }

    public final Set<Action> getActions(final Player player) {
        return playerActions.get(player);
    }

    protected final Payoff computePayoff(final Outcome outcome) {
        Player[] players = outcome.players().toArray(new Player[0]);
        double[] payoffs = new double[players.length];

        for(int i = 0; i < players.length; i++) {
            EstimatedUtilityFunction utilityFunction = playerUtilityFunctions.get(players[i]);

            if(utilityFunction!=null) {
                payoffs[i] = utilityFunction.utility(outcome);
            } else {
                payoffs[i] = Double.NaN;
            }
        }

        return PayoffFactory.createPayoff(players, payoffs);
    }

    public final Payoff payoff(final Profile profile) {
        return Games.computeStrategicPayoff(profile, this);
    }

    public final void addSample(final Outcome outcome, final Payoff payoff) {

        for(Player player : playerUtilityFunctions.keySet()) {

            PayoffValue payoffValue = payoff.getPayoff(player);

            if(payoffValue!=null) {
                playerUtilityFunctions.get(player).addSample(outcome, payoffValue.getValue());
            }

        }

    }

    @Override
    public final void putActions(final Player player, final Set<Action> actions) {
        Set<Action> set = playerActions.get(player);
        set.clear();
        set.addAll(actions);
    }

    @Override
    public final void removeAction(final Player player, final Action action) {
        playerActions.get(player).remove(action);
    }

    @Override
    public final void addAction(final Player player, final Action action) {
        playerActions.get(player).add(action);
    }

    @Override
    protected final void afterAddPlayerToGraph(final Player player) {
        playerActions.put(player, new HashSet<Action>());
        playerUtilityFunctions.put(player, new EstimatedUtilityLeaf());
    }

    @Override
    protected final void afterRemovePlayerFromGraph(final Player player) {
        playerActions.remove(player);
        playerUtilityFunctions.remove(player);
    }

    public final void setUtilityFunction(final Player player, final EstimatedUtilityFunction estimatedUtilityFunction) {
        playerUtilityFunctions.put(player,  estimatedUtilityFunction);
    }

    public void reset() {
        for(EstimatedUtilityFunction function : playerUtilityFunctions.values()) {
            if(function!=null) {
                function.reset();
            }
        }
    }

    @Override
    public AbstractStrategicEstimatedGraphicalGame clone() throws CloneNotSupportedException {

        AbstractStrategicEstimatedGraphicalGame game = (AbstractStrategicEstimatedGraphicalGame) super.clone();

        game.playerActions = new HashMap<Player, Set<Action>>();

        game.playerUtilityFunctions = new HashMap<Player, EstimatedUtilityFunction>();

        for(Player player : players()) {

            Set<Action> actions = playerActions.get(player);

            if(actions!=null) {

                game.playerActions.put(player, new HashSet<Action>(actions));

            }

            EstimatedUtilityFunction function = playerUtilityFunctions.get(player);

            if(function!=null) {

                game.playerUtilityFunctions.put(player,function.clone());

            }
        }

        return game;
    }
}
