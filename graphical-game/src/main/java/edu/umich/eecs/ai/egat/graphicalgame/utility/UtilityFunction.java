package edu.umich.eecs.ai.egat.graphicalgame.utility;

import edu.umich.eecs.ai.egat.game.Profile;
import edu.umich.eecs.ai.egat.game.Outcome;

/**
 * @author Patrick Jordan
 */
public interface UtilityFunction {
    double utility(Outcome outcome);
}
