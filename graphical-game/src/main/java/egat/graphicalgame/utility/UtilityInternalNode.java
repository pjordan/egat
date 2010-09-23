/*
 * UtilityInternalNode.java
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

import egat.game.Player;
import egat.game.Action;
import egat.game.Outcome;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

/**
 * @author Patrick Jordan
 */
public class UtilityInternalNode implements UtilityNode {
    private Player player;

    private Map<Action,UtilityNode> children;

    public UtilityInternalNode(Player player) {
        if(player==null) {
            throw new NullPointerException("Player cannot be null");
        }
        this.player = player;

        children = new HashMap<Action, UtilityNode>();
    }

    public double utility(Outcome outcome) {
        return children.get(outcome.getAction(player)).utility(outcome);
    }

    public void setUtilityChild(Action action, UtilityNode utilityNode) {
        children.put(action, utilityNode);
    }

    public Collection<UtilityNode> children() {
        return children.values();
    }

    public int decendantCount() {
        int count = 0;

        for(UtilityNode child : children()) {
            count+= child.decendantCount()+1;
        }

        return count;
    }
}
