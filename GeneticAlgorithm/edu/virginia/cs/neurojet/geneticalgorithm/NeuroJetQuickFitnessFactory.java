/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import edu.virginia.cs.geneticalgorithm.fitness.FitnessFactory;
import edu.virginia.cs.geneticalgorithm.fitness.ProxyFitness;
import edu.virginia.cs.geneticalgorithm.fitness.ProxyFitnessFactory;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Nov 20, 2010
 */
public class NeuroJetQuickFitnessFactory implements ProxyFitnessFactory {

    private final boolean _generatesPostFitness;
    private final FitnessFactory _postFitnessFactory;

    /**
     * Default constructor
     */
    public NeuroJetQuickFitnessFactory() {
        this(null);
    }

    /**
     * @param postFactory Fitness factory to use for generating post fitness function (or null if not generating one from here)
     */
    public NeuroJetQuickFitnessFactory(final FitnessFactory postFactory) {
        _generatesPostFitness = (postFactory != null);
        _postFitnessFactory = postFactory;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.FitnessFactory#createFitness(edu.virginia.cs.geneticalgorithm.gene.Genotype)
     */
    @Override
    public ProxyFitness createFitness(final Genotype individual) {
        return new NeuroJetQuickFitness(_postFitnessFactory.createFitness(individual));
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.ProxyFitnessFactory#generatesPostFitness()
     */
    @Override
    public boolean generatesPostFitness() {
        return _generatesPostFitness;
    }

}
