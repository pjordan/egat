/*
 * PayoffMatrix.java
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
package edu.umich.eecs.ai.egat.replicatordynamics;

/**
 * @author Patrick R. Jordan
 */
public class PayoffMatrix {
    private double[] data;
    private int actions;
    private int totalSize;
    private int players;
    private int[][] indicesMap;
    private int[][] actionIndicesMap;

    public PayoffMatrix(int players, int actions) {
        this.actions = actions;
        this.players = players;

        totalSize = 1;
        for (int i = 0; i < players; i++) {
            totalSize *= actions;
        }

        data = new double[totalSize];
        indicesMap = new int[totalSize][players];

        for (int i = 0; i < totalSize; i++) {
            expand(i, indicesMap[i]);
        }

        actionIndicesMap = new int[actions][totalSize / actions];
        for (int i = 0; i < actions; i++) {
            createActionIndex(i, actionIndicesMap[i]);
        }
    }

    public double getPayoff(int index) {
        return data[index];
    }

    public void setPayoff(int index, double value) {
        data[index] = value;
    }

    public double getPayoff(int[] indices) {
        return data[flatten(indices)];
    }

    public void setPayoff(int[] indices, double value) {
        data[flatten(indices)] = value;
    }

    public double getProfilePayoff(double[] distribution) {
        double payoff = 0.0;

        for (int i = 0; i < totalSize; i++) {


            double probability = 1.0;

            for (int j = 0; j < players; j++) {
                probability *= distribution[indicesMap[i][j]];
            }

            payoff += data[i] * probability;
        }

        return payoff;
    }

    public double regret(double[] base) {

        double basePayoff = getProfilePayoff(base);

        double maxPayoff = basePayoff;

        for (int a = 0; a < actions; a++) {
            double payoff = getProfilePayoff(base, a);
            maxPayoff = Math.max(payoff, maxPayoff);
        }

        return maxPayoff - basePayoff;
    }

    public double regret(double[] base, double[] deviation) {

        double basePayoff = getProfilePayoff(base, deviation);

        double maxPayoff = basePayoff;

        for (int a = 0; a < actions; a++) {

            double payoff = getProfilePayoff(base, a);

            maxPayoff = Math.max(payoff, maxPayoff);
        }

        return maxPayoff - basePayoff;
    }

    public double getProfilePayoff(double[] base, double[] deviation) {

        double payoff = 0.0;

        for (int i = 0; i < totalSize; i++) {
            double probability = deviation[indicesMap[i][0]];

            for (int j = 1; j < players & probability > 0.0; j++) {
                probability *= base[indicesMap[i][j]];
            }

            payoff += data[i] * probability;
        }

        return payoff;
    }

    public double getProfilePayoff(double[] base, int deviation) {

        double payoff = 0.0;

        int[] indices = actionIndicesMap[deviation];
        for (int i = 0; i < indices.length; i++) {
            double probability = 1.0;

            for (int j = 1; j < players & probability > 0.0; j++) {
                probability *= base[indicesMap[indices[i]][j]];
            }

            payoff += data[indices[i]] * probability;

        }

        return payoff;
    }

    public double getProfilePayoff(double[][] distribution) {
        double payoff = 0.0;

        for (int i = 0; i < totalSize; i++) {

            double probability = 1.0;
            for (int j = 0; j < players & probability > 0.0; j++) {
                probability *= distribution[j][indicesMap[i][j]];
            }

            payoff += data[i] * probability;
        }

        return payoff;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public int flatten(int[] indices) {
        int index = 0;

        int multiplier = 1;

        for (int i = 0; i < players; i++) {
            index += indices[i] * multiplier;
            multiplier *= actions;
        }

        return index;
    }

    public double minPayoff() {
        double min = Double.POSITIVE_INFINITY;

        for(double d : data) {
            min = Math.min(d,min);
        }

        return min;
    }

    public double maxPayoff() {
        double max = Double.NEGATIVE_INFINITY;

        for(double d : data) {
            max = Math.max(d,max);
        }

        return max;
    }

    private void createActionIndex(int i, int[] indices) {
        int currentIndex = 0;

        for (int t = 0; t < totalSize; t++) {
            if (indicesMap[t][0] == i) {
                indices[currentIndex++] = t;
            }
        }
    }

    public void expand(int index, int[] indices) {
        for (int i = 0; i < players; i++) {
            indices[i] = index % actions;

            index /= actions;
        }
    }
}
