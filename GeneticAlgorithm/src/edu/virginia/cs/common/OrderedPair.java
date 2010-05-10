/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

import static edu.virginia.cs.common.EqualUtils.eq;
import static edu.virginia.cs.common.HashUtils.SEED;
import static edu.virginia.cs.common.HashUtils.hash;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Wrapper around a pair of objects where the first object represents something different than the second object
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public class OrderedPair<S, T> extends Pair<S, T> {

    /**
     * @param s
     * @param t
     */
    public OrderedPair(final S s, final T t) {
        super(s, t);
    }

    private Class<?> getFirstClass() {
        return getFirst() != null ? getFirst().getClass() : null;
    }

    private Class<?> getLastClass() {
        return getLast() != null ? getLast().getClass() : null;
    }

    @SuppressWarnings("unchecked")
    public <U> Collection<U> asCollection(final Class<U> clazz) {
        if (!(eq(getFirstClass(), clazz) && eq(getLastClass(), clazz))) {
            throw new RuntimeException("Attempted to crease list of incompatible type '" + clazz.getSimpleName() + "'");
        }
        final Collection<U> retval = new ArrayList<U>();
        retval.add((U) getFirst());
        retval.add((U) getLast());
        return retval;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof OrderedPair<?, ?>)) return false;
        final OrderedPair<?, ?> p = (OrderedPair<?, ?>) o;
        return eq(getFirst(), p.getFirst()) && eq(getLast(), p.getLast());
    }

    @Override
    public int hashCode() {
        return hash(hash(SEED, getFirst()), getLast());
    }
}
