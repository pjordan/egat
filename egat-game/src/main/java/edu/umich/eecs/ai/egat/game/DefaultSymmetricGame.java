package edu.umich.eecs.ai.egat.game;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class DefaultSymmetricGame<T extends PayoffValue> extends AbstractStrategicGame<T> implements SymmetricGame<T> {
    private Set<Action> actions;
    private Map<Object, Map<Action,T>> outcomePayoffs;


    public DefaultSymmetricGame() {
        actions = new HashSet<Action>();
        outcomePayoffs = new HashMap<Object, Map<Action,T>>();
    }

    public DefaultSymmetricGame(String name) {
        super(name);
        actions = new HashSet<Action>();
        outcomePayoffs = new HashMap<Object, Map<Action,T>>();
    }

    public DefaultSymmetricGame(String name, String description) {
        super(name, description);
        actions = new HashSet<Action>();
        outcomePayoffs = new HashMap<Object, Map<Action,T>>();
    }

    public Set<Action> getActions() {
        return actions;
    }

    public Set<Action> getActions(Player player) {
        return getActions();
    }

    public SymmetricPayoff<T> payoff(Outcome outcome) {
        SymmetricOutcome so = Games.createSymmetricOutcome(outcome);

        if (so==null)
            return null;
        
        Map<Action,T> payoffs = outcomePayoffs.get(so.actionEntrySet());

        if( payoffs==null)
            return null;

        return PayoffFactory.createSymmetricPayoff(payoffs,outcome);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

    public void addAllActions(Collection<? extends Action> actions) {
        this.actions.addAll(actions);
    }

    public void clearActions() {
        actions.clear();
    }

    public void putPayoff(Outcome outcome, Map<Action,T> values) {
        SymmetricOutcome so = Games.createSymmetricOutcome(outcome);

        outcomePayoffs.put(so.actionEntrySet(),values);
    }
}
