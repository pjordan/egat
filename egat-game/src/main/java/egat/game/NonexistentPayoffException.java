/*
 * NonexistentPayoffException.java
 *
 * Copyright (C) 2006-2010 Patrick R. Jordan
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
package egat.game;

/**
 * @author Patrick Jordan
 */
public class NonexistentPayoffException extends RuntimeException {
    private Outcome outcome;

    public NonexistentPayoffException(Outcome outcome) {
        super(String.format("Payoff for %s does not exist", outcome));
        this.outcome = outcome;
    }

    public Outcome getOutcome() {
        return outcome;
    }
}
