/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.mutator;

import edu.virginia.cs.geneticalgorithm.gene.StandardGenotypeTest;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.gene.Gene;
import edu.virginia.cs.geneticalgorithm.gene.IntervalGene;
import edu.virginia.cs.geneticalgorithm.gene.StandardGenotype;

/**
 * Test harness for IntervalMutator
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class IntervalMutatorTest {

    /**
     * @return Default instance of IntervalMutator
     */
    public static IntervalMutator createIntervalMutator() {
        return new IntervalMutator(0.2, 0.02, new Random());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.mutator.IntervalMutator#setMutateRate(double)}.
     */
    @Test
    public final void testSetMutateRate() {
        final IntervalMutator instance = createIntervalMutator();
        instance.setMutateRate(0.5);
        assertEquals(0.5, instance.getMutateRate(), 0);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.mutator.IntervalMutator#getMutateRate()}.
     */
    @Test
    public final void testGetMutateRate() {
        final IntervalMutator instance = createIntervalMutator();
        assertEquals(0.2, instance.getMutateRate(), 0);
        instance.setMutateRate(0.5);
        assertEquals(0.5, instance.getMutateRate(), 0);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.mutator.IntervalMutator#setMutateSigma(double)}.
     */
    @Test
    public final void testSetMutateSigma() {
        final IntervalMutator instance = createIntervalMutator();
        assertEquals(0.02, instance.getMutateSigma(), 0);
        instance.setMutateSigma(0.5);
        assertEquals(0.5, instance.getMutateSigma(), 0);
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
     * Test method for {@link edu.virginia.cs.geneticalgorithm.mutator.IntervalMutator#mutate(edu.virginia.cs.geneticalgorithm.Genotype)}.
     */
    @Test
    public final void testMutate() {
        StandardGenotype g = StandardGenotypeTest.createStandardIntervalGenotype(5000, 0.5);
        assertEquals(2500.0, sumGenes(g), 0.0);
        final IntervalMutator instance = createIntervalMutator();
        g = (StandardGenotype) instance.mutate(g);
        assertEquals(1.0, Math.abs(2500 - sumGenes(g)), 0.999999);
        g = StandardGenotypeTest.createStandardGenotype(5000);
        final double sum1 = sumGenes(g);
        assertEquals(2500.0, sum1, 100.0);
        g = (StandardGenotype) instance.mutate(g);
        final double sum2 = sumGenes(g);
        assertEquals(2500.0, sum2, 100.0);
        assertEquals(false, sum1 == sum2);
    }

}
