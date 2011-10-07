/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class allowing easy iterating over a range of integers.
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 4, 2010
 */
public final class IntegerRange extends ArrayList<Integer> {

    /**
     * Constructor for iterating over the integers from 0 to <tt>to - 1</tt>.
     * @param to Upper bound (exclusive)
     */
    public IntegerRange(final int to) {
        this(0, to);
    }

    /**
     * Constructor for iterating over the integers from <tt>from</tt> to <tt>to - 1</tt>.
     * @param from Lower bound (inclusive)
     * @param to Upper bound (exclusive)
     */
    public IntegerRange(final int from, final int to) {
        this(from, (from <= to ? 1 : -1), to);
    }

    /**
     * Constructor for iterating over the integers from <tt>from</tt> to <tt>to - 1</tt> with step size <tt>step</tt>.
     * @param from Lower bound (inclusive)
     * @param step Step size
     * @param to Upper bound (exclusive)
     */
    public IntegerRange(final int from, final int step, final int to) {
        for (int i = from; step > 0 ? (i < to) : (i > to); i += step) {
            add(i);
        }
    }

    /**
     * @return this range as a Set
     */
    public Set<Integer> asSet() {
        return new HashSet<Integer>(this);
    }
}
