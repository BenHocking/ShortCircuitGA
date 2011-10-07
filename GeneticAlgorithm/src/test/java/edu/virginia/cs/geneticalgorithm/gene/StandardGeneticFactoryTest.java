/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.gene;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.distribution.Distribution;
import edu.virginia.cs.geneticalgorithm.fitness.AbstractFitness;
import edu.virginia.cs.geneticalgorithm.fitness.Fitness;
import edu.virginia.cs.geneticalgorithm.fitness.FitnessFactory;
import edu.virginia.cs.geneticalgorithm.mutator.DecayingIntervalMutator;
import edu.virginia.cs.geneticalgorithm.reproduction.Reproduction;

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

    /**
     * Trivial fitness function designed for easy testing
     * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
     * @since May 20, 2010
     */
    public static class TrivialStandardFitness extends AbstractFitness {

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

        @Override
        public int numFitnessValues() {
            return 1;
        }
    }

    /**
     * Trivial fitness function factory designed for easy testing
     * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
     * @since May 20, 2010
     */
    public static class TrivialStandardFitnessFactory implements FitnessFactory {

        /**
         * @see edu.virginia.cs.geneticalgorithm.fitness.FitnessFactory#createFitness(edu.virginia.cs.geneticalgorithm.gene.Genotype)
         */
        @Override
        public Fitness createFitness(final Genotype individual) {
            return new TrivialStandardFitness(individual);
        }
    }

    /**
     * Tests setMutator method in StandardGeneticFactory
     */
    @Test
    public void testSetMutator() {
        final GeneticFactory factory = new StandardGeneticFactory(1);
        final Random rng = new Random();
        final DecayingIntervalMutator mutator = new DecayingIntervalMutator(0.05, 0.001, 0.025, 0.005, rng);
        factory.setMutator(mutator);
        assertEquals(mutator, factory.getCrossoverFunction().getMutator());
    }

    /**
     * Tests all of the components of the GeneticFactory in an integrative manner.
     */
    @Test
    public void mainTest() {
        Reproduction.DEBUG_LEVEL = 0;
        Reproduction.SetNumProcesses(1);
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
        List<Distribution> history = reproduction.getHistory();
        assertEquals(0, history.size());
        Assert.assertEquals(GENOTYPE_SIZE, reproduction.getBestFit().get(0), tolerance);
        Assert.assertEquals(4.2, reproduction.getMeanFit(), tolerance);
        allowDuplicates = false;
        reproduction = new Reproduction(allowDuplicates, true);
        reproduction.setNumElites((int) (POP_SIZE * 0.1));
        assertEquals(2, reproduction.getNumElites());
        for (int i = 0; i < 4; ++i) {
            population = reproduction.reproduce(population, _fitFn, factory.getSelectFunction(), factory.getCrossoverFunction());
            Reproduction.DEBUG_LEVEL = i;
        }
        Assert.assertEquals(GENOTYPE_SIZE, reproduction.getBestFit().get(0), tolerance);
        Assert.assertEquals(2.7, reproduction.getMeanFit(), tolerance);
        history = reproduction.getHistory();
        assertEquals(4, history.size());
    }
}
