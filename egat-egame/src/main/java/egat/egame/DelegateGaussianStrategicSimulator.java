/*
 * DelegateGaussianStrategicSimulator.java
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

import egat.game.*;

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
