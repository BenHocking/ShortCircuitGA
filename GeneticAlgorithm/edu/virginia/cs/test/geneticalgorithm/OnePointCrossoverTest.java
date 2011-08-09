/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.geneticalgorithm;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.crossover.OnePointCrossover;
import edu.virginia.cs.geneticalgorithm.mutator.IntervalMutator;
import edu.virginia.cs.geneticalgorithm.mutator.Mutator;

/**
 * Test harness for OnePointCrossover routines not tested in StandardGeneticFactoryTest. TODO Add better test here for crossover
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class OnePointCrossoverTest {

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.crossover.OnePointCrossover#setMutator(edu.virginia.cs.geneticalgorithm.mutator.Mutator)} and
     * {@link edu.virginia.cs.geneticalgorithm.crossover.OnePointCrossover#getMutator()}.
     */
    @Test
    public final void testSetAndGetMutator() {
        final Mutator m = IntervalMutatorTest.createIntervalMutator();
        final OnePointCrossover xover = new OnePointCrossover(m, 0.5, new Random());
        assertEquals(m, xover.getMutator());
        final Mutator m2 = new IntervalMutator(0.3, 0.02, new Random());
        xover.setMutator(m2);
        assertEquals(m2, xover.getMutator());
        assertNotSame(m, xover.getMutator());
    }
}
