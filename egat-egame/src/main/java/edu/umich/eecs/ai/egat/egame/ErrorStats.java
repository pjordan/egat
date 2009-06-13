/*
 * AbstractIdentifiable.java
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
