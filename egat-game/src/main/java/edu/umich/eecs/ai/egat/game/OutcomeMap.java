package edu.umich.eecs.ai.egat.game;

/**
 * @author Patrick Jordan
 */
public interface OutcomeMap<T,K extends Outcome> {
    public <S extends K> T get(S outcome);
    public <S extends K> void put(S outcome, T t);
    public void build(StrategicMultiAgentSystem simulation);
}
