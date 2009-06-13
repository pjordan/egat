package edu.umich.eecs.ai.egat.game;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class DefaultStrategicGame<T extends PayoffValue> extends AbstractStrategicGame<T> implements MutableStrategicGame<T> {
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
        Payoff<T> payoff = outcomeMap.get(outcome);

        if(payoff==null)
            throw new NonexistentPayoffException(outcome);

        return payoff;
    }

    public void build() {
        outcomeMap.build(this);
    }

    public void putPayoff(Outcome outcome, Payoff<T> payoff) {
        outcomeMap.put(outcome,payoff);        
    }

    public void removeAction(Player player, Action action) {
        playerActions.get(player).remove(action);
    }

    public void addAction(Player player, Action action) {
        playerActions.get(player).add(action);
    }
}
