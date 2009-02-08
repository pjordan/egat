package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.Payoff;
import edu.umich.eecs.ai.egat.game.Outcome;

/**
 * @author Patrick Jordan
 */
public interface BasicStatsStrategicSimulationObserver extends StrategicSimulationObserver {
    Payoff meanObservationPayoff(Outcome outcome);
    int observationCount(Outcome outcome);
}
