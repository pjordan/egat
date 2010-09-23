/*
 * RegressionStatistics.java
 *
 * Copyright (C) 2006-2009 Patrick R. Jordan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package egat.egame;

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
