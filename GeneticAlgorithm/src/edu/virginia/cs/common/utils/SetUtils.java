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
     * @return Intersection of set a and b
     * @post a and b are unmodified
     */
    public static <T> Set<T> intersection(final Set<T> a, final Set<T> b) {
        final Set<T> retval = new HashSet<T>(a);
        retval.retainAll(b);
        return retval;
    }

    /**
     * @param <T> Generic type
     * @param a First set
     * @param b Second set
     * @return Union of set a and b
     * @post a and b are unmodified
     */
    public static <T> Set<T> union(final Set<T> a, final Set<T> b) {
        final Set<T> retval = new HashSet<T>(a);
        retval.addAll(b);
        return retval;
    }

    /**
     * @param <T> Generic type
     * @param a First set
     * @param b Second set
     * @return All elements of a not in b
     * @post a and b are unmodified
     */
    public static <T> Set<T> difference(final Set<T> a, final Set<T> b) {
        final Set<T> retval = new HashSet<T>(a);
        retval.removeAll(b);
        return retval;
    }

    /**
     * @param <T> Generic type of a
     * @param <U> Generic type of b
     * @param a First set
     * @param b Second set
     * @return Cartesian product of a and b
     * @post a and b are unmodified
     */
    public static <T, U> Set<OrderedPair<T, U>> product(final Set<T> a, final Set<U> b) {
        final Set<OrderedPair<T, U>> retval = new HashSet<OrderedPair<T, U>>();
        for (final T t : a) {
            for (final U u : b) {
                retval.add(new OrderedPair<T, U>(t, u));
            }
        }
        return retval;
    }
}
