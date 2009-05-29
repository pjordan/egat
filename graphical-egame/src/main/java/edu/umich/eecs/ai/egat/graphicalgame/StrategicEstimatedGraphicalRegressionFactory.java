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
