package edu.umich.eecs.ai.egat.egame;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Set;
import java.util.Random;

/**
 * @author Patrick Jordan
 */
public class DelegateGaussianStrategicSimulator implements StrategicSimulator {
    private StrategicGame game;
    private double mean;
    private double std;
    private Random random;

    public DelegateGaussianStrategicSimulator(StrategicGame game) {
        this(game,0.0,1.0);
    }

    public DelegateGaussianStrategicSimulator(StrategicGame game, double mean, double std) {
        this(game, mean, std, new Random());
    }

    public DelegateGaussianStrategicSimulator(StrategicGame game, double mean, double std, Random random) {
        this.game = game;
        this.mean = mean;
        this.std = std;
        this.random = random;
    }

    public Payoff simulate(Outcome outcome) {
        Payoff basePayoff = game.payoff(outcome);

        Player[] players = (Player[])basePayoff.players().toArray(new Player[0]);
        double[] payoffs = new double[players.length];


        for(int i = 0; i < players.length; i++) {
            payoffs[i] = basePayoff.getPayoff(players[i]).getValue() + (random.nextGaussian()*std + mean);
        }

        return PayoffFactory.createPayoff(players,payoffs);
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
