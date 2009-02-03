package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

/**
 * @author Patrick Jordan
 */
public class SimpleSymmetricRegressionFactory implements SymmetricRegressionFactory {
    public SymmetricRegression regress(SymmetricSimulationObserver observer) {
        SymmetricSimulation simulation = observer.getSymmetricSimulation();
        DefaultSymmetricGame game = new DefaultSymmetricGame(simulation.getName(), simulation.getDescription());
        BasicStatsSymmetricSimulationObserver basicObserver = new BasicStatsSymmetricSimulationObserverImpl(observer);

        for(Player p : simulation.players())
            game.addPlayer(p);

        for(Action a : simulation.getActions())
            game.addAction(a);


        for(SymmetricOutcome outcome : Games.symmetricTraversal(simulation)) {
            game.putPayoff(outcome, basicObserver.meanObservationPayoff(outcome).valueMap());
        }

        return new SimpleSymmetricRegression(game);
    }
}
