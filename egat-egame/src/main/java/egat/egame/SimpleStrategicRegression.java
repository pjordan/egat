/*
 * SimpleStrategicRegression.java
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

import egat.game.StrategicGame;
import egat.game.Payoff;
import egat.game.Outcome;

/**
 * @author Patrick Jordan
 */
public class SimpleStrategicRegression implements StrategicRegression {
    protected StrategicGame game;


    public SimpleStrategicRegression(StrategicGame game) {
        this.game = game;
    }

    public StrategicGame getStrategicGame() {
        return game;
    }

    public Payoff predict(Outcome outome) {
        return game.payoff(outome);
    }
}
