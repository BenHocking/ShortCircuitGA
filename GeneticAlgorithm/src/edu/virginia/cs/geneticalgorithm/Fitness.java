/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.List;

/**
 * Interface for Fitness functions, designed with multi-objective fitness functions in mind. Single-objective fitness functions can
 * use {@link java.util.Collections#singletonList(Object)} to turn their single fitness value into a {@link java.util.List List}.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public interface Fitness {

    /**
     * @param individual {@link Genotype} to return the multi-objective fitness values for.
     * @return Multi-objective fitness values
     */
    public List<Double> fitnessValues(final Genotype individual);

    /**
     * Converts a multi-objective fitness into a single fitness value.
     * @param individual {@link Genotype} to return the multi-objective fitness values for.
     * @return Overall Fitness
     */
    public double totalFitness(final Genotype individual);
}
