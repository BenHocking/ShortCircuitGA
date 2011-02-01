/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.ArrayList;
import java.util.List;

import edu.virginia.cs.common.utils.OrderedPair;

/**
 * A wrapper for a {@link Genotype} with its {@link Fitness} values and some helper functions.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public final class DistributionMember extends OrderedPair<List<Double>, Genotype> implements Comparable<DistributionMember> {

    /**
     * Constructor
     * @param v Overall {@link Fitness} of {@link Genotype} i.
     * @param s Multi-objective {@link Fitness} values.
     * @param i {@link Genotype} for which the {@link Fitness} values apply
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
     * @param toCopy DistributionMember to copy from
     */
    public DistributionMember(final DistributionMember toCopy) {
        super(appendToList(toCopy.getValue(), toCopy.getFitnessValues()), toCopy.getGenotype().clone());
    }

    /**
     * @return Overall {@link Fitness}
     */
    public Double getValue() {
        return getFirst().get(0);
    }

    /**
     * @return Multi-objective {@link Fitness} values.
     */
    public List<Double> getFitnessValues() {
        final List<Double> retval = new ArrayList<Double>(getFirst());
        retval.remove(0);
        return retval;
    }

    /**
     * @return {@link Genotype} for which the {@link Fitness} values apply
     */
    public Genotype getGenotype() {
        return getLast();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Fitness: ");
        sb.append(getValue());
        sb.append(", [");
        final List<Double> fitVals = getFitnessValues();
        for (int i = 0; i < fitVals.size(); ++i) {
            sb.append(fitVals.get(i));
            if (i < fitVals.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("], ");
        sb.append(getGenotype());
        return sb.toString();
    }

    /**
     * Return an ordering of DistributionMembers with most fit items first. Note that this means that most fit members are "less"
     * than less fit members, which might seem very non-intuitive.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final DistributionMember o) {
        // This is the most important bit.
        if (getValue() != o.getValue()) return (int) Math.signum(o.getValue() - getValue());
        // The rest is arbitrary and just designed to keep non-identical DistributionMembers from being equal
        final List<Double> fitVals = getFitnessValues();
        final List<Double> otherVals = o.getFitnessValues();
        for (int i = 0; i < fitVals.size(); ++i) {
            if (fitVals.get(i) != otherVals.get(i)) return (int) Math.signum(otherVals.get(i) - fitVals.get(i));
        }
        final Genotype g = getGenotype();
        final Genotype og = o.getGenotype();
        return g.compareTo(og);
    }

}
