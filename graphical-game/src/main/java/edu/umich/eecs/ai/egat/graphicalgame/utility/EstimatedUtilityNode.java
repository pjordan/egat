package edu.umich.eecs.ai.egat.graphicalgame.utility;

import java.util.Collection;

/**
 * @author Patrick Jordan
 */
public interface EstimatedUtilityNode extends EstimatedUtilityFunction {
    Collection<EstimatedUtilityNode> children();
    int decendantCount();
    EstimatedUtilityNode clone();
}
