/*
 * EmpiricalPayoffValue.java
 *
 * Copyright (C) 2006-2010 Patrick R. Jordan
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
package egat.game;

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
