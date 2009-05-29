package edu.umich.eecs.ai.egat.graphicalgame.utility;

import edu.umich.eecs.ai.egat.game.Outcome;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Patrick Jordan
 */
public class UtilityLeaf implements UtilityNode {
    private double utility;

    public UtilityLeaf(double utility) {
        this.utility = utility;
    }

    public double utility(Outcome outcome) {
        return utility;
    }

    public Collection<UtilityNode> children() {
        return Collections.EMPTY_LIST;
    }

    public int decendantCount() {
        return 0;
    }
}
