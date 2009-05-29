package edu.umich.eecs.ai.egat.graphicalgame.utility;

import edu.umich.eecs.ai.egat.game.Profile;
import edu.umich.eecs.ai.egat.game.Outcome;

/**
 * @author Patrick Jordan
 */
public interface EstimatedUtilityFunction extends UtilityFunction, Cloneable {
    void addSample(Outcome outcome, double utility);
    int sampleCount(Outcome outcome);
    double meanUtility(Outcome outcome);
    void reset();
    EstimatedUtilityFunction clone();
}
