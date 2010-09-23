/*
 * ExpectedLossCalculator.java
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

import egat.game.Outcome;
import egat.game.Games;
import egat.game.SymmetricOutcome;

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
