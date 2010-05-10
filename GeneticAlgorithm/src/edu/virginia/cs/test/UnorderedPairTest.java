/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.virginia.cs.common.UnorderedPair;

/**
 * TODO Add description
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
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEquals() {
        Assert.assertTrue(pair1.equals(pair2));
        Assert.assertTrue(pair1.equals(pair3));
        Assert.assertTrue(!pair1.equals(opair1));
        Assert.assertTrue(!pair1.equals(opair2));
    }

    @Test
    public void testHash() {
        Assert.assertEquals(pair1.hashCode(), pair2.hashCode());
        Assert.assertEquals(pair1.hashCode(), pair3.hashCode());
        Assert.assertTrue(pair1.hashCode() != opair1.hashCode());
        Assert.assertTrue(pair1.hashCode() != opair2.hashCode());
    }

}
