/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.virginia.cs.common.utils.ArrayNumberUtils;
import edu.virginia.cs.common.utils.ListComparator;
import edu.virginia.cs.geneticalgorithm.distribution.Distribution;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;

/**
 * Pareto Ranked {@link Select} class that selects a member similarly to the {@link StandardSelect} class, but after first
 * rank-ordering each fitness component and generating a temporary fitness function based off this rank-ordering, combined with an
 * optional weighted component
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public final class ParetoRankedSelect implements Select {

    final List<Double> _rankWeighting = new ArrayList<Double>();

    private final Random _rng;

    /**
     * Constructor taking a {@link Random Random Number Generator}
     * @param rng {@link Random Random Number Generator} used by {@link SelectUtils#substituteSelect(Random, Distribution, List)}
     */
    public ParetoRankedSelect(final Random rng) {
        this(rng, null);
    }

    /**
     * Constructor taking a {@link Random Random Number Generator}
     * @param rng {@link Random Random Number Generator} used by {@link SelectUtils#substituteSelect(Random, Distribution, List)}
     * @param rankWeighting Value to assign to each place in distribution members' fitness values list
     */
    public ParetoRankedSelect(final Random rng, final List<Double> rankWeighting) {
        _rng = rng;
        if (rankWeighting != null) {
            _rankWeighting.addAll(rankWeighting);
        }
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.select.Select#select(edu.virginia.cs.geneticalgorithm.distribution.Distribution)
     */
    @Override
    public Genotype select(final Distribution distribution) {
        final int numFitnessValues = distribution.get(0).getFitnessValues().size();
        if (_rankWeighting.isEmpty()) {
            for (int i = 0; i < numFitnessValues; ++i) {
                _rankWeighting.add(Double.valueOf(1.0));
            }
        }
        final List<List<Double>> enumeratedFitnessValues = SelectUtils.enumerateFitnessValues(distribution);
        final int numIndividuals = distribution.size();
        final List<Double> paretoFitnessValues = new ArrayList<Double>(Collections.nCopies(numIndividuals, Double.valueOf(0.0)));
        for (int i = 0; i < numFitnessValues; ++i) {
            final ListComparator<Double> lc = new ListComparator<Double>(i);
            Collections.sort(enumeratedFitnessValues, lc);
            for (int j = 0; j < numIndividuals; ++j) {
                final double rankValue = _rankWeighting.get(i) * (numIndividuals - j);
                paretoFitnessValues.set(j, paretoFitnessValues.get(j) + rankValue);
            }
        }
        return SelectUtils.substituteSelect(_rng, distribution, ArrayNumberUtils.normalize(paretoFitnessValues));
    }
}