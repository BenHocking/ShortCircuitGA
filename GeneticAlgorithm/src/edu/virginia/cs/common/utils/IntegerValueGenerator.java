/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

/**
 * {@link edu.virginia.cs.common.utils.ValueGenerator ValueGenerator} that generates a value from a range of integers
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 2, 2010
 */
public final class IntegerValueGenerator implements ValueGenerator {

    private final int _min;
    private final int _max;

    /**
     * Constructor specifying the lower and upper bounds for the integer to return
     * @param min Lower bound (inclusive)
     * @param max Upper bound (inclusive)
     */
    public IntegerValueGenerator(final int min, final int max) {
        _min = Math.min(min, max);
        _max = Math.max(min, max);
    }

    /**
     * @see edu.virginia.cs.common.utils.ValueGenerator#generate(double)
     */
    @Override
    public String generate(final double x) {
        return String.valueOf(MathUtils.scaleInt(_min, x, _max));
    }

    /**
     * @see edu.virginia.cs.common.utils.ValueGenerator#invert(java.lang.String)
     */
    @Override
    public double invert(final String s) {
        return MathUtils.scaleIntInverse(_min, Integer.valueOf(s), _max);
    }

}
