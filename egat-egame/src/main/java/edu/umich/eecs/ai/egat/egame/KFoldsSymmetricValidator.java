/*
 * KFoldsSymmetricValidator.java
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

import edu.umich.eecs.ai.egat.game.*;

import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.Set;

/**
 * @author Patrick Jordan
 */
public class KFoldsSymmetricValidator implements SymmetricCrossValidator {
    protected int folds;
    protected Random random;

    public KFoldsSymmetricValidator() {
        this(10);
    }

    public KFoldsSymmetricValidator(int folds) {
        this.folds = folds;
        random = new Random();
    }

    public double crossValidate(SymmetricRegressionFactory regressionFactory, SymmetricSimulationObserver observer) {

        ErrorStats stats = new ErrorStats();

        SymmetricSimulationObserver[] observers = splitObservers(observer);

        for(int i = 0; i < observers.length; i++) {
           SymmetricSimulationObserver trainingObserver = mergeObservers(observer.getSymmetricSimulation(), observers, i);

            EGames.updateErrorStats(regressionFactory, trainingObserver, observers[i], stats);
        }

        return stats.rmse();
    }

    protected SymmetricSimulationObserver[] splitObservers(SymmetricSimulationObserver observer) {
        SymmetricSimulationObserver[] observers = new SymmetricSimulationObserver[folds];

        for(int i = 0 ; i < folds; i++) {
            observers[i] = new AbstractSymmetricSimulationObserver(observer.getSymmetricSimulation());
        }

        for(SymmetricOutcome outcome : Games.symmetricTraversal(observer.getSymmetricSimulation())) {
            SymmetricPayoff[] payoffs = observer.getObservations(outcome).toArray(new SymmetricPayoff[0]);

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

    protected SymmetricSimulationObserver mergeObservers(SymmetricMultiAgentSystem simulation, SymmetricSimulationObserver[] observers, int outIndex) {
        SymmetricSimulationObserver observer = new AbstractSymmetricSimulationObserver(simulation);

        for(SymmetricOutcome outcome : Games.symmetricTraversal(observer.getSymmetricSimulation())) {
            for(int i = 0; i < observers.length; i++) {
                if ( outIndex != i) {
                    for(SymmetricPayoff payoff : observers[i].getObservations(outcome)) {
                        observer.observe(outcome, payoff);
                    }
                }
            }
        }

        return observer;
    }
}
