/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.geneticalgorithm;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.StandardMutator;

/**
 * TODO Add description
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class StandardMutatorTest {

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.StandardMutator#setMutateRate(double)} and
     * {@link edu.virginia.cs.geneticalgorithm.StandardMutator#getMutateRate()}.
     */
    @Test
    public final void testSetAndGetMutateRate() {
        final Random rng = new Random();
        final double mutateRate = 0.05;
        final StandardMutator instance = new StandardMutator(mutateRate, rng);
        assertEquals(mutateRate, instance.getMutateRate(), 0.0);
        instance.setMutateRate(0.01);
        assertEquals(0.01, instance.getMutateRate(), 0.0);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.StandardMutator#mutate(edu.virginia.cs.geneticalgorithm.Genotype)}.
     */
    @Test
    public final void testMutate() {
        // TODO add specific tests (currently covered elsewhere)
    }

}
