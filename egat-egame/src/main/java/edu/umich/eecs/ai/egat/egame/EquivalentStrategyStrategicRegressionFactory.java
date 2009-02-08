package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class EquivalentStrategyStrategicRegressionFactory implements StrategicRegressionFactory {
    protected Map<Player,Map<Action,Action>> actionConverter;


    public EquivalentStrategyStrategicRegressionFactory(Map<Player,Map<Action,Action>> actionConverter) {
        this.actionConverter = actionConverter;
    }


    public StrategicRegression regress(StrategicSimulationObserver observer) {
        StrategicSimulation simulation = observer.getStrategicSimulation();
        DefaultStrategicSimulation reducedSimulation = new DefaultStrategicSimulation(simulation.getName(), simulation.getDescription());
        
        for(Player p : simulation.players()) {
            reducedSimulation.addPlayer(p);

            Set<Action> actions = new HashSet<Action>();
            for(Action a : simulation.getActions(p)) {
                actions.add(actionConverter.get(p).get(a));
            }

            reducedSimulation.putActions(p,actions);
        }

        BasicStatsStrategicSimulationObserver reducedObserver = new BasicStatsStrategicSimulationObserverImpl(reducedSimulation);


        for(Outcome outcome : Games.traversal(simulation)) {
            Outcome convertedOutcome = convertOutcome(outcome);
            for(Payoff p : observer.getObservations(outcome)) {
                reducedObserver.observe(convertedOutcome, p);
            }
        }


        return new EquivalentStrategyStrategicRegression(new SimpleStrategicRegressionFactory().regress(reducedObserver).getStrategicGame(), actionConverter);
    }

    private Outcome convertOutcome(Outcome outcome) {
        Player[] players = outcome.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

        for(int i = 0; i < players.length; i++) {
            actions[i] = actionConverter.get(players[i]).get(outcome.getAction(players[i]));
        }

        return Games.createOutcome(players,actions);
    }


}
