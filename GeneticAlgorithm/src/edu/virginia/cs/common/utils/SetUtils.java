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
     * @param <T>
     * @param a
     * @param b
     * @return
     */
    public static <T> Set<T> intersection(final Set<T> a, final Set<T> b) {
        final Set<T> retval = new HashSet<T>(a);
        retval.retainAll(b);
        return retval;
    }
}
