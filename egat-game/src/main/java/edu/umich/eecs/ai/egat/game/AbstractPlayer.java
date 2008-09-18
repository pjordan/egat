package edu.umich.eecs.ai.egat.game;

/**
 * The abstact player class should be extended to from concrete player classes.
 *
 * @author Patrick Jordan
 */
public abstract class AbstractPlayer implements Player {
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
        if (!(object instanceof Player)) {
            return false;
        }

        return getID().equals(((Player) object).getID());
    }

    public int compareTo(Player action) {
        return getID().compareTo(action.getID());
    }


    public String toString() {
        return getID();
    }
}
