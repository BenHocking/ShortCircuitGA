/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.fitness;

import java.util.List;

/**
 * Interface for generating fitness values. Useful as a Strategy by Fitness classes.
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Mar 19, 2011
 */
public interface FitnessGenerator {

    /**
     * @return List of fitness values this generator generates
     */
    public List<Double> fitnessValues();

    /**
     * @return This component's contribution to overall fitness
     */
    public double overallFitness();

    /**
     * How many fitness values should be returned by the fitnessValues() method. Included to provide a way for verification.
     * @return how many fitness values should be returned by the fitnessValues() method
     */
    public int numFitnessValues();
}
