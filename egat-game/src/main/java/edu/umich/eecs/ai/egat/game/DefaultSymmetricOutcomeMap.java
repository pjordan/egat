package edu.umich.eecs.ai.egat.game;

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
