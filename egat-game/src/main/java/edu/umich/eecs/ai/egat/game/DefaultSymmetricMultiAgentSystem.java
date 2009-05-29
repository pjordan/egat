package edu.umich.eecs.ai.egat.game;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * @author Patrick Jordan
 */
public class DefaultSymmetricMultiAgentSystem extends DefaultMultiAgentSystem implements MutableSymmetricMultiAgentSystem {
    private Set<Action> actions;


    public DefaultSymmetricMultiAgentSystem() {
        actions = new HashSet<Action>();
    }

    public DefaultSymmetricMultiAgentSystem(String name) {
        super(name);
        actions = new HashSet<Action>();
    }

    public DefaultSymmetricMultiAgentSystem(String name, String description) {
        super(name, description);
        actions = new HashSet<Action>();
    }

    public DefaultSymmetricMultiAgentSystem(String name, String description, Set<Player> players) {
        super(name, description, players);
        actions = new HashSet<Action>();
    }

    public DefaultSymmetricMultiAgentSystem(Set<Player> players) {
        super(players);
        actions = new HashSet<Action>();
    }


    public Set<Action> getActions() {
        return actions;
    }

    public Set<Action> getActions(Player player) {
        return getActions();
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
}
