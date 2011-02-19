/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

/**
 * {@link edu.virginia.cs.common.utils.ValueGenerator ValueGenerator} that generates a value between an upper and lower double value
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 2, 2010
 */
public final class DoubleValueGenerator implements ValueGenerator {

    private final double _min;
    private final double _max;

    /**
     * Constructor specifying the lower and upper bounds for the double to return
     * @param min Lower bound
     * @param max Upper bound
     */
    public DoubleValueGenerator(final double min, final double max) {
        _min = Math.min(min, max);
        _max = Math.max(min, max);
    }

    /**
     * @see edu.virginia.cs.common.utils.ValueGenerator#generate(double)
     */
    @Override
    public String generate(final double x) {
        return String.valueOf(MathUtils.scale(_min, x, _max, true));
    }

    /**
     * @see edu.virginia.cs.common.utils.ValueGenerator#invert(java.lang.String)
     */
    @Override
    public double invert(final String s) {
        return MathUtils.scaleInverse(_min, Double.valueOf(s), _max);
    }

}
