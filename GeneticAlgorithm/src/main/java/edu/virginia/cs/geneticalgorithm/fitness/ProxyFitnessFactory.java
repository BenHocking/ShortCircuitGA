/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.fitness;

import edu.virginia.cs.geneticalgorithm.gene.Genotype;

/**
 * Fitness factory for generating fitness functions designed to act as a proxy
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Nov 21, 2010
 */
public interface ProxyFitnessFactory extends FitnessFactory {

    /**
     * Creates fitness function for the specified individual
     * @param individual {@link Genotype} describing the individual to evaluate
     * @return {@link Fitness} function to evaluate the individual with
     */
    public ProxyFitness createFitness(Genotype individual);

    /**
     * @return Whether this fitness factory is responsible for creating its own copy of the post fitness function
     */
    public boolean generatesPostFitness();
}
