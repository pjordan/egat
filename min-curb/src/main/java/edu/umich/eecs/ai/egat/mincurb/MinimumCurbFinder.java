package edu.umich.eecs.ai.egat.mincurb;

import edu.umich.eecs.ai.egat.game.SymmetricGame;

import java.util.List;
import java.util.Set;

/**
 * @author Patrick Jordan
 */
public interface MinimumCurbFinder {
    SymmetricGame findMinimumCurb(SymmetricGame game);
    Set<SymmetricGame> findAllMinimumCurbs(SymmetricGame game);
}
