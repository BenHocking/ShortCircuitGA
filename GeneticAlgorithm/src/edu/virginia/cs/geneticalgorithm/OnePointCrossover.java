/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.Random;

import edu.virginia.cs.common.UnorderedPair;

/**
 * Crossover function for when position in the {@link Genotype} is significant. The first part of one Genotype will be matched with
 * the last part of another, and vice-versa.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 5, 2010
 */
public final class OnePointCrossover implements Crossover {

    private final Random _rng;
    private final Mutator _mutator;
    private final double _xOverProb;

    /**
     * @param m {@link Mutator} functor class to run after crossover
     * @param xOverProb Probability (0 to 1) that a cross-over will occur
     * @param rng Random number generator used to determine whether a cross-over occurs
     */
    public OnePointCrossover(final Mutator m, final double xOverProb, final Random rng) {
        _rng = rng;
        _mutator = m;
        _xOverProb = xOverProb;
    }

    @Override
    public UnorderedPair<Genotype> crossover(final Genotype mother, final Genotype father) {
        final Genotype kid1 = mother.clone();
        final Genotype kid2 = father.clone();
        if (_rng.nextDouble() < _xOverProb) {
            final int site = _rng.nextInt(mother.getNumGenes() - 1) + 1;
            for (int i = site; i < mother.getNumGenes(); ++i) {
                kid1.setGene(i, father.getGene(i));
                kid2.setGene(i, mother.getGene(i));
            }
        }
        return new UnorderedPair<Genotype>(_mutator.mutate(kid1), _mutator.mutate(kid2));
    }

}