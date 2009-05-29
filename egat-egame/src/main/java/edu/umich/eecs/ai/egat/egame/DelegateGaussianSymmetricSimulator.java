package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Set;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class DelegateGaussianSymmetricSimulator implements SymmetricSimulator {
    private SymmetricGame game;
    private double mean;
    private double std;
    private Random random;


    public DelegateGaussianSymmetricSimulator(SymmetricGame game) {
        this(game, 0.0, 1.0);
    }

    public DelegateGaussianSymmetricSimulator(SymmetricGame game, double mean, double std) {
        this(game, mean, std, new Random());
    }

    public DelegateGaussianSymmetricSimulator(SymmetricGame game, double mean, double std, Random random) {
        this.game = game;
        this.mean = mean;
        this.std = std;
        this.random = random;
    }

    public SymmetricPayoff simulate(Outcome outcome) {
        SymmetricPayoff basePayoff = game.payoff(outcome);


        Map<Action,PayoffValue> payoffs = new HashMap<Action, PayoffValue>();

        for(Action action : (Set<Action>)basePayoff.actions()) {
            double payoff = basePayoff.getPayoff(action).getValue() + (random.nextGaussian()*std + mean);
            payoffs.put(action, PayoffFactory.createPayoffValue(payoff));
        }

        return PayoffFactory.createSymmetricPayoff(payoffs, outcome);  
    }

    public Set<Action> getActions() {
        return game.getActions();
    }

    public Set<Action> getActions(Player player) {
        return game.getActions(player);
    }

    public Set<Player> players() {
        return game.players();
    }

    public String getName() {
        return game.getName();
    }

    public String getDescription() {
        return game.getDescription();
    }

}
