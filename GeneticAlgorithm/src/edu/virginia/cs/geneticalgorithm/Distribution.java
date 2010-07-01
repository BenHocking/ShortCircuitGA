/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public final class Distribution extends ArrayList<DistributionMember> {

    public Distribution() {
        super();
    }

    /**
     * Copy constructor with deep copy
     * @param distribution
     */
    public Distribution(final Distribution distribution) {
        super();
        for (final DistributionMember m : distribution) {
            add(new DistributionMember(m));
        }
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
        double sum = 0.0;
        for (final DistributionMember m : this) {
            sum += m.getValue();
        }
        // Make sure it hasn't already been normalized (and that we don't divide by zero)
        if (sum > 0 && Math.abs(sum - 1) > 0.0001) {
            final Distribution copy = new Distribution(this);
            clear();
            for (final DistributionMember m : copy) {
                add(new DistributionMember(m.getValue() / sum, m.getFitnessValues(), m.getGenotype()));
            }
        }
    }
}
