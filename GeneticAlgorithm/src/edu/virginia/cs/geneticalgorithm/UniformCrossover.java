/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.Random;

import edu.virginia.cs.common.UnorderedPair;

final class UniformCrossover implements Crossover {

    private final Mutator _mutator;
    private final double _xOverProb;
    private final double _geneXOverProb;
    private final Random _rng;

    public UniformCrossover(final Mutator m, final double xOverProb, final double geneXOverProb, final Random rng) {
        _mutator = m;
        _xOverProb = xOverProb;
        _geneXOverProb = geneXOverProb;
        _rng = rng;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Crossover#crossover(edu.virginia.cs.geneticalgorithm.Genotype,
     * edu.virginia.cs.gCopyOfOnePointCrossovereneticalgorithm.Genotype)
     */
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