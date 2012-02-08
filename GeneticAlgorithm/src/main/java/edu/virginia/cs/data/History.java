/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.data;

/**
 * Stores a history of objects of type T
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @param <T> Type of objects to maintain a history for
 * @since Jun 28, 2011
 */
public interface History<T> {

    /**
     * Add an item to the history
     * @param item Item to add to the history
     * @return True if history changed due to addition of item
     */
    boolean add(final T item);

    /**
     * @param i Which item to get from the history
     * @return ith item added to the history
     */
    T get(final int i);
}
