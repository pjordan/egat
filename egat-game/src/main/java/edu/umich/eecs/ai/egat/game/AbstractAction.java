package edu.umich.eecs.ai.egat.game;

/**
 * The abstact action class should be extended to from concrete action classes.
 *
 * @author Patrick Jordan
 */
public abstract class AbstractAction implements Action {
    private int hashCode;
    private boolean hashCodeFlag;

    public int hashCode() {
        if(!hashCodeFlag) {
            hashCode = getID().hashCode();
            hashCodeFlag = true;
        }
        return hashCode;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Action)) {
            return false;
        }

        return getID().equals(((Action) object).getID());
    }

    public int compareTo(Action action) {
        return getID().compareTo(action.getID());
    }


    public String toString() {
        return getID();
    }
}
