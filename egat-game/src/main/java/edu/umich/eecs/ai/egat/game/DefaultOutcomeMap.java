/*
 * DefaultOutcomeMap.java
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
public class DefaultOutcomeMap<T> implements OutcomeMap<T,Outcome>{

    private Map<Action, Integer>[] playerActionIndex;
    private Player[] indexPlayer;
    private int playerCount;
    private List outcomeT;


    public DefaultOutcomeMap() {

    }



    public void build(StrategicMultiAgentSystem simulation) {
        int index = 0;
        indexPlayer = simulation.players().toArray(new Player[0]);
        playerActionIndex = (Map<Action, Integer>[])new Map[indexPlayer.length];

        for (Player p : simulation.players()) {
            indexPlayer[index]=p;
            int aindex = 0;
            Map<Action, Integer> amap = new HashMap<Action, Integer>();
            for (Action a : (Set<Action>)simulation.getActions(p)) {
                amap.put(a, aindex++);
            }
            playerActionIndex[index++]= amap;
        }

        if(index > 0) {
            outcomeT = buildRecursive(0,index);
        } else {
            outcomeT = Collections.emptyList();
        }
        playerCount = index;
    }

    private List buildRecursive(int playerIndex, int maxCount) {
        int actions = playerActionIndex[playerIndex].keySet().size();
        List list = new ArrayList(actions);

        for (int i = 0; i < actions; i++) {
            list.add(null);
        }

        if (playerIndex < maxCount - 1) {
            for (int i = 0; i < actions; i++) {
                list.set(i, buildRecursive(playerIndex+1, maxCount));
            }
        }

        return list;
    }

    public <K extends Outcome> T get(K outcome) {
        Object o = outcomeT;

        for(int i = 0; i < playerCount; i++) {
            Player p = indexPlayer[i];
            Integer index = playerActionIndex[i].get(outcome.getAction(p));

            if(o==null || index==null) {
                return null;
            } else {                                
                o = ((List)o).get(index);
            }
        }

        return (T)o;
    }

    public <K extends Outcome> void put(K outcome,T t) {
        Object o = outcomeT;

        for(int i = 0; i < playerCount-1; i++) {
            Player p = indexPlayer[i];
            o = ((List)o).get(playerActionIndex[i].get(outcome.getAction(p)));
        }

        ((List)o).set(playerActionIndex[playerCount-1].get(outcome.getAction(indexPlayer[playerCount-1])),t);
    }

    
}
