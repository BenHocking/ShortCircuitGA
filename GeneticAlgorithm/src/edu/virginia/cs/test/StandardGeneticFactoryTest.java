/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.virginia.cs.common.SingleItemList;
import edu.virginia.cs.geneticalgorithm.Fitness;
import edu.virginia.cs.geneticalgorithm.Gene;
import edu.virginia.cs.geneticalgorithm.GeneticFactory;
import edu.virginia.cs.geneticalgorithm.Genotype;
import edu.virginia.cs.geneticalgorithm.Reproduction;
import edu.virginia.cs.geneticalgorithm.StandardGene;
import edu.virginia.cs.geneticalgorithm.StandardGeneticFactory;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 26, 2010
 */
public final class StandardGeneticFactoryTest {

    private final Fitness _fitFn = new TrivialStandardFitness();
    private static int POP_SIZE = 20;
    private static int GENOTYPE_SIZE = 5;
    private static int NUM_GENERATIONS = 20;

    private class TrivialStandardFitness implements Fitness {

        /**
         * @see edu.virginia.cs.geneticalgorithm.Fitness#fitness(edu.virginia.cs.geneticalgorithm.Genotype)
         */
        @Override
        public List<Double> fitness(final Genotype individual) {
            double retval = 0;
            for (final Gene g : individual) {
                if (g == StandardGene.ONE) retval += 1.0;
            }
            return new SingleItemList<Double>(retval);
        }

    }

    @Test
    public void mainTest() {
        final double tolerance = 1E-8;
        final long seed = 1;
        final GeneticFactory factory = new StandardGeneticFactory(seed);
        List<Genotype> population = factory.createPopulation(POP_SIZE, GENOTYPE_SIZE);
        boolean allowDuplicates = true;
        Reproduction reproduction = new Reproduction(allowDuplicates);
        for (int i = 0; i < NUM_GENERATIONS; ++i) {
            population = reproduction.reproduce(population, _fitFn, factory.getSelectFunction(), factory.getCrossoverFunction());
        }
        Assert.assertEquals(GENOTYPE_SIZE, reproduction.getBestFit(), tolerance);
        Assert.assertEquals(4.45, reproduction.getMeanFit(), tolerance);
        allowDuplicates = false;
        reproduction = new Reproduction(allowDuplicates);
        for (int i = 0; i < NUM_GENERATIONS; ++i) {
            population = reproduction.reproduce(population, _fitFn, factory.getSelectFunction(), factory.getCrossoverFunction());
        }
        Assert.assertEquals(GENOTYPE_SIZE, reproduction.getBestFit(), tolerance);
        Assert.assertEquals(3.05, reproduction.getMeanFit(), tolerance);
    }
}
