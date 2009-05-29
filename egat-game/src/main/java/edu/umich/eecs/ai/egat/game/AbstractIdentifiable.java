package edu.umich.eecs.ai.egat.game;

/**
 * The abstract identifiable class provides string based identification to objects by implementing the
 * {@link Identifiable} interface.
 *
 * @param  <T> the identifiable class.
 *
 * @author Patrick Jordan
 */
public abstract class AbstractIdentifiable<T extends Identifiable> implements Identifiable<T> {
    /**
     * The string identifier.
     */
    private String id;

    /**
     * Creates a new identifiable.
     * @param id the identifier.
     */
    protected AbstractIdentifiable(final String id) {
        setID(id);
    }

    /**
     * Returns the identifier.
     *
     * @return the identifier.
     */
    public final String getID() {
        return id;
    }

    /**
     * Sets the identifier.
     *
     * @param id the identifier.
     *
     * @throws NullPointerException if the identifier is null.
     */
    protected final void setID(final String id) throws NullPointerException {
        if (id == null) {
            throw new NullPointerException("Identifiable ID cannot be null");
        }
        this.id = id;
    }

    /**
     * Compares two identifiers lexicographically.
     *
     * @param t the identifiable to compare to.
     *
     * @return the value <code>0</code> if the argument identifier is equal to this identifier;
     *         a value less than <code>0</code> if this identifier is lexicographically less than
     *         the identifier argument; and a value greater than <code>0</code> if this identifier
     *          is lexicographically greater than the identifier argument.
     */
    public final int compareTo(final T t) {
        return getID().compareTo(t.getID());
    }


    /**
     * Checks for equality.
     * @param o the object to check equality against.
     * @return <code>true</code> if the object is of the right class and has the same identifier.
     */
    @Override
    public final boolean equals(final Object o) {

        if (this == o) {
            return true;
        }

        if (!checkClass(o)) {
            return false;
        }

        T that = (T) o;

        return id.equals(that.getID());
    }

    /**
     * Returns a hash code for the identifier.
     * @return a hash code for the identifier.
     */
    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    /**
     * Checks the class of an object in equals comparison.
     *
     * @param o the object
     * @return <code>true</code> if the object is of the right class.
     */
    protected abstract boolean checkClass(final Object o);

    @Override
    public String toString() {
        return id;
    }
}
