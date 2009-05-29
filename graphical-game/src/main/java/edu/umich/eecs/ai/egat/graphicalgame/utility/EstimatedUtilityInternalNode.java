package edu.umich.eecs.ai.egat.graphicalgame.utility;

import edu.umich.eecs.ai.egat.game.Profile;
import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.Outcome;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.io.Serializable;

/**
 * @author Patrick Jordan
 */
public class EstimatedUtilityInternalNode implements EstimatedUtilityNode, Cloneable, Serializable {
    private Player player;

    private Map<Action,EstimatedUtilityNode> children;

    public EstimatedUtilityInternalNode(Player player) {
        if(player==null) {
            throw new NullPointerException("Player cannot be null");
        }
        this.player = player;
        
        children = new HashMap<Action, EstimatedUtilityNode>();
    }

    public void addSample(Outcome outcome, double utility) {
        children.get(outcome.getAction(player)).addSample(outcome,utility);
    }

    public int sampleCount(Outcome outcome) {
        return children.get(outcome.getAction(player)).sampleCount(outcome);
    }

    public double meanUtility(Outcome outcome) {
        return children.get(outcome.getAction(player)).meanUtility(outcome);
    }

    public double utility(Outcome outcome) {
        return children.get(outcome.getAction(player)).utility(outcome);
    }

    public void setEstimatedUtilityChild(Action action, EstimatedUtilityNode utilityNode) {
        children.put(action, utilityNode);
    }

    public Collection<EstimatedUtilityNode> children() {
        return children.values();
    }

    public int decendantCount() {
        int count = 0;

        for(EstimatedUtilityNode child : children()) {
            count+= child.decendantCount()+1;
        }

        return count;
    }

    public void reset() {
        for(EstimatedUtilityNode node : children()) {
            node.reset();
        }
    }

    @Override
    public EstimatedUtilityInternalNode clone() {
        try {
            EstimatedUtilityInternalNode node  = (EstimatedUtilityInternalNode) super.clone();

            node.children = new HashMap<Action, EstimatedUtilityNode>();

            for(Action action : children.keySet()) {
                EstimatedUtilityNode child = children.get(action);
                if(child!=null) {
                    node.children.put(action, child.clone());
                }
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
