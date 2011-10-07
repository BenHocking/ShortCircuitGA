/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.select;

import edu.virginia.cs.geneticalgorithm.distribution.DistributionMemberTest;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.distribution.Distribution;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;

/**
 * Test harness for StandardSelect
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class StandardSelectTest {

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.select.StandardSelect#select(edu.virginia.cs.geneticalgorithm.distribution.Distribution)}
     * .
     */
    @Test
    public final void testSelect() {
        final Distribution d = new Distribution() {

            @Override
            public void normalize() { // Don't normalize this one
            }
        };
        d.add(DistributionMemberTest.createDistributionMember(1.0, 0.1));
        d.add(DistributionMemberTest.createDistributionMember(1.0, 0.2));
        final StandardSelect s = new StandardSelect(new Random());
        Genotype g = s.select(d);
        assertEquals(d.get(0).getGenotype(), g);
        assertNotSame(d.getLast().getGenotype(), g);
        d.clear();
        d.add(DistributionMemberTest.createDistributionMember(0.0, 0.1));
        d.add(DistributionMemberTest.createDistributionMember(0.0, 0.2));
        g = s.select(d);
        assertNotSame(d.get(0).getGenotype(), g);
        assertEquals(d.getLast().getGenotype(), g);
    }
}
