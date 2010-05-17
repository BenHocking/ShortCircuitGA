/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.List;

/**
 * Abstract version of Fitness with default version of totalFitness
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 16, 2010
 */
public abstract class AbstractFitness implements Fitness {

    /**
     * @see edu.virginia.cs.geneticalgorithm.Fitness#totalFitness(edu.virginia.cs.geneticalgorithm.Genotype)
     */
    @Override
    public double totalFitness(final Genotype individual) {
        final List<Double> fitList = fitnessValues(individual);
        double retval = 0.0;
        for (final Double d : fitList) {
            retval += d;
        }
        return retval;
    }

}
