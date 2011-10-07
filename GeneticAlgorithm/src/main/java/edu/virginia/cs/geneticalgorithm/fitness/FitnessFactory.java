/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.fitness;

import edu.virginia.cs.geneticalgorithm.gene.Genotype;

/**
 * Generates instances of Fitness functions
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Nov 20, 2010
 */
public interface FitnessFactory {

    /**
     * Creates fitness function for the specified individual
     * @param individual {@link Genotype} describing the individual to evaluate
     * @return {@link Fitness} function to evaluate the individual with
     */
    public Fitness createFitness(Genotype individual);
}
