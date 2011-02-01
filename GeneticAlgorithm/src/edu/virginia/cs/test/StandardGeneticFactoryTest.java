/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.AbstractFitness;
import edu.virginia.cs.geneticalgorithm.Fitness;
import edu.virginia.cs.geneticalgorithm.FitnessFactory;
import edu.virginia.cs.geneticalgorithm.Gene;
import edu.virginia.cs.geneticalgorithm.GeneticFactory;
import edu.virginia.cs.geneticalgorithm.Genotype;
import edu.virginia.cs.geneticalgorithm.Reproduction;
import edu.virginia.cs.geneticalgorithm.StandardGene;
import edu.virginia.cs.geneticalgorithm.StandardGeneticFactory;

/**
 * Test harness for the {@link StandardGeneticFactory}
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 26, 2010
 */
public final class StandardGeneticFactoryTest {

    private final FitnessFactory _fitFn = new TrivialStandardFitnessFactory();
    private static int POP_SIZE = 20;
    private static int GENOTYPE_SIZE = 5;
    private static int NUM_GENERATIONS = 20;

    private class TrivialStandardFitnessFactory implements FitnessFactory {

        private class TrivialStandardFitness extends AbstractFitness {

            private final Genotype _genotype;

            TrivialStandardFitness(final Genotype genotype) {
                _genotype = genotype;
            }

            @Override
            public List<Double> fitnessValues() {
                double retval = 0;
                for (final Gene g : _genotype) {
                    if (g == StandardGene.ONE) retval += 1.0;
                }
                return Collections.singletonList(retval);
            }

        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.FitnessFactory#createFitness(edu.virginia.cs.geneticalgorithm.Genotype)
         */
        @Override
        public Fitness createFitness(final Genotype individual) {
            return new TrivialStandardFitness(individual);
        }
    }

    /**
     * Tests all of the components of the GeneticFactory in an integrative manner.
     */
    @Test
    public void mainTest() {
        final double tolerance = 1E-8;
        final long seed = 1;
        final GeneticFactory factory = new StandardGeneticFactory(seed);
        List<Genotype> population = factory.createPopulation(POP_SIZE, GENOTYPE_SIZE);
        boolean allowDuplicates = true;
        final boolean keepHistory = false;
        Reproduction reproduction = new Reproduction(allowDuplicates, keepHistory);
        for (int i = 0; i < NUM_GENERATIONS; ++i) {
            population = reproduction.reproduce(population, _fitFn, factory.getSelectFunction(), factory.getCrossoverFunction());
        }
        Assert.assertEquals(GENOTYPE_SIZE, reproduction.getBestFit().get(0), tolerance);
        Assert.assertEquals(4.45, reproduction.getMeanFit(), tolerance);
        allowDuplicates = false;
        reproduction = new Reproduction(allowDuplicates, keepHistory);
        for (int i = 0; i < NUM_GENERATIONS; ++i) {
            population = reproduction.reproduce(population, _fitFn, factory.getSelectFunction(), factory.getCrossoverFunction());
        }
        Assert.assertEquals(GENOTYPE_SIZE, reproduction.getBestFit().get(0), tolerance);
        Assert.assertEquals(2.9, reproduction.getMeanFit(), tolerance);
    }
}
