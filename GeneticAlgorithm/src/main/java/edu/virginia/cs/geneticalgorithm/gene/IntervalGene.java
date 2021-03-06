/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.gene;

import java.util.Random;

import edu.virginia.cs.common.utils.HashUtils;
import edu.virginia.cs.common.utils.MathUtils;

/**
 * Gene initially constrained to [0,1). The value is allowed to grow outside these bounds internally as a buffer against going back
 * inside the [0,1) interval. However, externally, the reported value is always in the range [0,1].
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public class IntervalGene implements Gene {

    private final double _value;
    private final double _sigma;

    /**
     * Constructor
     * @param value Initial value of the {@link Gene}
     * @param sigma Standard deviation to use when mutating the {@link Gene}
     */
    public IntervalGene(final double value, final double sigma) {
        _value = value;
        _sigma = sigma;
    }

    /**
     * Constructor for non-mutating Gene
     * @param value Value of the {@link Gene}
     */
    public IntervalGene(final double value) {
        this(value, 0);
    }

    /**
     * Constructor for mutating Gene with initial value of 0.5 and standard deviation of same
     */
    public IntervalGene() {
        this(0.5, 0.5);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.gene.Gene#generate(java.util.Random)
     */
    @Override
    public Gene generate(final Random rng) {
        if (_sigma == 0) {
            // Assume no deviation is wanted
            return new IntervalGene(_value, _sigma);
        }
        // The default behavior (if _sigma is non-zero) is uniform distribution
        return new IntervalGene(rng.nextDouble(), _sigma);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.gene.Gene#mutate(java.util.Random)
     */
    @Override
    public Gene mutate(final Random rng) {
        final double rawGaussian = rng.nextGaussian();
        final double newVal = _value + rawGaussian * _sigma;
        return new IntervalGene(newVal, _sigma);
    }

    /**
     * @param rng Random number generator used to perform the next mutation
     * @param sigma Standard deviation to use when mutating the {@link Gene}
     * @return Mutated gene
     * @see edu.virginia.cs.geneticalgorithm.gene.Gene#mutate(java.util.Random)
     */
    public Gene mutate(final Random rng, final double sigma) {
        final double rawGaussian = rng.nextGaussian();
        final double newVal = _value + rawGaussian * sigma;
        return new IntervalGene(newVal, sigma);
    }

    /**
     * @return Double value represented by this gene.
     */
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

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Gene g) {
        if (!(g instanceof IntervalGene)) throw new IllegalArgumentException("Genes must be of the same type");
        final IntervalGene ig = (IntervalGene) g;
        return (int) Math.signum(_value - ig._value);
    }

    @Override
    public String toString() {
        return "IntervalGene (" + _value + ", " + _sigma + ")";
    }
}
