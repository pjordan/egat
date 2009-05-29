package edu.umich.eecs.ai.egat.egame;

/**
 * @author Patrick Jordan
 */
public class ErrorStats {
    private double sum;
    private double sumSquared;
    private double count;


    public ErrorStats() {
        sum = 0.0;
        count = 0.0;
    }

    public void addError(double error) {
        sum += error;
        sumSquared += error*error;
        count++;
    }

    public double mse() {
        return sum / count;
    }

    public double var() {
        return sumSquared / count - sum*sum/(count * count);
    }

    public double std() {
        return Math.sqrt(var());
    }
    
    public double rmse() {
        return Math.sqrt(mse());
    }
}
