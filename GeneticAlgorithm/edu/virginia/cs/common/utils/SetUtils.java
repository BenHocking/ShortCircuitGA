/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Static utility methods for sets
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 12, 2011
 */
public class SetUtils {

    /**
     * @param <T> Generic type
     * @param a First set
     * @param b Second set
     * @return Intersection of set a and b (never null, even if a and/or b are null)
     * @post a and b are unmodified
     */
    public static <T> Set<T> intersection(final Set<T> a, final Set<T> b) {
        final Set<T> retval = a != null ? new HashSet<T>(a) : new HashSet<T>();
        retval.retainAll(b);
        return retval;
    }

    /**
     * @param <T> Generic type
     * @param a First set
     * @param b Second set
     * @return Union of set a and b (never null, even if a and/or b are null)
     * @post a and b are unmodified
     */
    public static <T> Set<T> union(final Set<T> a, final Set<T> b) {
        final Set<T> retval = a != null ? new HashSet<T>(a) : new HashSet<T>();
        if (b != null) {
            retval.addAll(b);
        }
        return retval;
    }

    /**
     * @param <T> Generic type
     * @param a First set
     * @param b Second set
     * @return Whether b is a subset of a
     * @post a and b are unmodified
     */
    public static <T> boolean isSubset(final Set<T> a, final Set<T> b) {
        // The size of the intersected set will equal the size of b if and only if b is a subset of a
        return b == null ? true : intersection(a, b).size() == b.size();
    }

    /**
     * @param <T> Generic type
     * @param a First set
     * @param b Second set
     * @return All elements of a not in b (never null, even if a and/or b are null)
     * @post a and b are unmodified
     */
    public static <T> Set<T> difference(final Set<T> a, final Set<T> b) {
        final Set<T> retval = a != null ? new HashSet<T>(a) : new HashSet<T>();
        if (b != null) {
            retval.removeAll(b);
        }
        return retval;
    }

    /**
     * @param <T> Generic type of a
     * @param <U> Generic type of b
     * @param a First set
     * @param b Second set
     * @return Cartesian product of a and b (never null, even if a and/or b are null)
     * @post a and b are unmodified
     */
    public static <T, U> Set<OrderedPair<T, U>> product(final Set<T> a, final Set<U> b) {
        final Set<OrderedPair<T, U>> retval = new HashSet<OrderedPair<T, U>>();
        if (a != null && b != null) {
            for (final T t : a) {
                for (final U u : b) {
                    retval.add(new OrderedPair<T, U>(t, u));
                }
            }
        }
        return retval;
    }
}
