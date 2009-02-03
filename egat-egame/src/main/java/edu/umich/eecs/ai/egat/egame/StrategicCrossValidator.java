package edu.umich.eecs.ai.egat.egame;

/**
 * @author Patrick Jordan
 */
public interface StrategicCrossValidator {

    public double crossValidate(StrategicRegressionFactory regressionFactory, StrategicSimulationObserver observer);

}
