package edu.umich.eecs.ai.egat.graphicalgame.utility;

import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.Outcome;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

/**
 * @author Patrick Jordan
 */
public class UtilityInternalNode implements UtilityNode {
    private Player player;

    private Map<Action,UtilityNode> children;

    public UtilityInternalNode(Player player) {
        if(player==null) {
            throw new NullPointerException("Player cannot be null");
        }
        this.player = player;

        children = new HashMap<Action, UtilityNode>();
    }

    public double utility(Outcome outcome) {
        return children.get(outcome.getAction(player)).utility(outcome);
    }

    public void setUtilityChild(Action action, UtilityNode utilityNode) {
        children.put(action, utilityNode);
    }

    public Collection<UtilityNode> children() {
        return children.values();
    }

    public int decendantCount() {
        int count = 0;

        for(UtilityNode child : children()) {
            count+= child.decendantCount()+1;
        }

        return count;
    }
}
