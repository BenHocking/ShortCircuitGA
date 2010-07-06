/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import edu.virginia.cs.common.UnorderedPair;

/**
 * Class governing how a population of individuals in a genetic algorithm reproduce. It requires the specification of a
 * {@link Fitness} class, a {@link Select} class, and a {@link Crossover} class.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public final class Reproduction {

    private final boolean _allowDuplicates;
    private final boolean _keepAllHistory;
    private final List<List<Double>> _bestFits = new ArrayList<List<Double>>();
    private final List<Double> _meanFits = new ArrayList<Double>();
    private final List<Distribution> _popHist = new ArrayList<Distribution>();
    private int _numElites = 0;
    public static int DEBUG_LEVEL = 1;

    /**
     * Constructor specifying whether to allow duplicates and whether to keep all history (requires more memory)
     * @param allowDuplicates Whether to allow duplicate individuals in a population
     * @param keepAllHistory Whether to retain history from generation to generation
     */
    public Reproduction(final boolean allowDuplicates, final boolean keepAllHistory) {
        _allowDuplicates = allowDuplicates;
        _keepAllHistory = keepAllHistory;
    }

    /**
     * @param population The population that will be reproducing
     * @param fitFn {@link Fitness} function used to determine which members reproduce and how well
     * @param selFn
     * @param xFn
     * @return The new generation
     */
    public List<Genotype> reproduce(final List<Genotype> population, final Fitness fitFn, final Select selFn, final Crossover xFn) {
        return reproduce(population, population.size(), fitFn, selFn, xFn);
    }

    public List<Genotype> reproduce(final List<Genotype> population, final int newPopSize, final Fitness fitFn, final Select selFn,
                                    final Crossover xFn) {
        double totalFit = 0;
        double bestFit = 0; // Best is maximal, and all fitness values need to be positive
        List<Double> bestFitList = new ArrayList<Double>();
        final Distribution distribution = new Distribution();
        int ctr = 0;
        if (DEBUG_LEVEL == 1) System.out.print("Evaluating individual:");
        for (final Genotype i : population) {
            if (DEBUG_LEVEL > 1) System.out.println("Finding fitness of individual #" + ++ctr);
            if (DEBUG_LEVEL == 1) System.out.print(" " + ++ctr);
            final List<Double> fitList = fitFn.fitnessValues(i);
            final double fit = fitFn.totalFitness(i);
            if (DEBUG_LEVEL > 1) System.out.println("\tfitness: " + fit);
            distribution.add(new DistributionMember(fit, fitList, i));
            totalFit += fit;
            if (fit > bestFit) {
                bestFit = fit;
                bestFitList = fitList;
            }
        }
        if (DEBUG_LEVEL == 1) System.out.println(" ... done");
        // Calculate statistics
        final List<Double> best = new ArrayList<Double>();
        best.add(bestFit);
        best.addAll(bestFitList);
        _bestFits.add(best);
        _meanFits.add(Double.valueOf(totalFit / population.size()));
        final Collection<Genotype> retval = _allowDuplicates ? new ArrayList<Genotype>() : new HashSet<Genotype>();
        // Sorts with most fit members first
        Collections.sort(distribution);
        for (int i = 0; i < _numElites; ++i) {
            retval.add(distribution.get(i).getGenotype().clone());
        }
        while (retval.size() < newPopSize) {
            final Genotype mom = selFn.select(distribution);
            final Genotype dad = selFn.select(distribution);
            final UnorderedPair<Genotype> kids = xFn.crossover(mom, dad);
            retval.add(kids.getFirst());
            if (retval.size() < newPopSize) {
                retval.add(kids.getLast());
            }
        }
        if (_keepAllHistory) {
            _popHist.add(new Distribution(distribution));
        }
        return _allowDuplicates ? (List<Genotype>) retval : new ArrayList<Genotype>(retval);
    }

    public void setNumElites(final int numElites) {
        _numElites = numElites;
    }

    public int getNumElites() {
        return _numElites;
    }

    /**
     * @return
     */
    public List<Double> getBestFit() {
        return _bestFits.get(_bestFits.size() - 1);
    }

    /**
     * @return Copy of history of best fits
     */
    public List<List<Double>> getBestFits() {
        return new ArrayList<List<Double>>(_bestFits);
    }

    /**
     * 
     * @return
     */
    public List<Distribution> getHistory() {
        return _popHist;
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
