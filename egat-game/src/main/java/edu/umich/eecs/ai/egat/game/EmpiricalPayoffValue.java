/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

/**
 * The empirical payoff value class is an extension of the {@link PayoffValue payoff value}
 * class that includes sample statistics.
 *
 * @author Patrick Jordan
 */
public interface EmpiricalPayoffValue extends PayoffValue {
    /**
     * Returns the sample size for the payoff value.
     * @return the sample size for the payoff value.
     */
    int getSampleSize();

    /**
     * Returns the standard deviation of the payoff value.
     * @return the standard deviation of the payoff value.
     */
    double getStandardDeviation();

    /**
     * Returns the mean of the payoff value.
     * @return the mean of the payoff value.
     */
    double getMean();
}
