package edu.umich.eecs.ai.egat.egame;

/**
 * @author Patrick Jordan
 */
public interface SymmetricRegressionFactory {
    SymmetricRegression regress(SymmetricSimulationObserver observer);
}
