package edu.umich.eecs.ai.egat.game;

/**
 * An outcome traversal provides an {@link java.util.Iterator iterator}
 * over all of the available outcomes or a {@link edu.umich.eecs.ai.egat.game.StrategicGame simulation}.
 *
 * @author Patrick Jordan
 */
public interface SymmetricOutcomeTraversal extends Iterable<SymmetricOutcome> {
}
