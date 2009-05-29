package edu.umich.eecs.ai.egat.egame;

/**
 * @author Patrick Jordan
 */
public class SampleStatistics {
    private double sum;
    private double sumSquared;
    private double count;


    public SampleStatistics() {
        sum = 0.0;
        count = 0.0;
    }

    public void add(double sample) {
        sum += sample;
        sumSquared += sample*sample;
        count++;
    }

    public double mean() {
        return sum / count;
    }

    public double var() {
        return sumSquared / count - sum*sum/(count * count);
    }

    public double std() {
        return Math.sqrt(var());
    }

    public double getCount() {
        return count;
    }
}
