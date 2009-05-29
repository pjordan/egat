package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

/**
 * @author Patrick Jordan
 */
public interface SymmetricSimulator extends SymmetricMultiAgentSystem, StrategicSimulator {
    SymmetricPayoff simulate(Outcome outcome);
}
