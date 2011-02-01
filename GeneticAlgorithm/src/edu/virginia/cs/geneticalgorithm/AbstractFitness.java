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

}
