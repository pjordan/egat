package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class SparseStrategicGame<T extends PayoffValue> extends AbstractStrategicGame<T> {
    private Map<Player,Set<Action>> playerActions;
    private Map<Outcome,Payoff<T>> outcomePayoffs;


    public SparseStrategicGame() {
        playerActions = new HashMap<Player,Set<Action>>();
        outcomePayoffs = new HashMap<Outcome,Payoff<T>>();
    }

    public SparseStrategicGame(String name) {
        super(name);
        playerActions = new HashMap<Player,Set<Action>>();
        outcomePayoffs = new HashMap<Outcome,Payoff<T>>();
    }

    public SparseStrategicGame(String name, String description) {
        super(name, description);
        playerActions = new HashMap<Player,Set<Action>>();
        outcomePayoffs = new HashMap<Outcome,Payoff<T>>();
    }

    public Set<Action> getActions(Player player) {
        return playerActions.get(player);
    }

    public Payoff<T> payoff(Outcome outcome) {
        return outcomePayoffs.get(outcome);
    }

    public void putActions(Player player, Set<Action> actions) {
        playerActions.put(player,actions);
    }

    public void putPayoff(Outcome outcome, Payoff<T> payoff) {
        outcomePayoffs.put(outcome,payoff);
    }
}
