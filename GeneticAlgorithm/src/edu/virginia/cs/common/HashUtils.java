/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Original version of this code came from
 * http://www.javapractices.com/topic/TopicAction.do?Id=28
 * 
 * Collected methods which allow easy implementation of <code>hashCode</code>.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public final class HashUtils {

    /**
     * An initial value for a <code>hashCode</code>, to which is added contributions from
     * fields. Using a non-zero value decreases collisions of <code>hashCode</code>
     * values.
     */
    public static final int SEED = 23;

    public static final int MAGIC_PRIME_NUMBER = 37;

    /**
     * booleans.
     */
    public static int hash(final int aSeed, final boolean aBoolean) {
        return firstTerm(aSeed) + (aBoolean ? 1 : 0);
    }

    /**
     * chars.
     */
    public static int hash(final int aSeed, final char aChar) {
        return firstTerm(aSeed) + aChar;
    }

    /**
     * ints.
     */
    public static int hash(final int aSeed, final int aInt) {
        /*
         * Implementation Note Note that byte and short are handled by this method,
         * through implicit conversion.
         */
        return firstTerm(aSeed) + aInt;
    }

    /**
     * longs.
     */
    public static int hash(final int aSeed, final long aLong) {
        return firstTerm(aSeed) + (int) (aLong ^ (aLong >>> 32));
    }

    /**
     * floats.
     */
    public static int hash(final int aSeed, final float aFloat) {
        return hash(aSeed, Float.floatToIntBits(aFloat));
    }

    /**
     * doubles.
     */
    public static int hash(final int aSeed, final double aDouble) {
        return hash(aSeed, Double.doubleToLongBits(aDouble));
    }

    /**
     * <code>aObject</code> is a possibly-null object field, and possibly an array.
     * 
     * If <code>aObject</code> is an array, then each element may be a primitive or a
     * possibly-null object.
     */
    public static int hash(final int aSeed, final Object aObject) {
        int result = aSeed;
        if (aObject == null) {
            result = hash(result, 0);
        }
        else if (aObject instanceof Collection<?>) {
            for (final Object o : (Collection<?>) aObject) {
                // recursive call!
                result = hash(result, o);
            }
        }
        else if (!isArray(aObject)) {
            result = hash(result, aObject.hashCode());
        }
        else {
            final int length = Array.getLength(aObject);
            for (int idx = 0; idx < length; ++idx) {
                final Object item = Array.get(aObject, idx);
                // recursive call!
                result = hash(result, item);
            }
        }
        return result;
    }

    private static int firstTerm(final int aSeed) {
        return MAGIC_PRIME_NUMBER * aSeed;
    }

    private static boolean isArray(final Object aObject) {
        return aObject.getClass().isArray();
    }
}
