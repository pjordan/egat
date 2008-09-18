/**
 * $Id$
 * $Date$
 * $Author$
 * $Revision$
 */
package edu.umich.eecs.ai.egat.gamexml;

import org.jdom.Element;

/**
 * @author Patrick Jordan
 */
class XMLDelegator {
    private Element root;

    private XMLDelegator() {
    }

    XMLDelegator(Element root) {
        if(root ==null)
            throw new NullPointerException("root cannot be null.");
        this.root = root;
    }

    public int hashCode() {
        return root.hashCode();
    }

    public boolean equals(final Object object) {
        if(object instanceof XMLDelegator) {
            return root.equals(((XMLDelegator)object).root);
        } else
            return false;
    }

    public String toString() {
        return root.toString();
    }

    /**
     * Get the delegate that the object is wrapping.
     * @return the delegate that the object is wrapping.
     */
    final Element getRoot() {
        return root;
    }
}
