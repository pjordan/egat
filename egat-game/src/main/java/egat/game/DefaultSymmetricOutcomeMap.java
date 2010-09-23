/*
 * DefaultSymmetricOutcomeMap.java
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

import java.util.*;

/**
 * @author Patrick Jordan
 */
public class DefaultSymmetricOutcomeMap<T> implements OutcomeMap<T,SymmetricOutcome>{
    private Map<Set<Map.Entry<Action,Integer>>,T> map;

    public DefaultSymmetricOutcomeMap() {
        map = new HashMap<Set<Map.Entry<Action,Integer>>,T>();
    }

    public <K extends SymmetricOutcome> T get(K outcome) {
        return map.get(outcome.actionEntrySet());
    }

    public <K extends SymmetricOutcome> void put(K outcome,T t) {
        map.put(outcome.actionEntrySet(),t);
    }


    public void build(StrategicMultiAgentSystem simulation) {
    }
}
