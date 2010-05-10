/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.List;

import edu.virginia.cs.common.OrderedPair;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
final class DistributionMember extends OrderedPair<List<Double>, Genotype> {

    /**
     * @param s
     * @param i
     */
    public DistributionMember(final List<Double> s, final Genotype i) {
        super(s, i);
    }

    public Double getValue() {
        Double retval = Double.valueOf(0);
        for (final Double d : getFirst()) {
            retval += d;
        }
        return retval;
    }

    public List<Double> getFitnessValues() {
        return getFirst();
    }

    public Genotype getGenotype() {
        return getLast();
    }

}
