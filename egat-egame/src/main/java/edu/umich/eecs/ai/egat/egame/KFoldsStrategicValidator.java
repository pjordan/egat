package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Arrays;
import java.util.Random;
import java.util.Collections;
import java.util.Set;

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
