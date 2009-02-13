package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Patrick Jordan
 */
public class EquivalentStrategySymmetricRegressionFactory implements SymmetricRegressionFactory {
    protected Map<Action, Action> actionConverter;


    public EquivalentStrategySymmetricRegressionFactory(Map<Action, Action> actionConverter) {
        this.actionConverter = actionConverter;
    }


    public SymmetricRegression regress(SymmetricSimulationObserver observer) {
        SymmetricSimulation simulation = observer.getSymmetricSimulation();
        MutableSymmetricSimulation reducedSimulation = new DefaultSymmetricSimulation(simulation.getName(), simulation.getDescription());

        for (Player p : simulation.players()) {
            reducedSimulation.addPlayer(p);
        }

        for (Action a : simulation.getActions()) {
            reducedSimulation.addAction(actionConverter.get(a));
        }

        BasicStatsSymmetricSimulationObserver reducedObserver = new BasicStatsSymmetricSimulationObserverImpl(reducedSimulation);


        for (SymmetricOutcome outcome : Games.symmetricTraversal(simulation)) {

            SymmetricOutcome convertedOutcome = convertOutcome(outcome);

            for (SymmetricPayoff p : observer.getObservations(outcome)) {
                reducedObserver.observe(convertedOutcome, convertPayoff(p, outcome, convertedOutcome));
            }
        }


        return new EquivalentStrategySymmetricRegression(new SimpleSymmetricRegressionFactory().regress(reducedObserver).getSymmetricGame(), actionConverter);
    }

    private SymmetricOutcome convertOutcome(SymmetricOutcome outcome) {
        Player[] players = outcome.players().toArray(new Player[0]);
        Action[] actions = new Action[players.length];

        for (int i = 0; i < players.length; i++) {
            actions[i] = actionConverter.get(outcome.getAction(players[i]));
        }

        return Games.createSymmetricOutcome(players, actions);
    }

    private SymmetricPayoff convertPayoff(SymmetricPayoff payoff, SymmetricOutcome oldOutcome, SymmetricOutcome newOutcome) {
        Map<Action, PayoffValue> map = new HashMap<Action, PayoffValue>();
        for (Action a : new HashSet<Action>(newOutcome.actions())) {

            double sum = 0.0;
            double count = 0.0;

            for (Action oa : new HashSet<Action>(oldOutcome.actions())) {
                if ( a.equals( actionConverter.get(oa) ) ) {
                    sum += payoff.getPayoff(oa).getValue() * oldOutcome.getCount(oa);
                    count += oldOutcome.getCount(oa);
                }
            }

            map.put(a, PayoffFactory.createPayoffValue(sum / count));

        }


        return PayoffFactory.createSymmetricPayoff(map, newOutcome);
    }

}
