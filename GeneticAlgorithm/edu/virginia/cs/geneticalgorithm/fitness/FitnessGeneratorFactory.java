/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.fitness;

/**
 * Factory for generating fitness generators
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Mar 19, 2011
 */
public interface FitnessGeneratorFactory {

    /**
     * @return FitnessGenerator
     */
    public FitnessGenerator createFitnessGenerator();
}
