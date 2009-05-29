package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

/**
 * @author Patrick Jordan
 */
public class SimpleStrategicRegressionFactory implements StrategicRegressionFactory {
    public StrategicRegression regress(StrategicSimulationObserver observer) {
        StrategicMultiAgentSystem simulation = observer.getStrategicSimulation();
        DefaultStrategicGame game = new DefaultStrategicGame(simulation.getName(), simulation.getDescription());
        BasicStatsStrategicSimulationObserver basicObserver = new BasicStatsStrategicSimulationObserverImpl(observer);
        for(Player p : simulation.players()) {
            game.addPlayer(p);

            game.putActions(p, simulation.getActions(p));
        }

        game.build();

        for(Outcome outcome : Games.traversal(simulation)) {
            game.putPayoff(outcome, basicObserver.meanObservationPayoff(outcome));
        }

        return new SimpleStrategicRegression(game);
    }
}
