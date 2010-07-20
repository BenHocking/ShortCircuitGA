/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import static edu.virginia.cs.common.utils.EqualUtils.eq;
import static edu.virginia.cs.common.utils.HashUtils.SEED;
import static edu.virginia.cs.common.utils.HashUtils.hash;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Pair where the order doesn't matter
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @param <S> Class of both items in the UnorderedPair
 * @since Apr 24, 2010
 */
public final class UnorderedPair<S> extends Pair<S, S> {

    /**
     * Constructor
     * @param s First item in the UnorderedPair
     * @param t Second item in the UnorderedPair
     */
    public UnorderedPair(final S s, final S t) {
        super(s, t);
    }

    /**
     * @return UnorderedPair as a {@link java.util.Collection Collection} of class S
     */
    public Collection<S> asCollection() {
        final Collection<S> retval = new ArrayList<S>();
        retval.add(getFirst());
        retval.add(getLast());
        return retval;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof UnorderedPair<?>)) return false;
        final UnorderedPair<?> p = (UnorderedPair<?>) o;
        return (eq(getFirst(), p.getFirst()) && eq(getLast(), p.getLast()))
               || (eq(getFirst(), p.getLast()) && eq(getLast(), p.getFirst()));
    }

    /**
     * Hash should be the same if pairs are switched
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return hash(SEED, getFirst()) + hash(SEED, getLast());
    }
}
