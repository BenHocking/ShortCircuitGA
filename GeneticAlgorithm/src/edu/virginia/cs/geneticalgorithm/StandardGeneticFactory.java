/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Typical {@link GeneticFactory} dealing with {@link StandardGenotype StandardGenotypes} and using the {@link OnePointCrossover}.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public final class StandardGeneticFactory implements GeneticFactory {

    private final Random _rng;
    private final Select _select;
    private final Mutator _mutator;
    private final Crossover _xOver;

    /**
     * Constructor using default settings
     * @param seed Random seed to use for generating genetic features.
     */
    public StandardGeneticFactory(final long seed) {
        this(seed, 0.03, 0.6);
    }

    /**
     * Constructor with additional parameters
     * @param seed Random seed to use for generating genetic features.
     * @param mutateProb Probability (0 to 1) for each {@link Gene} to mutate
     * @param xOverProb Probability (0 to 1) that there will be any {@link Crossover Crossovers}
     */
    public StandardGeneticFactory(final long seed, final double mutateProb, final double xOverProb) {
        _rng = new Random(seed);
        _select = new StandardSelect(_rng);
        _mutator = new StandardMutator(mutateProb, _rng);
        _xOver = new OnePointCrossover(_mutator, xOverProb, _rng);
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
        for (int i = 0; i < numIndividuals; ++i) {
            population.add(new StandardGenotype(genotypeLength, StandardGene.ONE, _rng));
        }
        return population;
    }
}
