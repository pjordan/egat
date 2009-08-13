/*
 * StrategicRationalizableFinder.java
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
package edu.umich.eecs.ai.egat.minform;

import edu.umich.eecs.ai.egat.game.*;

import java.util.Set;

/**
 * @author Patrick R. Jordan
 */
public interface StrategicRationalizableFinder {
    /**
     * Returns all strategies that are rationalizable from a restricted set of actions.
     * @param player the player
     * @param restrictedActions the restricted actions
     * @param game the base game
     * @return the set of strategies that are rationalizable strategies.
     */
    Set<Action> findRationalizable(Player player, StrategicMultiAgentSystem restrictedActions, StrategicGame game);

    /**
     * Returns the strategy that maximizes the gain from deviation
     * @param player the player
     * @param restrictedActions the restricted actions
     * @param game the base game
     * @return the strategy that maximizes the gain from deviation
     */
    Action findMaxRationalizable(Player player, StrategicMultiAgentSystem restrictedActions, StrategicGame game);

    /**
     * Returns the strategy that maximizes the gain from deviation
     * @param player the player
     * @param restrictedActions the restricted actions
     * @param otherActions the set of actions outside the restricted set
     * @param game the base game
     * @return the strategy that maximizes the gain from deviation
     */
    Action findMaxRationalizable(Player player, StrategicMultiAgentSystem restrictedActions, Set<Action> otherActions, StrategicGame game);

    /**
     * Returns the slack from the rationalizable LP.
     * @param player the player
     * @param candidateAction the candidate action
     * @param restrictedActions the restricted actions
     * @param game the base game
     * @return the slack from the rationalizable LP
     */
    double rationalizableSlack(Player player, Action candidateAction, StrategicMultiAgentSystem restrictedActions, StrategicGame game);

    /**
     * Returns the tau from the rationalizable LP.
     * @param player the player
     * @param candidateAction the candidate action
     * @param restrictedActions the restricted actions
     * @param game the base game
     * @return the slack from the rationalizable LP
     */
    double rationalizableTau(Player player, Action candidateAction, StrategicMultiAgentSystem restrictedActions, StrategicGame game);

    /**
     * Returns the delta for the restricted game.
     * @param restrictedGame the restricted game
     * @param game the base game
     * @return the slack from the rationalizable LP
     */
    double rationalizableDelta(Player player, StrategicMultiAgentSystem restrictedGame, StrategicGame game);

    /**
     * Returns the delta for the restricted game.
     * @param restrictedGame the restricted game
     * @param game the base game
     * @return the slack from the rationalizable LP
     */
    double rationalizableEpsilon(StrategicMultiAgentSystem restrictedGame, StrategicGame game);
}
