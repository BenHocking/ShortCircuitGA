/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.fitness;

import static org.junit.Assert.*;
import static edu.virginia.cs.geneticalgorithm.fitness.AbstractFitness.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test harness for AbstractFitness
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Mar 15, 2011
 */
public class AbstractFitnessTest {

    private static class ConcreteFitness extends AbstractFitness {

        private final boolean _generateErrors;
        private final int NUM_FIT_VALS = 4;

        public ConcreteFitness(final boolean generateErrors) {
            _generateErrors = generateErrors;
        }

        @Override
        public List<Double> fitnessValues() {
            final List<Double> retval = new ArrayList<Double>();
            for (int i = 0; i < NUM_FIT_VALS; ++i) {
                retval.add(Double.valueOf(i));
            }
            return retval;
        }

        @Override
        public int numFitnessValues() {
            return _generateErrors ? 0 : NUM_FIT_VALS;
        }

        @Override
        public void prepare() {
            // No preparation required
        }

    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.fitness.AbstractFitness#totalFitness()}.
     */
    @Test
    public final void testTotalFitness() {
        final AbstractFitness af = new ConcreteFitness(false);
        assertEquals(6.0, af.totalFitness(), 0.0);
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.fitness.AbstractFitness#checkFitnessSize(edu.virginia.cs.geneticalgorithm.fitness.Fitness, java.util.List)}
     * .
     */
    @Test
    public final void testCheckFitnessSize() {
        AbstractFitness af = new ConcreteFitness(false);
        assertEquals(4, af.numFitnessValues());
        checkFitnessSize(af, af.fitnessValues());
        af = new ConcreteFitness(true);
        try {
            checkFitnessSize(af, af.fitnessValues());
            fail("Should not be of the correct size");
        }
        catch (final RuntimeException e) {
            assertEquals("Program error resulted in unexpected number of fitness values.\nFound: 4, expected: 0", e.getMessage());
        }
    }

}
