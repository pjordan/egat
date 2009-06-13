/*
 * DeviationFactory.java
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
package edu.umich.eecs.ai.egat.game;

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class DeviationFactory {
    private DeviationFactory() {
    }

    public static OutcomeTraversal deviationTraversal(Outcome outcome, StrategicMultiAgentSystem game, Player player) {
        return new DeviationTraversal(outcome,game,player);
    }

    public static SymmetricOutcomeTraversal deviationTraversal(SymmetricOutcome outcome, SymmetricMultiAgentSystem simulation) {
        return new SymmetricDeviationTraversal(outcome,simulation);
    }
    
    static class DeviationTraversal implements OutcomeTraversal {
        Outcome outcome;
        StrategicMultiAgentSystem simulation;
        Player player;
        public DeviationTraversal(Outcome outcome, StrategicMultiAgentSystem simulation, Player player) {
            this.outcome = outcome;
            this.simulation = simulation;
            this.player = player;
        }

        public Iterator<Outcome> iterator() {
            return new DeviationIterator(outcome, simulation,player);
        }
    }

    static class SymmetricDeviationTraversal implements SymmetricOutcomeTraversal {
        SymmetricOutcome outcome;
        SymmetricMultiAgentSystem simulation;
        public SymmetricDeviationTraversal(SymmetricOutcome outcome, SymmetricMultiAgentSystem simulation) {
            this.outcome = outcome;
            this.simulation = simulation;

        }

        public Iterator<SymmetricOutcome> iterator() {
            return new SymmetricDeviationIterator(outcome, simulation);
        }
    }

    static class DeviationIterator implements Iterator<Outcome> {
        Outcome base;
        StrategicMultiAgentSystem simulation;
        Player[] players;
        Action[] actions;
        Outcome cur;
        Iterator<Action> actionIterator;
        int playerIdx;

        public DeviationIterator(Outcome outcome, StrategicMultiAgentSystem simulation, Player player) {
            this.base = outcome;
            this.simulation = simulation;


            players = simulation.players().toArray(new Player[0]);
            playerIdx = -1;
            // Find the player index
            for(int i = 0; i < players.length; i++) {
                if(player.equals(players[i])) {
                    playerIdx = i;
                    break;
                }
            }

            actions = new Action[players.length];
            for(int i = 0; i < players.length; i++ ) {
                actions[i] = outcome.getAction(players[i]);
            }

            Set<Action> set = new HashSet<Action>(simulation.getActions(player));
            set.remove(actions[playerIdx]);
            actionIterator = set.iterator();
            update();
        }

        public boolean hasNext() {
            return cur!=null;
        }

        public Outcome next() {
            Outcome next = cur;
            update();
            return next;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported in this iterator.");
        }

        private void update() {
             if(actionIterator==null || !actionIterator.hasNext()) {
                cur = null;
            } else {
                actions[playerIdx] = actionIterator.next();
                cur = Games.createOutcome(players,actions);
            }
        }

    }

    static class SymmetricDeviationIterator implements Iterator<SymmetricOutcome> {
        SymmetricOutcome base;
        SymmetricMultiAgentSystem simulation;
        SymmetricOutcome cur;
        List<Player> players;
        DefaultSymmetricOutcomeMap<Boolean> outcomeMap;
        Iterator<Outcome> iterator;

        public SymmetricDeviationIterator(SymmetricOutcome outcome, SymmetricMultiAgentSystem simulation) {
            this.base = outcome;
            this.simulation = simulation;
            players = new LinkedList<Player>(simulation.players());
            outcomeMap = new DefaultSymmetricOutcomeMap<Boolean>();
            outcomeMap.put(outcome,true);
            update();
        }

        public boolean hasNext() {
            return cur!=null;
        }

        public SymmetricOutcome next() {
            SymmetricOutcome next = cur;
            update();
            return next;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported in this iterator.");
        }

        private void update() {
            if(iterator==null || !iterator.hasNext()) {
                cur = null;

                if(!players.isEmpty()) {
                    Player p = players.remove(0);
                    iterator =  new DeviationIterator(base, simulation,p);
                    update();
                } else {
                    return;
                }
            } else {
                SymmetricOutcome so = Games.createSymmetricOutcome(iterator.next());
                if(outcomeMap.get(so)!=null) {
                    update();
                    return;
                }

                cur = so;
                outcomeMap.put(so,true);
            }
        }

    }
}
