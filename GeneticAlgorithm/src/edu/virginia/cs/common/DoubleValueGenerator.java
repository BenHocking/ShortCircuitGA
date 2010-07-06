/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

/**
 * {@link edu.virginia.cs.common.ValueGenerator ValueGenerator} that generates a value between an upper and lower double value
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
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
     * @see edu.virginia.cs.common.ValueGenerator#generate(double)
     */
    @Override
    public String generate(final double x) {
        return String.valueOf(MathUtils.scale(_min, x, _max, true));
    }

}
