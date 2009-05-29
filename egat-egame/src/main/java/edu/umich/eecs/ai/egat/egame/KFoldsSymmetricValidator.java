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
