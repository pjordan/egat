/*
 * KFoldsSymmetricValidatorTest.java
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
package edu.umich.eecs.ai.egat.egame;

import org.junit.Test;
import org.junit.Assert;
import edu.umich.eecs.ai.egat.game.*;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Patrick Jordan
 */
public class KFoldsSymmetricValidatorTest {
    @Test
    public void testDefaultConstructor() {
        new KFoldsSymmetricValidator();
    }

    @Test
    public void testValidation() {
        Player[] players = new Player[]{Games.createPlayer("Alice"),
                Games.createPlayer("Bob")};
        Action[] actions = new Action[]{Games.createAction("a"),
                Games.createAction("b")};

        SymmetricMultiAgentSystem sim = createSimulation(players, actions);
        SymmetricSimulationObserver observer = new AbstractSymmetricSimulationObserver(sim);

        observePayoffs( observer, 0.0);
        observePayoffs( observer, 1.0);

        SimpleSymmetricRegressionFactory factory = new SimpleSymmetricRegressionFactory();
        KFoldsSymmetricValidator validator = new KFoldsSymmetricValidator(2);
        double rmse = validator.crossValidate(factory, observer);

        Assert.assertEquals(rmse, 1.0);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidFold() {
        Player[] players = new Player[]{Games.createPlayer("Alice"),
                Games.createPlayer("Bob")};
        Action[] actions = new Action[]{Games.createAction("a"),
                Games.createAction("b")};


        SymmetricMultiAgentSystem sim = createSimulation(players, actions);
        SymmetricSimulationObserver observer = new AbstractSymmetricSimulationObserver(sim);

        observePayoffs( observer, 0.0);
        observePayoffs( observer, 1.0);

        SimpleSymmetricRegressionFactory factory = new SimpleSymmetricRegressionFactory();
        KFoldsSymmetricValidator validator = new KFoldsSymmetricValidator();
        double rmse = validator.crossValidate(factory, observer);

        Assert.assertEquals(rmse, 1.0);
    }

    protected SymmetricMultiAgentSystem createSimulation(Player[] players, Action[] actions) {
        MutableSymmetricMultiAgentSystem sim = new DefaultSymmetricMultiAgentSystem("test", "testEmptyObservationCount");

        for (Player p : players) {
            sim.addPlayer(p);
        }

        for (Action a : actions) {
            sim.addAction(a);
        }

        return sim;
    }

    protected void observePayoffs(SymmetricSimulationObserver observer, double payoff) {
        for (SymmetricOutcome outcome : Games.symmetricTraversal(observer.getSymmetricSimulation())) {
            Map<Action,PayoffValue> map = new HashMap<Action,PayoffValue>();
            for(Action a : new HashSet<Action>(outcome.actions())) {
                map.put(a, PayoffFactory.createPayoffValue(payoff));
            }

            observer.observe(outcome, PayoffFactory.createSymmetricPayoff(map, outcome));

        }
    }
}
