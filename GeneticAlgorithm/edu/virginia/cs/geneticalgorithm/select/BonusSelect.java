/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.virginia.cs.common.utils.ArrayNumberUtils;
import edu.virginia.cs.common.utils.ListComparator;
import edu.virginia.cs.geneticalgorithm.distribution.Distribution;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;

/**
 * Bonus {@link Select} is a technique somewhat between {@link ParetoRankedSelect} and {@link StandardSelect}, where the algorithm
 * for StandardSelect is used, with bonus fitness values assigned in a manner similar to how ParetoRankedSelect prefers items that
 * are ranked higher.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Sep 11, 2011
 */
public final class BonusSelect implements Select {

    final List<Double> _placeMultiplier = new ArrayList<Double>();
    final List<Double> _rankPercents;

    private final Random _rng;

    /**
     * Constructor taking a {@link Random Random Number Generator}
     * @param rng {@link Random Random Number Generator} used by {@link SelectUtils#substituteSelect(Random, Distribution, List)}
     */
    public BonusSelect(final Random rng) {
        this(rng, null);
    }

    /**
     * Constructor taking a {@link Random Random Number Generator} and a List of place multipliers
     * @param rng {@link Random Random Number Generator} used by {@link SelectUtils#substituteSelect(Random, Distribution, List)}
     * @param placeMultiplier Multiplier to signify value of each place
     */
    public BonusSelect(final Random rng, final List<Double> placeMultiplier) {
        this(rng, placeMultiplier, Arrays.asList(0.3, 0.25, 0.2, 0.15, 0.1, 0.05));
    }

    /**
     * Constructor taking a {@link Random Random Number Generator}, a List of place multipliers, and a List of rank percentages
     * @param rng {@link Random Random Number Generator} used by {@link SelectUtils#substituteSelect(Random, Distribution, List)}
     * @param placeMultiplier Multiplier to signify value of each place
     * @param rankPercents Bonus percentages to give to each place (1st, 2nd, etc.)
     */
    public BonusSelect(final Random rng, final List<Double> placeMultiplier, final List<Double> rankPercents) {
        _rng = rng;
        if (placeMultiplier != null) {
            _placeMultiplier.addAll(placeMultiplier);
        }
        _rankPercents = new ArrayList<Double>(rankPercents);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.select.Select#select(edu.virginia.cs.geneticalgorithm.distribution.Distribution)
     */
    @Override
    public Genotype select(final Distribution distribution) {
        final int numFitnessValues = distribution.get(0).getFitnessValues().size();
        distribution.normalize();
        if (_placeMultiplier.isEmpty()) {
            for (int i = 0; i < numFitnessValues; ++i) {
                _placeMultiplier.add(Double.valueOf(1.0));
            }
        }
        final List<List<Double>> enumeratedFitnessValues = SelectUtils.enumerateFitnessValues(distribution);
        final int numIndividuals = distribution.size();
        final List<Double> bonusFitnessValues = new ArrayList<Double>();
        for (int j = 0; j < numIndividuals; ++j) {
            bonusFitnessValues.add(distribution.get(j).getValue());
        }
        for (int i = 0; i < numFitnessValues; ++i) {
            final ListComparator<Double> lc = new ListComparator<Double>(i + 1);
            Collections.sort(enumeratedFitnessValues, lc);
            for (int j = 0; j < Math.min(numIndividuals, _rankPercents.size()); ++j) {
                if (Math.abs(distribution.get(j).getFitnessValues().get(i)) < 1E-10) break;
                final double rankValue = _placeMultiplier.get(i) * _rankPercents.get(j);
                bonusFitnessValues.set(j, bonusFitnessValues.get(j) + rankValue);
            }
        }
        return SelectUtils.substituteSelect(_rng, distribution, ArrayNumberUtils.normalize(bonusFitnessValues));
    }
}