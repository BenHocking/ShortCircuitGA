/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.geneticalgorithm;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.distribution.Distribution;
import edu.virginia.cs.geneticalgorithm.distribution.DistributionMember;

/**
 * Test harness for Distribution
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class DistributionTest {

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.distribution.Distribution#Distribution(edu.virginia.cs.geneticalgorithm.distribution.Distribution)}.
     */
    @Test
    public final void testDistributionCopyConstructor() {
        final Distribution d = new Distribution();
        final DistributionMember dm = DistributionMemberTest.createDistributionMember(0.5);
        d.add(dm);
        assertEquals(dm, d.get(0));
        final Distribution dc = new Distribution(d);
        assertNotSame(d.get(0), dc.get(0));
        assertTrue(DistributionMemberTest.membersHaveSameValues(d.get(0), dc.get(0)));
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.distribution.Distribution#getLast()}.
     */
    @Test
    public final void testGetLast() {
        final Distribution d = new Distribution();
        final DistributionMember dm = DistributionMemberTest.createDistributionMember(0.5);
        d.add(dm);
        assertEquals(dm, d.getLast());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.distribution.Distribution#removeDuplicates()}.
     */
    @Test
    public final void testRemoveDuplicates() {
        final Distribution d = new Distribution();
        final DistributionMember dm = DistributionMemberTest.createDistributionMember(0.5, 0.5);
        d.add(dm);
        d.add(dm);
        d.add(new DistributionMember(dm));
        d.add(DistributionMemberTest.createDistributionMember(0.5, 0.6));
        assertEquals(4, d.size());
        d.removeDuplicates();
        assertEquals(2, d.size());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.distribution.Distribution#hasValues()}.
     */
    @Test
    public final void testHasValues() {
        final Distribution d = new Distribution();
        assertTrue(d.hasValues());
        DistributionMember dm = DistributionMemberTest.createDistributionMember(0.5);
        d.add(dm);
        assertTrue(d.hasValues());
        dm = DistributionMemberTest.createDistributionMember(null);
        d.add(dm);
        assertEquals(false, d.hasValues());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.distribution.Distribution#normalize()}.
     */
    @Test
    public final void testNormalize() {
        final Distribution d = new Distribution();
        d.add(DistributionMemberTest.createDistributionMember(null));
        d.normalize();
        d.clear();
        d.add(DistributionMemberTest.createDistributionMember(0.0));
        d.normalize();
        d.clear();
        final DistributionMember dm = DistributionMemberTest.createDistributionMember(0.5);
        d.add(dm);
        d.add(dm);
        d.add(dm);
        d.add(dm);
        d.normalize();
        d.normalize();
        assertEquals(0.25, d.getLast().getValue());
    }

}
