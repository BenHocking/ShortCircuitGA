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
import edu.virginia.cs.geneticalgorithm.IntervalGene;
import edu.virginia.cs.geneticalgorithm.IntervalGeneticFactory;
import edu.virginia.cs.geneticalgorithm.Reproduction;

/**
 * Test of the {@link IntervalGeneticFactory} class.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 26, 2010
 */
public final class IntervalGeneticFactoryTest {

    private final FitnessFactory _fitFnFactory = new TrivialIntervalFitnessFactory();
    private static int POP_SIZE = 20;
    private static int GENOTYPE_SIZE = 5;
    private static int NUM_GENERATIONS = 20;

    private class TrivialIntervalFitnessFactory implements FitnessFactory {

        private class TrivialIntervalFitness extends AbstractFitness {

            Genotype _genotype;

            TrivialIntervalFitness(final Genotype genotype) {
                _genotype = genotype;
            }

            @Override
            public List<Double> fitnessValues() {
                double retval = 0;
                for (final Gene g : _genotype) {
                    retval += ((IntervalGene) g).getValue();
                }
                return Collections.singletonList(retval);
            }

        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.FitnessFactory#createFitness(edu.virginia.cs.geneticalgorithm.Genotype)
         */
        @Override
        public Fitness createFitness(final Genotype individual) {
            return new TrivialIntervalFitness(individual);
        }

    }

    /**
     * Tests all of the components of the GeneticFactory in an integrative manner.
     */
    @Test
    public void mainTest() {
        final double tolerance = 1E-8;
        final long seed = 1;
        final GeneticFactory factory = new IntervalGeneticFactory(seed);
        List<Genotype> population = factory.createPopulation(POP_SIZE, GENOTYPE_SIZE);
        boolean allowDuplicates = true;
        final boolean keepHistory = false;
        Reproduction reproduction = new Reproduction(allowDuplicates, keepHistory);
        for (int i = 0; i < NUM_GENERATIONS; ++i) {
            population = reproduction.reproduce(population, _fitFnFactory, factory.getSelectFunction(),
                                                factory.getCrossoverFunction());
        }
        Assert.assertEquals(3.793964434630042, reproduction.getBestFits().get(0).get(0), tolerance);
        // As POP_SIZE -> inf, you would expect reproduction.getMeanFits().get(0) -> GENOTYPE_SIZE / 2 (i.e., 2.5)
        Assert.assertEquals(2.4418757761918597, reproduction.getMeanFits().get(0), tolerance);
        Assert.assertEquals(4.15, reproduction.getBestFit().get(0), 0.1);
        Assert.assertEquals(3.75, reproduction.getMeanFit(), 0.1);
        allowDuplicates = false;
        reproduction = new Reproduction(allowDuplicates, keepHistory);
        for (int i = 0; i < NUM_GENERATIONS; ++i) {
            population = reproduction.reproduce(population, _fitFnFactory, factory.getSelectFunction(),
                                                factory.getCrossoverFunction());
        }
        Assert.assertEquals(4.5, reproduction.getBestFit().get(0), 0.15);
        Assert.assertEquals(4.05, reproduction.getMeanFit(), 0.1);
    }
}
