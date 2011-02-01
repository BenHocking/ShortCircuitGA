/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test;

import junit.framework.Assert;

import org.junit.Test;

import edu.virginia.cs.common.utils.UnorderedPair;

/**
 * Test harness of the {@link UnorderedPair} class.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public final class UnorderedPairTest {

    final static UnorderedPair<Integer> pair1 = new UnorderedPair<Integer>(new Integer(3), new Integer(14));
    final static UnorderedPair<Integer> pair2 = new UnorderedPair<Integer>(new Integer(14), new Integer(3));
    final static UnorderedPair<Integer> pair3 = new UnorderedPair<Integer>(new Integer(3), new Integer(14));
    final static UnorderedPair<Integer> opair1 = new UnorderedPair<Integer>(new Integer(2), new Integer(14));
    final static UnorderedPair<Integer> opair2 = new UnorderedPair<Integer>(new Integer(3), new Integer(2));

    /**
     * Tests that pairs with a different (or same) order of same items are equal, but that others are not.
     */
    @Test
    public void testEquals() {
        Assert.assertTrue(pair1.equals(pair2));
        Assert.assertTrue(pair1.equals(pair3));
        Assert.assertTrue(!pair1.equals(opair1));
        Assert.assertTrue(!pair1.equals(opair2));
    }

    /**
     * Tests that pairs that should be equal also have the same hash code.
     */
    @Test
    public void testHash() {
        Assert.assertEquals(pair1.hashCode(), pair2.hashCode());
        Assert.assertEquals(pair1.hashCode(), pair3.hashCode());
        Assert.assertTrue(pair1.hashCode() != opair1.hashCode());
        Assert.assertTrue(pair1.hashCode() != opair2.hashCode());
    }

}
