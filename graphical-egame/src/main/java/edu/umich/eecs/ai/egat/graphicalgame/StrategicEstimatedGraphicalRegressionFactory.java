/*
 * StrategicEstimatedGraphicalRegressionFactory.java
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
package edu.umich.eecs.ai.egat.graphicalgame;

import edu.umich.eecs.ai.egat.egame.StrategicRegressionFactory;
import edu.umich.eecs.ai.egat.egame.StrategicRegression;
import edu.umich.eecs.ai.egat.egame.StrategicSimulationObserver;
import edu.umich.eecs.ai.egat.egame.SimpleStrategicRegression;
import edu.umich.eecs.ai.egat.game.Games;
import edu.umich.eecs.ai.egat.game.Outcome;
import edu.umich.eecs.ai.egat.game.Payoff;

/**
 * @author Patrick Jordan
 */
public class StrategicEstimatedGraphicalRegressionFactory implements StrategicRegressionFactory {
    DefaultStrategicEstimatedGraphicalGame game;

    public StrategicEstimatedGraphicalRegressionFactory(DefaultStrategicEstimatedGraphicalGame game) {
        this.game = game;
    }

    public StrategicRegression regress(StrategicSimulationObserver observer) {

        try {
            StrategicEstimatedGraphicalGame rGame = game.clone();

            for(Outcome outcome : Games.traversal(observer.getStrategicSimulation())) {
                for(Payoff payoff : observer.getObservations(outcome)) {
                    rGame.addSample(outcome, payoff);
                }
            }

            return new SimpleStrategicRegression(rGame);

        } catch (CloneNotSupportedException e) {

            throw new RuntimeException(e);
            
        }
    }
}
