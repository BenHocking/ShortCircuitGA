/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.Random;

import edu.virginia.cs.common.UnorderedPair;

final class StandardCrossover implements Crossover {

    private final Random _rng;
    private final Mutator _mutator;
    private final double _xOverProb;

    public StandardCrossover(final Mutator m, final double xOverProb, final Random rng) {
        _rng = rng;
        _mutator = m;
        _xOverProb = xOverProb;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Crossover#crossover(edu.virginia.cs.geneticalgorithm.Genotype,
     * edu.virginia.cs.geneticalgorithm.Genotype)
     */
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