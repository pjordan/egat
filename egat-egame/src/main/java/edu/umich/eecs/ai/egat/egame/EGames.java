/*
 * EGames.java
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

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class EGames {
    private EGames() {
    }

    public static Payoff meanPayoff(Collection<Payoff> list, Player[] players) {
        double[] values = new double[players.length];

        for(Payoff payoff : list) {
            for(int i = 0; i < players.length; i++) {
                values[i] += payoff.getPayoff(players[i]).getValue();
            }
        }


        if( !list.isEmpty() ) {
            for(int i = 0, n = list.size(); i < players.length; i++) {
                    values[i] /= n;
            }
        }

        return PayoffFactory.createPayoff(players,values);
    }

    public static PayoffValue meanPayoff(List<SymmetricPayoff> payoffs, Action a) {
        double mu = 0.0;
        double n = 0.0;


        for (SymmetricPayoff payoff : payoffs) {
            mu += payoff.getPayoff(a).getValue();
            n++;
        }

        if(n>0)
            mu /= n;


        return PayoffFactory.createPayoffValue(mu);
    }


    public static SymmetricPayoff meanObservationPayoff(List<SymmetricPayoff> payoffs, SymmetricOutcome outcome) {
        Map<Action,PayoffValue> map = new HashMap<Action,PayoffValue>();

        for(Map.Entry<Action,Integer> entry : outcome.actionEntrySet()) {
            map.put(entry.getKey(), meanPayoff(payoffs,entry.getKey()));
        }

        return PayoffFactory.createSymmetricPayoff(map,outcome);
    }

    public static void updateErrorStats(Payoff payoff1, Payoff payoff2, ErrorStats errorStats) {
        for(Player player : (Set<Player>)payoff1.players()) {
            double p1 = payoff1.getPayoff(player).getValue();
            double p2 = payoff2.getPayoff(player).getValue();

            errorStats.addError((p1 - p2)*(p1-p2));
        }

    }

    public static void updateErrorStats(StrategicRegressionFactory regressionFactory,
                                        StrategicSimulationObserver trainingObserver,
                                        StrategicSimulationObserver validationObserver,
                                        ErrorStats errorStats) {
        StrategicRegression regression = regressionFactory.regress(trainingObserver);

        for(Outcome outcome : Games.traversal(validationObserver.getStrategicSimulation())) {
            for(Payoff payoff : validationObserver.getObservations(outcome)) {
                updateErrorStats(payoff, regression.predict(outcome), errorStats);
            }
        }
    }

    public static void updateErrorStats(SymmetricRegressionFactory regressionFactory,
                                 SymmetricSimulationObserver trainingObserver,
                                 SymmetricSimulationObserver validationObserver,
                                 ErrorStats errorStats) {
        SymmetricRegression regression = regressionFactory.regress(trainingObserver);

        for(SymmetricOutcome outcome : Games.symmetricTraversal(validationObserver.getSymmetricSimulation())) {
            for(SymmetricPayoff payoff : validationObserver.getObservations(outcome)) {
                updateErrorStats(payoff, regression.predict(outcome), errorStats);
            }
        }
    }

    public static void updateRegressionStats(StrategicRegressionFactory regressionFactory,
                                        StrategicSimulationObserver trainingObserver,
                                        StrategicSimulationObserver validationObserver,
                                        RegressionStatistics regressionStatistics) {
        
        StrategicRegression optimalRegression = regressionFactory.regress(validationObserver);
        StrategicRegression regression = regressionFactory.regress(trainingObserver);

        for(Outcome outcome : Games.traversal(validationObserver.getStrategicSimulation())) {
            updateRegressionStatsBias(optimalRegression.predict(outcome), regression.predict(outcome), regressionStatistics);   

            for(Payoff payoff : validationObserver.getObservations(outcome)) {
                updateRegressionStatsError(payoff, regression.predict(outcome), regressionStatistics);
            }
        }
    }

    public static void updateRegressionStats(SymmetricRegressionFactory regressionFactory,
                                             SymmetricSimulationObserver trainingObserver,
                                             SymmetricSimulationObserver validationObserver,
                                             RegressionStatistics regressionStatistics) {
        SymmetricRegression optimalRegression = regressionFactory.regress(validationObserver);
        SymmetricRegression regression = regressionFactory.regress(trainingObserver);

        for(SymmetricOutcome outcome : Games.symmetricTraversal(validationObserver.getSymmetricSimulation())) {
            updateRegressionStatsBias(optimalRegression.predict(outcome), regression.predict(outcome), regressionStatistics);

            for(SymmetricPayoff payoff : validationObserver.getObservations(outcome)) {
                updateRegressionStatsError(payoff, regression.predict(outcome), regressionStatistics);
            }
        }
    }

    private static void updateRegressionStatsBias(Payoff payoff1, Payoff payoff2, RegressionStatistics regressionStatistics) {
        for(Player player : (Set<Player>)payoff1.players()) {
            double p1 = payoff1.getPayoff(player).getValue();
            double p2 = payoff2.getPayoff(player).getValue();

            regressionStatistics.addBias(p1 - p2);
        }
    }

    private static void updateRegressionStatsError(Payoff payoff1, Payoff payoff2, RegressionStatistics regressionStatistics) {
        for(Player player : (Set<Player>)payoff1.players()) {
            double p1 = payoff1.getPayoff(player).getValue();
            double p2 = payoff2.getPayoff(player).getValue();

            regressionStatistics.addError(p1 - p2);
        }
    }
}
