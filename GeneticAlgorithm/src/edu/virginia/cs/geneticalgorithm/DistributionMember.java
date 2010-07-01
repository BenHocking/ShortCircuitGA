/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.ArrayList;
import java.util.List;

import edu.virginia.cs.common.OrderedPair;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public final class DistributionMember extends OrderedPair<List<Double>, Genotype> {

    /**
     * @param s
     * @param i
     */
    public DistributionMember(final Double v, final List<Double> s, final Genotype i) {
        super(appendToList(v, s), i);
    }

    private static List<Double> appendToList(final Double v, final List<Double> l) {
        final List<Double> retval = new ArrayList<Double>();
        retval.add(v);
        retval.addAll(l);
        return retval;
    }

    /**
     * Copy constructor. Warning: requires {@link Genotype} to implement clone correctly
     * @param toCopy
     */
    public DistributionMember(final DistributionMember toCopy) {
        super(appendToList(toCopy.getValue(), toCopy.getFitnessValues()), toCopy.getGenotype().clone());
    }

    public Double getValue() {
        return getFirst().get(0);
    }

    public List<Double> getFitnessValues() {
        final List<Double> retval = new ArrayList<Double>(getFirst());
        retval.remove(0);
        return retval;
    }

    public Genotype getGenotype() {
        return getLast();
    }

}
