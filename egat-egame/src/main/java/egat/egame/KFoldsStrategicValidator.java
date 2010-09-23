/*
 * KFoldsStrategicValidator.java
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

import egat.game.*;

import java.util.Arrays;
import java.util.Random;
import java.util.Collections;

/**
 * @author Patrick Jordan
 */
public class KFoldsStrategicValidator implements StrategicCrossValidator {
    protected int folds;
    protected Random random;

    public KFoldsStrategicValidator() {
        this(10);
    }

    public KFoldsStrategicValidator(int folds) {
        this.folds = folds;
        random = new Random();
    }

    public double crossValidate(StrategicRegressionFactory regressionFactory, StrategicSimulationObserver observer) {

        ErrorStats stats = new ErrorStats();

        StrategicSimulationObserver[] observers = splitObservers(observer);

        for(int i = 0; i < observers.length; i++) {
           StrategicSimulationObserver trainingObserver = mergeObservers(observer.getStrategicSimulation(), observers, i);

            EGames.updateErrorStats(regressionFactory, trainingObserver, observers[i], stats);
        }

        return stats.rmse();
    }

    protected StrategicSimulationObserver[] splitObservers(StrategicSimulationObserver observer) {
        StrategicSimulationObserver[] observers = new StrategicSimulationObserver[folds];

        for(int i = 0 ; i < folds; i++) {
            observers[i] = new AbstractStrategicSimulationObserver(observer.getStrategicSimulation());
        }

        for(Outcome outcome : Games.traversal(observer.getStrategicSimulation())) {
            Payoff[] payoffs = observer.getObservations(outcome).toArray(new Payoff[0]);

            if(payoffs.length < folds)
                throw new RuntimeException("Data instances must be larger than fold count.");

            Collections.shuffle(Arrays.asList(payoffs));

            for(int i = 0; i < payoffs.length; i++) {
                int index = i % folds;
                observers[index].observe(outcome,payoffs[i]);
            }
        }

        return observers;
    }

    protected StrategicSimulationObserver mergeObservers(StrategicMultiAgentSystem simulation, StrategicSimulationObserver[] observers, int outIndex) {
        StrategicSimulationObserver observer = new AbstractStrategicSimulationObserver(simulation);

        for(Outcome outcome : Games.traversal(observer.getStrategicSimulation())) {
            for(int i = 0; i < observers.length; i++) {
                if ( outIndex != i) {
                    for(Payoff payoff : observers[i].getObservations(outcome)) {
                        observer.observe(outcome, payoff);
                    }
                }
            }
        }

        return observer;
    }
}
