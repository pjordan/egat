package edu.umich.eecs.ai.egat.game;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class DefaultStrategicGame<T extends PayoffValue> extends AbstractStrategicGame<T> {
    private DefaultOutcomeMap<Payoff<T>> outcomeMap;
    private Map<Player, Set<Action>> playerActions;

    public DefaultStrategicGame() {
        outcomeMap = new DefaultOutcomeMap<Payoff<T>>();
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public DefaultStrategicGame(String name) {
        super(name);
        outcomeMap = new DefaultOutcomeMap<Payoff<T>>();
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public DefaultStrategicGame(String name, String description) {
        super(name, description);
        outcomeMap = new DefaultOutcomeMap<Payoff<T>>();
        playerActions = new HashMap<Player, Set<Action>>();
    }

    public void putActions(Player player, Set<Action> actions) {
        playerActions.put(player, actions);
    }

    public Set<Action> getActions(Player player) {
        return playerActions.get(player);
    }

    public Payoff<T> payoff(Outcome outcome) {
        return outcomeMap.get(outcome);
    }

    public void build() {
        outcomeMap.build(this);
    }

    public void putPayoff(Outcome outcome, Payoff<T> payoff) {
        outcomeMap.put(outcome,payoff);        
    }
}
