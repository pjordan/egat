package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.Outcome;
import edu.umich.eecs.ai.egat.game.Games;
import edu.umich.eecs.ai.egat.game.SymmetricOutcome;

/**
 * @author Patrick Jordan
 */
public class ExpectedLossCalculator {

    public double expectedLoss(StrategicSimulator simulator,
                               StrategicRegressionFactory strategicRegressionFactory,
                               int trainingSetSize,
                               int testSetSize,
                               int runs) {

        return expectedLossStats(simulator,strategicRegressionFactory,trainingSetSize,testSetSize,runs).mean();

    }

    public SampleStatistics expectedLossStats(StrategicSimulator simulator,
                               StrategicRegressionFactory strategicRegressionFactory,
                               int trainingSetSize,
                               int testSetSize,
                               int runs) {

        SampleStatistics lossStats = new SampleStatistics();

        for(int run = 0; run < runs; run++) {

            ErrorStats errorStats = new ErrorStats();

            StrategicSimulationObserver trainingObserver = new BasicStatsStrategicSimulationObserverImpl(simulator);

            StrategicSimulationObserver testObserver = new BasicStatsStrategicSimulationObserverImpl(simulator);


            for(Outcome outcome : Games.traversal(simulator)) {

                for(int i = 0; i < trainingSetSize; i++) {

                    trainingObserver.observe(outcome, simulator.simulate(outcome));

                }

                for(int i = 0; i < testSetSize; i++) {

                    testObserver.observe(outcome, simulator.simulate(outcome));

                }

            }

            EGames.updateErrorStats(strategicRegressionFactory, trainingObserver, testObserver, errorStats);

            lossStats.add(errorStats.rmse());

        }

        return lossStats;

    }

    public double expectedLoss(SymmetricSimulator simulator,
                               SymmetricRegressionFactory regressionFactory,
                               int trainingSetSize,
                               int testSetSize,
                               int runs) {

        return expectedLossStats(simulator,regressionFactory,trainingSetSize,testSetSize,runs).mean();
    }

    public SampleStatistics expectedLossStats(SymmetricSimulator simulator,
                               SymmetricRegressionFactory regressionFactory,
                               int trainingSetSize,
                               int testSetSize,
                               int runs) {

        SampleStatistics lossStats = new SampleStatistics();

        for(int run = 0; run < runs; run++) {

            ErrorStats errorStats = new ErrorStats();

            SymmetricSimulationObserver trainingObserver = new BasicStatsSymmetricSimulationObserverImpl(simulator);

            SymmetricSimulationObserver testObserver = new BasicStatsSymmetricSimulationObserverImpl(simulator);


            for(SymmetricOutcome outcome : Games.symmetricTraversal(simulator)) {

                for(int i = 0; i < trainingSetSize; i++) {

                    trainingObserver.observe(outcome, simulator.simulate(outcome));

                }

                for(int i = 0; i < testSetSize; i++) {

                    testObserver.observe(outcome, simulator.simulate(outcome));

                }

            }

            EGames.updateErrorStats(regressionFactory, trainingObserver, testObserver, errorStats);

            lossStats.add(errorStats.rmse());

        }

        return lossStats;

    }


    public RegressionStatistics regressionStatistics(StrategicSimulator simulator,
                               StrategicRegressionFactory strategicRegressionFactory,
                               int trainingSetSize,
                               int testSetSize,
                               int runs) {

        RegressionStatistics statistics = new RegressionStatistics();

        for(int run = 0; run < runs; run++) {

            StrategicSimulationObserver trainingObserver = new BasicStatsStrategicSimulationObserverImpl(simulator);

            StrategicSimulationObserver testObserver = new BasicStatsStrategicSimulationObserverImpl(simulator);


            for(Outcome outcome : Games.traversal(simulator)) {

                for(int i = 0; i < trainingSetSize; i++) {

                    trainingObserver.observe(outcome, simulator.simulate(outcome));

                }

                for(int i = 0; i < testSetSize; i++) {

                    testObserver.observe(outcome, simulator.simulate(outcome));

                }

            }

            EGames.updateRegressionStats(strategicRegressionFactory, trainingObserver, testObserver, statistics);

        }

        return statistics;

    }

    public RegressionStatistics regressionStatistics(SymmetricSimulator simulator,
                                                     SymmetricRegressionFactory regressionFactory,
                                                     int trainingSetSize,
                                                     int testSetSize,
                                                     int runs) {

        RegressionStatistics statistics = new RegressionStatistics();

        for(int run = 0; run < runs; run++) {

            SymmetricSimulationObserver trainingObserver = new BasicStatsSymmetricSimulationObserverImpl(simulator);

            SymmetricSimulationObserver testObserver = new BasicStatsSymmetricSimulationObserverImpl(simulator);

            for(SymmetricOutcome outcome : Games.symmetricTraversal(simulator)) {

                for(int i = 0; i < trainingSetSize; i++) {

                    trainingObserver.observe(outcome, simulator.simulate(outcome));

                }

                for(int i = 0; i < testSetSize; i++) {

                    testObserver.observe(outcome, simulator.simulate(outcome));

                }

            }

            EGames.updateRegressionStats(regressionFactory, trainingObserver, testObserver, statistics);
        }

        return statistics;

    }
}
