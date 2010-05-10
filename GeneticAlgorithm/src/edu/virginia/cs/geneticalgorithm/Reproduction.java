/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.virginia.cs.common.UnorderedPair;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public final class Reproduction {

    private final boolean _allowDuplicates;
    private final List<Double> _bestFits = new ArrayList<Double>();
    private final List<Double> _meanFits = new ArrayList<Double>();

    public Reproduction(final boolean allowDuplicates) {
        _allowDuplicates = allowDuplicates;
    }

    public List<Genotype> reproduce(final List<Genotype> population, final Fitness fitFn, final Select selFn, final Crossover xFn) {
        return reproduce(population, population.size(), fitFn, selFn, xFn);
    }

    public List<Genotype> reproduce(final List<Genotype> population, final int newPopSize, final Fitness fitFn, final Select selFn,
                                    final Crossover xFn) {
        double totalFit = 0;
        double bestFit = 0; // Best is maximal, and all fitness values need to be positive
        final Distribution distribution = new Distribution();
        for (final Genotype i : population) {
            final List<Double> fitList = fitFn.fitness(i);
            double fit = 0.0;
            for (final Double d : fitList) {
                fit += d;
            }
            distribution.add(new DistributionMember(fitList, i));
            totalFit += fit;
            if (fit > bestFit) bestFit = fit;
        }
        // Calculate statistics
        _bestFits.add(Double.valueOf(bestFit));
        _meanFits.add(Double.valueOf(totalFit / population.size()));
        final Collection<Genotype> retval = _allowDuplicates ? new ArrayList<Genotype>() : new HashSet<Genotype>();
        while (retval.size() < newPopSize) {
            final Genotype mom = selFn.select(distribution);
            final Genotype dad = selFn.select(distribution);
            final UnorderedPair<Genotype> kids = xFn.crossover(mom, dad);
            retval.add(kids.getFirst());
            if (retval.size() < newPopSize) {
                retval.add(kids.getLast());
            }
        }
        return _allowDuplicates ? (List<Genotype>) retval : new ArrayList<Genotype>(retval);
    }

    /**
     * @return
     */
    public double getBestFit() {
        return _bestFits.get(_bestFits.size() - 1);
    }

    /**
     * @return Copy of history of best fits
     */
    public List<Double> getBestFits() {
        return new ArrayList<Double>(_bestFits);
    }

    /**
     * @return
     */
    public double getMeanFit() {
        return _meanFits.get(_meanFits.size() - 1);
    }

    /**
     * @return Copy of history of mean fits
     */
    public List<Double> getMeanFits() {
        return new ArrayList<Double>(_meanFits);
    }
}
