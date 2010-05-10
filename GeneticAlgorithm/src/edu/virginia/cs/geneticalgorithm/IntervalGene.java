/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.Random;

import edu.virginia.cs.common.HashUtils;
import edu.virginia.cs.common.MathUtils;

/**
 * Gene initially constrained to [0,1). The value is allowed to grow outside these bounds internally as a buffer against going back
 * inside the [0,1) interval. However, externally, the reported value is always in the range [0,1].
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public final class IntervalGene implements Gene {

    private final double _value;
    private final double _sigma;

    public IntervalGene(final double value, final double sigma) {
        _value = value;
        _sigma = sigma;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Gene#generate(java.util.Random)
     */
    @Override
    public Gene generate(final Random rng) {
        return new IntervalGene(rng.nextDouble(), _sigma);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Gene#mutate(java.util.Random)
     */
    @Override
    public Gene mutate(final Random rng) {
        final double rawGaussian = rng.nextGaussian();
        final double newVal = _value + rawGaussian * _sigma;
        return new IntervalGene(newVal, _sigma);
    }

    public double getValue() {
        return MathUtils.imposeBounds(0.0, _value, 1.0);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashUtils.hash(0, _value);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj.getClass().equals(getClass()) && _value == ((IntervalGene) obj)._value;
    }
}
