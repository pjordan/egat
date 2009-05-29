package edu.umich.eecs.ai.egat.graphicalgame.utility;

import edu.umich.eecs.ai.egat.game.Profile;
import edu.umich.eecs.ai.egat.game.Outcome;

import java.util.Collection;
import java.util.Collections;
import java.io.Serializable;

/**
 * @author Patrick Jordan
 */
public class EstimatedUtilityLeaf implements EstimatedUtilityNode, Cloneable, Serializable {
    double sum = 0.0;
    int count = 0;

    public void addSample(Outcome outcome, double utility) {
        sum+= utility;
        count++;
    }

    public int sampleCount(Outcome outcome) {
        return count;
    }

    public double meanUtility(Outcome outcome) {
        return sum / count;
    }

    public double utility(Outcome outcome) {
        return count > 0 ? meanUtility(outcome) : sum;
    }

    public Collection<EstimatedUtilityNode> children() {
        return Collections.EMPTY_LIST;
    }

    public int decendantCount() {
        return 0;  
    }

    public void reset() {
        sum = 0.0;
        count = 0;
    }

    @Override
    public EstimatedUtilityLeaf clone() {
        try {
            return (EstimatedUtilityLeaf)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
