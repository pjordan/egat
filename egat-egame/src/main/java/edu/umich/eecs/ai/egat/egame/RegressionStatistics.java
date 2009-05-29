package edu.umich.eecs.ai.egat.egame;

/**
 * @author Patrick Jordan
 */
public class RegressionStatistics extends SampleStatistics {
    private double biasSquaredSum;
    private double biasCount;
    private double errorSquaredSum;
    private double errorCount;

    public RegressionStatistics() {
    }

    public double getBiasSquared() {
        return biasSquaredSum /biasCount;
    }

    public double getBiasCount() {
        return biasCount;
    }

    public void addBias(double bias) {
        biasSquaredSum += bias*bias;
        biasCount++;
    }

    public double getErrorSquared() {
        return errorSquaredSum /errorCount;
    }

    public double getErrorCount() {
        return errorCount;
    }

    public void addError(double error) {
        errorSquaredSum += error*error;
        errorCount++;
    }

    public double calculateVariance(double noiseVariance) {
        return getErrorSquared() - getBiasSquared() - noiseVariance;
    }
}
