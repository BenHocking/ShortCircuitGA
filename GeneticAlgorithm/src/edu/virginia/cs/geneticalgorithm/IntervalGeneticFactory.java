/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * {@link GeneticFactory} for generating Select and Crossover functions compatible with {@link IntervalGene IntervalGenes}
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public final class IntervalGeneticFactory implements GeneticFactory {

    private final Random _rng;
    private final Select _select;
    private final Mutator _mutator;
    private final Crossover _xOver;

    /**
     * Constructor using default settings
     * @param seed Random seed to use for generating genetic features.
     */
    public IntervalGeneticFactory(final long seed) {
        this(seed, 0.03, 0.6, 0.25);
    }

    /**
     * Constructor with additional parameters
     * @param seed Random seed to use for generating genetic features.
     * @param mutateProb Probability (0 to 1) for each {@link Gene} to mutate
     * @param xOverProb Probability (0 to 1) that there will be any {@link Crossover Crossovers}
     * @param geneXOverProb If there are any {@link Crossover Crossovers}, probability for each {@link Gene} to cross over. Values
     * from 0.5-1 are essentially the same as 1-x (where x is the value given).
     */
    public IntervalGeneticFactory(final long seed, final double mutateProb, final double xOverProb, final double geneXOverProb) {
        _rng = new Random(seed);
        _select = new StandardSelect(_rng);
        _mutator = new StandardMutator(mutateProb, _rng);
        _xOver = new UniformCrossover(_mutator, xOverProb, geneXOverProb, _rng);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.GeneticFactory#getSelectFunction()
     */
    @Override
    public Select getSelectFunction() {
        return _select;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.GeneticFactory#getCrossoverFunction()
     */
    @Override
    public Crossover getCrossoverFunction() {
        return _xOver;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.GeneticFactory#createPopulation(int, int)
     */
    @Override
    public List<Genotype> createPopulation(final int numIndividuals, final int genotypeLength) {
        final List<Genotype> population = new ArrayList<Genotype>();
        final Gene basis = new IntervalGene(0.5, 0.2);
        for (int i = 0; i < numIndividuals; ++i) {
            population.add(new StandardGenotype(genotypeLength, basis, _rng));
        }
        return population;
    }
}
