package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.Payoff;
import edu.umich.eecs.ai.egat.game.SymmetricOutcome;
import edu.umich.eecs.ai.egat.game.SymmetricPayoff;

/**
 * @author Patrick Jordan
 */
public interface BasicStatsSymmetricSimulationObserver extends SymmetricSimulationObserver {
    SymmetricPayoff meanObservationPayoff(SymmetricOutcome outcome);
    int observationCount(SymmetricOutcome outcome);
}
