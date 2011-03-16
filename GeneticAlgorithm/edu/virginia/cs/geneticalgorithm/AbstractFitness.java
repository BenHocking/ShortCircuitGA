/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
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
     * Total fitness is just the sum of the fitness components
     * @see edu.virginia.cs.geneticalgorithm.Fitness#totalFitness()
     */
    @Override
    public double totalFitness() {
        double retval = 0.0;
        final List<Double> fitList = fitnessValues();
        for (final Double d : fitList) {
            retval += d;
        }
        return retval;
    }

    /**
     * @param f Fitness function providing the expected number of fitness values
     * @param fitness Actual number of fitness values
     */
    public static void checkFitnessSize(final Fitness f, final List<Double> fitness) {
        if (f.numFitnessValues() != fitness.size()) {
            final String errMsg = "Program error resulted in unexpected number of fitness values.\nFound: " + fitness.size()
                                  + ", expected: " + f.numFitnessValues();
            throw new RuntimeException(errMsg);
        }
    }

}
