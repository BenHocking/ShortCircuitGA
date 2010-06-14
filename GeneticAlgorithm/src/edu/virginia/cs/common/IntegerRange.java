/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class allowing easy iterating over a range of integers.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 4, 2010
 */
public final class IntegerRange implements Iterable<Integer>, Iterator<Integer> {

    private int _currentlyAt;
    private final int _step;
    private final int _stopAt;

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
        assert (from == to || (from < to && step > 0) || (from > to && step < 0));
        _currentlyAt = from;
        _step = step;
        _stopAt = to;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Integer> iterator() {
        return this;
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return (_step > 0) ? (_currentlyAt < _stopAt) : (_currentlyAt > _stopAt);
    }

    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public Integer next() {
        if (!hasNext()) throw new NoSuchElementException("next called out of range");
        final Integer retval = _currentlyAt;
        _currentlyAt += _step;
        return retval;
    }

    /**
     * Not supported.
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        /* do nothing, not supported */
    }
}
