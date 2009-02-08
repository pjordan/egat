package edu.umich.eecs.ai.egat.egame;

/**
 * @author Patrick Jordan
 */
public interface SymmetricCrossValidator {

    public double crossValidate(SymmetricRegressionFactory regressionFactory, SymmetricSimulationObserver observer);

    
}
