/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * Based very closely on HashUtils, as any class modifying equals should modify hashCode (and vice-versa)
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public final class EqualUtils {

    /**
     * booleans.
     */
    public static boolean eq(final boolean a, final boolean b) {
        return a == b;
    }

    /**
     * chars.
     */
    public static boolean eq(final char a, final char b) {
        return a == b;
    }

    /**
     * ints.
     */
    public static boolean eq(final int a, final int b) {
        return a == b;
    }

    /**
     * longs.
     */
    public static boolean eq(final long a, final long b) {
        return a == b;
    }

    /**
     * floats.
     */
    public static boolean eq(final float a, final float b) {
        return a == b || Float.floatToIntBits(a) == Float.floatToIntBits(b);
    }

    /**
     * doubles.
     */
    public static boolean eq(final double a, final double b) {
        return a == b || Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
    }

    /**
     * <code>aObject</code> is a possibly-null object field, and possibly an array.
     * 
     * If <code>aObject</code> is an array, then each element may be a primitive or a possibly-null object.
     */
    public static boolean eq(final Object a, final Object b) {
        if (a == null) return b == null;
        if (a instanceof Collection<?> && b instanceof Collection<?>) {
            final Collection<?> c1 = (Collection<?>) a;
            final Collection<?> c2 = (Collection<?>) b;
            if (c1.size() != c2.size()) return false;
            final Iterator<?> it1 = c1.iterator();
            final Iterator<?> it2 = c2.iterator();
            while (it1.hasNext()) {
                if (!eq(it1.next(), it2.next())) return false;
            }
            return true;
        }
        if (!isArray(a)) {
            return a.equals(b);
        }
        final int length = Array.getLength(a);
        if (Array.getLength(b) != length) return false;
        for (int idx = 0; idx < length; ++idx) {
            final Object item1 = Array.get(a, idx);
            final Object item2 = Array.get(b, idx);
            // recursive call!
            if (!eq(item1, item2)) return false;
        }
        return true;
    }

    private static boolean isArray(final Object aObject) {
        return aObject.getClass().isArray();
    }
}
