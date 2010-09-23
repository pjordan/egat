/*
 * DefaultStrategicEstimatedGraphicalGame.java
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
package egat.graphicalgame;

import egat.game.Payoff;
import egat.game.Outcome;
import egat.game.OutcomeMap;
import egat.game.DefaultOutcomeMap;

import java.util.RandomAccess;

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
