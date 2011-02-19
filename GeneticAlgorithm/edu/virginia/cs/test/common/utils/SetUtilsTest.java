/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.common.utils;

import static edu.virginia.cs.common.utils.SetUtils.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.virginia.cs.common.utils.OrderedPair;
import edu.virginia.cs.common.utils.SetUtils;

/**
 * Test harness for SetUtils
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 15, 2011
 */
public class SetUtilsTest {

    /**
     * Tests intersection method in class SetUtils
     */
    @Test
    public void testIntersection() {
        new SetUtils(); // For coverage
        Set<Integer> a = null;
        Set<Integer> b = null;
        assertEquals(0, intersection(a, b).size());
        a = new HashSet<Integer>();
        assertEquals(0, intersection(a, b).size());
        b = new HashSet<Integer>();
        assertEquals(0, intersection(a, b).size());
        a = null;
        assertEquals(0, intersection(a, b).size());
        b.add(1);
        assertEquals(0, intersection(a, b).size());
        a = new HashSet<Integer>(b);
        b.add(2);
        assertEquals(a, intersection(a, b));
        final Set<Integer> c = new HashSet<Integer>(a);
        a.add(3);
        final Set<Integer> aCopy = Collections.unmodifiableSet(a);
        final Set<Integer> bCopy = Collections.unmodifiableSet(b);
        assertEquals(c, intersection(a, b));
        assertEquals(aCopy, a);
        assertEquals(bCopy, b);
        assertEquals(c, intersection(aCopy, bCopy));
    }

    /**
     * Tests union method in class SetUtils
     */
    @Test
    public void testUnion() {
        Set<Integer> a = null;
        Set<Integer> b = null;
        assertEquals(0, union(a, b).size());
        a = new HashSet<Integer>();
        assertEquals(0, union(a, b).size());
        b = new HashSet<Integer>();
        assertEquals(0, union(a, b).size());
        a = null;
        assertEquals(0, union(a, b).size());
        b.add(1);
        assertEquals(b, union(a, b));
        a = new HashSet<Integer>(b);
        b.add(2);
        assertEquals(b, union(a, b));
        a.add(3);
        final Set<Integer> c = new HashSet<Integer>(a);
        c.add(2);
        final Set<Integer> aCopy = Collections.unmodifiableSet(a);
        final Set<Integer> bCopy = Collections.unmodifiableSet(b);
        assertEquals(c, union(a, b));
        assertEquals(aCopy, a);
        assertEquals(bCopy, b);
        assertEquals(c, union(aCopy, bCopy));
    }

    /**
     * Tests isSubset method in class SetUtils
     */
    @Test
    public void testIsSubset() {
        Set<Integer> a = null;
        Set<Integer> b = null;
        assertEquals(true, isSubset(a, b));
        a = new HashSet<Integer>();
        assertEquals(true, isSubset(a, b));
        b = new HashSet<Integer>();
        assertEquals(true, isSubset(a, b));
        a = null;
        assertEquals(true, isSubset(a, b));
        b.add(1);
        assertEquals(false, isSubset(a, b));
        assertEquals(true, isSubset(b, a));
        a = new HashSet<Integer>(b);
        b.add(2);
        assertEquals(false, isSubset(a, b));
        assertEquals(true, isSubset(b, a));
        a.add(3);
        final Set<Integer> aCopy = Collections.unmodifiableSet(a);
        final Set<Integer> bCopy = Collections.unmodifiableSet(b);
        assertEquals(false, isSubset(a, b));
        assertEquals(false, isSubset(b, a));
        assertEquals(aCopy, a);
        assertEquals(bCopy, b);
    }

    /**
     * Tests difference method in class SetUtils
     */
    @Test
    public void testDifference() {
        Set<Integer> a = null;
        Set<Integer> b = null;
        assertEquals(0, difference(a, b).size());
        a = new HashSet<Integer>();
        assertEquals(0, difference(a, b).size());
        b = new HashSet<Integer>();
        assertEquals(0, difference(a, b).size());
        a = null;
        assertEquals(0, difference(a, b).size());
        b.add(1);
        assertEquals(0, difference(a, b).size());
        assertEquals(b, difference(b, a));
        a = new HashSet<Integer>(b);
        b.add(2);
        final Set<Integer> c = new HashSet<Integer>();
        c.add(2);
        final Set<Integer> aCopy = Collections.unmodifiableSet(a);
        final Set<Integer> bCopy = Collections.unmodifiableSet(b);
        assertEquals(c, difference(b, a));
        assertEquals(aCopy, a);
        assertEquals(bCopy, b);
        assertEquals(c, difference(bCopy, aCopy));
    }

    /**
     * Tests product method in class SetUtils
     */
    @Test
    public void testProduct() {
        Set<Integer> a = null;
        Set<Integer> b = null;
        assertEquals(0, product(a, b).size());
        a = new HashSet<Integer>();
        assertEquals(0, product(a, b).size());
        b = new HashSet<Integer>();
        assertEquals(0, product(a, b).size());
        a = null;
        assertEquals(0, product(a, b).size());
        b.add(1);
        assertEquals(0, product(a, b).size());
        a = new HashSet<Integer>(b);
        b.add(2);
        a.add(3);
        final Set<OrderedPair<Integer, Integer>> c = new HashSet<OrderedPair<Integer, Integer>>();
        c.add(new OrderedPair<Integer, Integer>(1, 1));
        c.add(new OrderedPair<Integer, Integer>(1, 2));
        c.add(new OrderedPair<Integer, Integer>(3, 1));
        c.add(new OrderedPair<Integer, Integer>(3, 2));
        final Set<Integer> aCopy = Collections.unmodifiableSet(a);
        final Set<Integer> bCopy = Collections.unmodifiableSet(b);
        assertEquals(c, product(a, b));
        assertEquals(aCopy, a);
        assertEquals(bCopy, b);
        assertEquals(c, product(aCopy, bCopy));
    }

}
