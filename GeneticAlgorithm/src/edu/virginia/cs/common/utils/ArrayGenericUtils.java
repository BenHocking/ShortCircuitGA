/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.util.Iterator;
import java.lang.Comparable;

/**
 * Generic array utility functions
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 3, 2010
 */
public class ArrayGenericUtils {

    /**
     * Compares two lists (or other {@link java.lang.Iterable Iterable}) to see which one is "less". T must be
     * {@link java.lang.Comparable Comparable}.
     * @see java.lang.Comparable#compareTo(Object)
     * @param <T> Class that implements {@link java.lang.Comparable Comparable}
     * @param l1 {@link java.lang.Iterable Iterable} (e.g., {@link java.util.List List}) of item T.
     * @param l2 {@link java.lang.Iterable Iterable} (e.g., {@link java.util.List List}) of item T
     * @return Value less than 0 if l1 is less than l2, value greater than 0 if l2 is less than l1, else zero
     * @exception RuntimeException if T does not implement {@link java.lang.Comparable Comparable}
     */
    @SuppressWarnings("unchecked")
    static public <T> int compare(final Iterable<T> l1, final Iterable<T> l2) {
        // A null list is "less than" a non-null list
        if (l1 == null) return (l2 != null) ? -1 : 0;
        if (l2 == null) return 1;
        // The smaller list determines how many iterations we can perform
        final Iterator<T> i1 = l1.iterator();
        final Iterator<T> i2 = l2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            final T c1 = i1.next();
            final T c2 = i2.next();
            if (!(c1 instanceof Comparable<?>)) throw new RuntimeException("Items must be Comparable");
            final Comparable<T> cc1 = (Comparable<T>) c1;
            final int result = cc1.compareTo(c2);
            if (result != 0) return result;
        }
        // If they're the same up to the last iteration, than the short list is "less"
        if (i1.hasNext()) return 1;
        if (i2.hasNext()) return -1;
        // Else, they're (comparably) the same
        return 0;
    }
}
