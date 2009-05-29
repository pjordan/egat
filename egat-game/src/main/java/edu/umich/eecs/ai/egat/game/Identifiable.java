package edu.umich.eecs.ai.egat.game;

/**
 * The {@link Identifiable} interface for a multi-agent system.  The contract for {@link Identifiable}
 * requires that two identifiables of a given class are equal if {@link # getID()} returns
 * equal strings for both identifiables.
 *
 * @param <T> the identifiable type.
 *
 * @author Patrick Jordan
 */
public interface Identifiable<T extends Identifiable> extends Comparable<T> {
    /**
     * Returns the identifier for this identifiable.
     * @return the identifier for this identifiable.
     */
    String getID();
}
