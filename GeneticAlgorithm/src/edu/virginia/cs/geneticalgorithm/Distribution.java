/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.ArrayList;
import java.util.List;

import static edu.virginia.cs.common.ArrayNumberUtils.*;
import edu.virginia.cs.common.IntegerRange;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
final class Distribution extends ArrayList<DistributionMember> {

    public Distribution() {
        super();
    }

    /**
     * @param distribution
     */
    public Distribution(final Distribution distribution) {
        super(distribution);
    }

    public DistributionMember getLast() {
        return get(size() - 1);
    }

    public void removeDuplicates() {
        final List<Boolean> duplicates = new ArrayList<Boolean>(size());
        for (int i = 0; i < size(); ++i) {
            if (duplicates.get(i) == null) {
                duplicates.set(i, false);
                final DistributionMember cf = get(i);
                for (int j = i + 1; j < size(); ++j) {
                    if (cf.getGenotype().equals(get(j).getGenotype())) {
                        duplicates.set(j, true);
                    }
                }
            }
        }
        for (int i = size() - 1; i >= 0; --i) {
            if (duplicates.get(i)) {
                remove(i);
            }
        }
    }

    public boolean hasValues() {
        for (final DistributionMember m : this) {
            if (m.getValue() == null) return false;
        }
        return true;
    }

    public void normalize() {
        if (!hasValues()) return; // Not ready to normalize yet
        final List<Double> representativeList = get(0).getFitnessValues();
        final List<Double> sumList = new ArrayList<Double>(representativeList.size());
        for (@SuppressWarnings("unused")
        final Integer i : new IntegerRange(representativeList.size())) {
            sumList.add(0.0);
        }
        for (final DistributionMember m : this) {
            accum(sumList, m.getFitnessValues());
        }
        // Make sure it hasn't already been normalized
        final double sum = sum(sumList);
        if (Math.abs(sum - 1) > 0.0001) {
            final Distribution copy = new Distribution(this);
            clear();
            for (final DistributionMember m : copy) {
                add(new DistributionMember(divide(m.getFitnessValues(), sum), m.getGenotype()));
            }
        }
    }
}
