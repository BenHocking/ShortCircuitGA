/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.geneticalgorithm;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.DecayingIntervalMutator;
import edu.virginia.cs.geneticalgorithm.Gene;
import edu.virginia.cs.geneticalgorithm.IntervalGene;
import edu.virginia.cs.geneticalgorithm.StandardGenotype;

/**
 * Test harness for DecayingIntervalMutator
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 19, 2011
 */
public class DecayingIntervalMutatorTest {

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.DecayingIntervalMutator#setMutateRate(double)} and
     * {@link edu.virginia.cs.geneticalgorithm.DecayingIntervalMutator#getMutateRate()}.
     */
    @Test
    public final void testSetAndGetMutateRate() {
        final Random rng = new Random();
        final double mutateRate = 0.05;
        final DecayingIntervalMutator instance = new DecayingIntervalMutator(mutateRate, 0.001, 0.01, 0.005, rng);
        assertEquals(mutateRate, instance.getMutateRate(), 0.0);
        instance.setMutateRate(0.01);
        assertEquals(0.01, instance.getMutateRate(), 0.0);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.DecayingIntervalMutator#setMutateSigma(double)} and
     * {@link edu.virginia.cs.geneticalgorithm.DecayingIntervalMutator#mutate(edu.virginia.cs.geneticalgorithm.Genotype)}.
     */
    @Test
    public final void testSetAndGetMutateSigma() {
        final Random rng = new Random();
        final double mutateSigma = 0.025;
        final DecayingIntervalMutator instance = new DecayingIntervalMutator(0.05, 0.001, mutateSigma, 0.005, rng);
        assertEquals(mutateSigma, instance.getMutateSigma(), 0.0);
        instance.setMutateSigma(0.01);
        assertEquals(0.01, instance.getMutateSigma(), 0.0);
    }

    private double sumGenes(final StandardGenotype sg) {
        double retval = 0;
        for (final Gene g : sg) {
            if (g instanceof IntervalGene) {
                retval += ((IntervalGene) g).getValue();
            }
            else {
                retval += Integer.valueOf(g.toString());
            }
        }
        return retval;
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.DecayingIntervalMutator#setMutateSigma(double)} and
     * {@link edu.virginia.cs.geneticalgorithm.DecayingIntervalMutator#mutate(edu.virginia.cs.geneticalgorithm.Genotype)}.
     */
    @Test
    public final void testMutate() {
        final Random rng = new Random();
        final DecayingIntervalMutator instance = new DecayingIntervalMutator(0.05, 0.001, 0.025, 0.005, rng);
        // They all start out with 0.5 values
        StandardGenotype g = StandardGenotypeTest.createStandardIntervalGenotype(5000, 0.5);
        assertEquals(2500.0, sumGenes(g), 0.0);
        g = (StandardGenotype) instance.mutate(g);
        assertEquals(1.0, Math.abs(2500 - sumGenes(g)), 0.999999);
        assertEquals(0.05 * 0.999, instance.getMutateRate(), 1e-9);
        assertEquals(0.025 * 0.995, instance.getMutateSigma(), 1e-9);
        g = StandardGenotypeTest.createStandardGenotype(5000);
        final double sum1 = sumGenes(g);
        assertEquals(2500.0, sum1, 100.0);
        g = (StandardGenotype) instance.mutate(g);
        final double sum2 = sumGenes(g);
        assertEquals(2500.0, sum2, 100.0);
        assertEquals(false, sum1 == sum2);
    }
}
