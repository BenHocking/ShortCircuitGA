/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.fitness;

import java.util.List;

/**
 * Interface for Fitness functions, designed with multi-objective fitness functions in mind. Single-objective fitness functions can
 * use {@link java.util.Collections#singletonList(Object)} to turn their single fitness value into a {@link java.util.List List}.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public interface Fitness {

    /**
     * @return Multi-objective fitness values
     */
    public List<Double> fitnessValues();

    /**
     * Converts a multi-objective fitness into a single fitness value.
     * @return Overall Fitness
     */
    public double totalFitness();

    /**
     * @return Expected number of fitness values
     */
    public int numFitnessValues();
}
