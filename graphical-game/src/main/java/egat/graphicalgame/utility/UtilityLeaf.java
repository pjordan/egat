/*
 * UtilityLeaf.java
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
package egat.graphicalgame.utility;

import egat.game.Outcome;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Patrick Jordan
 */
public class UtilityLeaf implements UtilityNode {
    private double utility;

    public UtilityLeaf(double utility) {
        this.utility = utility;
    }

    public double utility(Outcome outcome) {
        return utility;
    }

    public Collection<UtilityNode> children() {
        return Collections.EMPTY_LIST;
    }

    public int decendantCount() {
        return 0;
    }
}
