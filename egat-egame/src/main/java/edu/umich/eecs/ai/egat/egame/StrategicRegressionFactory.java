package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.StrategicGame;

/**
 * @author Patrick Jordan
 */
public interface StrategicRegressionFactory {
    StrategicRegression regress(StrategicSimulationObserver observer);
}
