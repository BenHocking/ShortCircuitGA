/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.common.utils;

import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
    final static UnorderedPair<Integer> pair4 = new UnorderedPair<Integer>(new Integer(3), new Integer(3));
    final static UnorderedPair<Integer> opair1 = new UnorderedPair<Integer>(new Integer(2), new Integer(14));
    final static UnorderedPair<Integer> opair2 = new UnorderedPair<Integer>(new Integer(3), new Integer(2));

    /**
     * Tests asCollection method in UnorderedPair
     */
    @Test
    public void testAsCollection() {
        final List<Integer> expected = new ArrayList<Integer>();
        expected.add(3);
        expected.add(14);
        assertEquals(expected, pair1.asCollection());
    }

    /**
     * Tests that pairs with a different (or same) order of same items are equal, but that others are not.
     */
    @Test
    public void testEquals() {
        assertTrue(pair1.equals(pair2));
        assertTrue(pair1.equals(pair3));
        assertTrue(!pair1.equals(pair4));
        assertTrue(!pair1.equals(opair1));
        assertTrue(!pair1.equals(opair2));
        assertEquals(false, pair1.equals(new Object()));
        assertEquals(false, pair1.equals(null));
    }

    /**
     * Tests that pairs that should be equal also have the same hash code.
     */
    @Test
    public void testHash() {
        assertEquals(pair1.hashCode(), pair2.hashCode());
        assertEquals(pair1.hashCode(), pair3.hashCode());
        assertTrue(pair1.hashCode() != opair1.hashCode());
        assertTrue(pair1.hashCode() != opair2.hashCode());
    }

}
