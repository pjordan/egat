/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.gamexml;

import edu.umich.eecs.ai.egat.game.StrategicGame;
import org.jdom.Document;

/**
 * @author Patrick Jordan
 */
public class XMLNfgFactory {
    private XMLNfgFactory() {
    }


    public static StrategicGame buildStrategic(Document document) {
        return new GameImpl(document.getRootElement());
    }

    public static StrategicGame buildSymmetric(Document document) {
        return new SymmetricGameImpl(document.getRootElement());
    }
}
