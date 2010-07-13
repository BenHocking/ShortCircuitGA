/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.Random;

import edu.virginia.cs.common.UnorderedPair;

/**
 * Crossover function for when position in the {@link Genotype} is irrelevant. Each {@link Gene} in the Genotype will be crossed
 * independently of the others.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 5, 2010
 */
public final class UniformCrossover implements Crossover {

    private final Mutator _mutator;
    private final double _xOverProb;
    private final double _geneXOverProb;
    private final Random _rng;

    /**
     * @param m {@link Mutator} functor class to run after crossover
     * @param xOverProb Probability (0 to 1) that a cross-over will occur
     * @param geneXOverProb Probability (0 to 1) for each individual {@link Gene} that it will be in the other child
     * {@link Genotype}
     * @param rng Random number generator used to determine whether a cross-over occurs
     */
    public UniformCrossover(final Mutator m, final double xOverProb, final double geneXOverProb, final Random rng) {
        _mutator = m;
        _xOverProb = xOverProb;
        _geneXOverProb = geneXOverProb;
        _rng = rng;
    }

    @Override
    public UnorderedPair<Genotype> crossover(final Genotype mother, final Genotype father) {
        final Genotype kid1 = mother.clone();
        final Genotype kid2 = father.clone();
        final int site = _rng.nextInt(mother.getNumGenes() - 1) + 1;
        if (_rng.nextDouble() < _xOverProb) {
            for (int i = site; i < mother.getNumGenes(); ++i) {
                if (_rng.nextDouble() < _geneXOverProb) {
                    kid1.setGene(i, father.getGene(i));
                    kid2.setGene(i, mother.getGene(i));
                }
            }
        }
        return new UnorderedPair<Genotype>(_mutator.mutate(kid1), _mutator.mutate(kid2));
    }

}