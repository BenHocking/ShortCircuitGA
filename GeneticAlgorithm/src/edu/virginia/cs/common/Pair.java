/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

/**
 * Wrapper around a pair of objects
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @param <S> Class of first item in the Pair
 * @param <T> Class of second item in the Pair
 * @since Apr 24, 2010
 */
public abstract class Pair<S, T> {

    private final S _first;
    private final T _last;

    /**
     * Constructor
     * @param s First item in the pair
     * @param t Second item in the pair
     */
    public Pair(final S s, final T t) {
        _first = s;
        _last = t;
    }

    /**
     * @return First item in the pair
     */
    public S getFirst() {
        return _first;
    }

    /**
     * @return Second item in the pair
     */
    public T getLast() {
        return _last;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", _first, _last);
    }
}
