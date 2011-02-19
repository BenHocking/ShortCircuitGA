/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Original version of this code came from http://www.javapractices.com/topic/TopicAction.do?Id=28
 * 
 * Collected methods which allow easy implementation of <code>hashCode</code>.
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public final class HashUtils {

    /**
     * An initial value for a <code>hashCode</code>, to which is added contributions from fields. Using a non-zero value decreases
     * collisions of <code>hashCode</code> values.
     */
    public static final int SEED = 23;

    /**
     * Multiplier for a previously generated hash code when moving on to the next field in an {@link java.lang.Object Object}
     */
    public static final int MAGIC_PRIME_NUMBER = 37;

    /**
     * Hash code generator for booleans.
     * @param aSeed Previously generated hash code
     * @param aBoolean Boolean to use for extending the previously generated hash code
     * @return New hash code
     */
    public static int hash(final int aSeed, final boolean aBoolean) {
        return firstTerm(aSeed) + (aBoolean ? 1 : 0);
    }

    /**
     * Hash code generator for chars.
     * @param aSeed Previously generated hash code
     * @param aChar Character to use for extending the previously generated hash code
     * @return New hash code
     */
    public static int hash(final int aSeed, final char aChar) {
        return firstTerm(aSeed) + aChar;
    }

    /**
     * Hash code generator for integers.
     * @param aSeed Previously generated hash code
     * @param aInt Integer to use for extending the previously generated hash code
     * @return New hash code
     */
    public static int hash(final int aSeed, final int aInt) {
        /*
         * Implementation Note Note that byte and short are handled by this method, through implicit conversion.
         */
        return firstTerm(aSeed) + aInt;
    }

    /**
     * Hash code generator for longs.
     * @param aSeed Previously generated hash code
     * @param aLong Long to use for extending the previously generated hash code
     * @return New hash code
     */
    public static int hash(final int aSeed, final long aLong) {
        return firstTerm(aSeed) + (int) (aLong ^ (aLong >>> 32));
    }

    /**
     * Hash code generator for floats.
     * @param aSeed Previously generated hash code
     * @param aFloat Float to use for extending the previously generated hash code
     * @return New hash code
     */
    public static int hash(final int aSeed, final float aFloat) {
        return hash(aSeed, Float.floatToIntBits(aFloat));
    }

    /**
     * Hash code generator for doubles.
     * @param aSeed Previously generated hash code
     * @param aDouble double to use for extending the previously generated hash code
     * @return New hash code
     */
    public static int hash(final int aSeed, final double aDouble) {
        return hash(aSeed, Double.doubleToLongBits(aDouble));
    }

    /**
     * Hash code generator for Objects.
     * @param aSeed Previously generated hash code
     * @param aObject a possibly-null object field, and possibly an array.
     * @return New hash code
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
