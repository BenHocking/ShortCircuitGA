/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.data;

/**
 * History with no memory
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @param <T> Type of item to not store in history
 * @since Jun 28, 2011
 */
public class NullHistory<T> implements History<T> {

    /**
     * @see edu.virginia.cs.data.History#add(java.lang.Object)
     */
    @Override
    public boolean add(final T item) {
        return false;
    }

    /**
     * @see edu.virginia.cs.data.History#get(int)
     */
    @Override
    public T get(final int i) {
        return null;
    }

}
