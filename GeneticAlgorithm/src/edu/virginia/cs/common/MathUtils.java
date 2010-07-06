/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

/**
 * Wrapper around common math utilities
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public final class MathUtils {

    /**
     * Return x, unless x is less than min (then use min), or greater than max (then use max).
     * <p>
     * Note that if min > max, then this function will always return min
     * 
     * @param min Minimum allowable value
     * @param max Maximum allowable value
     * @param x Variable to impose bounds on
     * @return Bounded x
     */
    public static double imposeBounds(final double min, final double x, final double max) {
        return Math.min(max, Math.max(min, x));
    }

    /**
     * Scales range from min to max instead of from 0 to 1
     * 
     * @param min minimum value to return (unless x < 0)
     * @param x result of a function that ranges from 0 to 1
     * @param max maximum value to return (unless x > 1)
     * @return scaled result between min and max (unless x is less than 0 or greater than 1)
     * @see #scaleInt(int, double, int)
     * @see #scale(double, double, double, boolean)
     */
    public static double scale(final double min, final double x, final double max) {
        return (x * (max - min)) + min;
    }

    /**
     * Scales range from min to max instead of from 0 to 1
     * 
     * @param min minimum value to return
     * @param x result of a function that ranges from 0 to 1
     * @param max maximum value to return
     * @param enforceBounds Whether to strictly enforce the resultant to be between min and max, even if x is not between 0 and 1
     * @return scaled result between min and max (even if x is outside 0..1 boundary)
     * @see #scaleInt(int, double, int)
     * @see #scale(double, double, double)
     * @see #imposeBounds(double, double, double)
     */
    public static double scale(final double min, final double x, final double max, final boolean enforceBounds) {
        if (!enforceBounds) return scale(min, x, max);
        return scale(min, imposeBounds(0, x, 1), max);
    }

    /**
     * Scales range from min to max instead of from 0 to 1
     * 
     * @param min minimum value to return
     * @param x result of a function that ranges from 0 to 1
     * @param max maximum value to return
     * @return scaled result between min and max (even if x is less than 0 or greater than 1)
     * @see #scale(double, double, double)
     * @see #scale(double, double, double, boolean)
     */
    public static int scaleInt(final int min, final double x, final int max) {
        final int retval = (int) (Math.floor(x * (max - min + 1)) + min);
        return retval < max ? (retval > min ? retval : min) : max;
    }
}
