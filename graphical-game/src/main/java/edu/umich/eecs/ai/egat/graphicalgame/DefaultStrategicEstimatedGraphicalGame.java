package edu.umich.eecs.ai.egat.graphicalgame;

import edu.umich.eecs.ai.egat.game.Payoff;
import edu.umich.eecs.ai.egat.game.Outcome;
import edu.umich.eecs.ai.egat.game.OutcomeMap;
import edu.umich.eecs.ai.egat.game.DefaultOutcomeMap;

import java.util.RandomAccess;
import java.util.Map;

/**
 * @author Patrick Jordan
 */
public class DefaultStrategicEstimatedGraphicalGame extends AbstractStrategicEstimatedGraphicalGame implements RandomAccess {
    private OutcomeMap<Payoff, Outcome> payoffs;

    public DefaultStrategicEstimatedGraphicalGame() {
        payoffs = new DefaultOutcomeMap<Payoff>();
    }

    public DefaultStrategicEstimatedGraphicalGame(final String name) {
        super(name);
        payoffs = new DefaultOutcomeMap<Payoff>();
    }

    public DefaultStrategicEstimatedGraphicalGame(final String name, final String description) {
        super(name, description);
        payoffs = new DefaultOutcomeMap<Payoff>();
    }

    public void build() {
        payoffs.build(this);
    }
    
    public Payoff payoff(Outcome outcome) {

        Payoff payoff = payoffs.get(outcome);

        if(payoff == null) {
            payoff = computePayoff(outcome);

            payoffs.put(outcome, payoff);
        }
        
        return payoff;
    }

    public void resetCache() {
        payoffs.build(this);
    }

    @Override
    public DefaultStrategicEstimatedGraphicalGame clone() throws CloneNotSupportedException {
        DefaultStrategicEstimatedGraphicalGame game = (DefaultStrategicEstimatedGraphicalGame) super.clone();

        game.payoffs = new DefaultOutcomeMap<Payoff>();
        game.payoffs.build(game);
        
        return game;
    }
}
