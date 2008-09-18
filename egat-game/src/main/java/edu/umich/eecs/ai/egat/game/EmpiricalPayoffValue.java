/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.game;

/**
 * @author Patrick Jordan
 */
public interface EmpiricalPayoffValue extends PayoffValue {
    int getSampleSize();
    double getStandardDeviation();
    double getMean();
}
