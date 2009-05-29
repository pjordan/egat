package edu.umich.eecs.ai.egat.minform;

import edu.umich.eecs.ai.egat.game.SymmetricGame;

import java.util.Set;

/**
 * @author Patrick Jordan
 */
public interface MinimumFormationFinder {
    SymmetricGame findMinimumFormation(SymmetricGame game);
    Set<SymmetricGame> findAllMinimumFormations(SymmetricGame game);
}
