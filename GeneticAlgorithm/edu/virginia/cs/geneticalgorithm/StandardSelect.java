/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.Random;

/**
 * Standard {@link Select} class that selects a member by chance proportionately to its {@link Fitness} relative to the total
 * {@link Fitness} of the population.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public final class StandardSelect implements Select {

    private final Random _rng;

    /**
     * Constructor taking a {@link Random Random Number Generator}
     * @param rng {@link Random Random Number Generator} used by {@link #select}
     */
    public StandardSelect(final Random rng) {
        _rng = rng;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Select#select(edu.virginia.cs.geneticalgorithm.Distribution)
     */
    @Override
    public Genotype select(final Distribution distribution) {
        distribution.normalize();
        final double selector = _rng.nextDouble();
        double totalProb = 0;
        for (final DistributionMember m : distribution) {
            totalProb += m.getValue();
            if (totalProb > selector) return m.getLast();
        }
        // Default to last one in case of rounding error
        return distribution.getLast().getGenotype();
    }
}