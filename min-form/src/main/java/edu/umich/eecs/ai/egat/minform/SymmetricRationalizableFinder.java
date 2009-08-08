/*
 * SymmetricRationalizableFinder.java
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

import edu.umich.eecs.ai.egat.game.Action;
import edu.umich.eecs.ai.egat.game.SymmetricGame;
import edu.umich.eecs.ai.egat.game.Player;
import edu.umich.eecs.ai.egat.game.SymmetricMultiAgentSystem;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public interface SymmetricRationalizableFinder {
    /**
     * Returns all strategies that are rationalizable from a restricted set of actions.
     * @param actions the restricted set of strategies
     * @param game the base game
     * @return the set of strategies that are rationalizable strategies.
     */
    Set<Action> findRationalizable(Set<Action> actions, SymmetricGame game);

    /**
     * Returns the strategy that maximizes the gain from deviation
     * @param actions the restricted set of strategies
     * @param game the base game
     * @return the strategy that maximizes the gain from deviation
     */
    Action findMaxRationalizable(Set<Action> actions, SymmetricGame game);

    /**
     * Returns the strategy that maximizes the gain from deviation
     * @param actions the restricted set of strategies
     * @param others the set of actions outside the restricted set
     * @param game the base game
     * @return the strategy that maximizes the gain from deviation
     */
    Action findMaxRationalizable(Set<Action> actions, Set<Action> others, SymmetricGame game);

    /**
     * Returns the slack from the rationalizable LP.
     * @param candidateAction the candidate action
     * @param actions the restricted set of actions
     * @param game the base game
     * @return the slack from the rationalizable LP
     */
    double rationalizableSlack(Action candidateAction, Set<Action> actions, SymmetricGame game);

    /**
     * Returns the delta from the restricted game.
     * @param player the player
     * @param restrictedGame the restricted game
     * @param game the base game
     * @return the slack from the rationalizable LP
     */
    double rationalizableDelta(Player player, Set<Action> restrictedGame, SymmetricGame game);

    /**
     * Returns the delta from the restricted game.
     * @param player the player
     * @param restrictedGame the restricted game
     * @param game the base game
     * @return the slack from the rationalizable LP
     */
    double rationalizableDelta(Player player, SymmetricMultiAgentSystem restrictedGame, SymmetricGame game);

    /**
     * Returns the epsilon from the restricted game.
     * @param restrictedGame the restricted game
     * @param game the base game
     * @return the slack from the rationalizable LP
     */
    double rationalizableEpsilon(Set<Action> restrictedGame, SymmetricGame game);

    /**
     * Returns the epsilon from the restricted game.
     * @param restrictedGame the restricted game
     * @param game the base game
     * @return the slack from the rationalizable LP
     */
    double rationalizableEpsilon(SymmetricMultiAgentSystem restrictedGame, SymmetricGame game);
}
