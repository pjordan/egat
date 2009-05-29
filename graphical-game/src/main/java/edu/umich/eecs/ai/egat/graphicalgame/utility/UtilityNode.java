package edu.umich.eecs.ai.egat.graphicalgame.utility;

import java.util.Collection;

/**
 * @author Patrick Jordan
 */
public interface UtilityNode extends UtilityFunction {
    Collection<UtilityNode> children();    
    int decendantCount();
}
