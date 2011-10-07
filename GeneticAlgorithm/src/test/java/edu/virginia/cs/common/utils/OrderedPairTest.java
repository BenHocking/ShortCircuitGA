/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test harness for OrderedPair
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 18, 2011
 */
public class OrderedPairTest {

    /**
     * Test method for {@link edu.virginia.cs.common.utils.OrderedPair#hashCode()}.
     */
    @Test
    public final void testHashCode() {
        final OrderedPair<Integer, Double> id = new OrderedPair<Integer, Double>(0, 0.0);
        assertEquals(31487, id.hashCode());
    }

    /**
     * Test method for {@link edu.virginia.cs.common.utils.OrderedPair#asCollection(java.lang.Class)}.
     */
    @Test
    public final void testAsCollection() {
        final OrderedPair<Integer, Double> id = new OrderedPair<Integer, Double>(1, 1.2);
        final List<Number> list1 = new ArrayList<Number>();
        list1.add(new Integer(1));
        list1.add(new Double(1.2));
        try {
            id.asCollection(Number.class);
            fail("Can only call collection on identically classed objects in pair");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Attempted to crease list of incompatible type 'Number'", e.getMessage());
        }
        final OrderedPair<Integer, Number> id2 = new OrderedPair<Integer, Number>(Integer.valueOf(1), Integer.valueOf(2));
        final List<Number> list2 = new ArrayList<Number>();
        list2.add(new Integer(1));
        list2.add(new Integer(2));
        assertEquals(list2, id2.asCollection(Integer.class));
        final OrderedPair<Integer, Double> idn1 = new OrderedPair<Integer, Double>(null, 1.2);
        try {
            idn1.asCollection(Number.class);
            fail("Can only call collection on identically classed objects in pair");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Attempted to crease list of incompatible type 'Number'", e.getMessage());
        }
        final OrderedPair<Integer, Double> idn2 = new OrderedPair<Integer, Double>(1, null);
        try {
            idn2.asCollection(Integer.class);
            fail("Can only call collection on identically classed objects in pair");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Attempted to crease list of incompatible type 'Integer'", e.getMessage());
        }
    }

    /**
     * Test method for {@link edu.virginia.cs.common.utils.OrderedPair#equals(java.lang.Object)}.
     */
    @Test
    public final void testEqualsObject() {
        final OrderedPair<Integer, Double> id = new OrderedPair<Integer, Double>(1, 1.2);
        final OrderedPair<Integer, Number> id2 = new OrderedPair<Integer, Number>(Integer.valueOf(1), Integer.valueOf(2));
        final OrderedPair<Integer, Integer> id3 = new OrderedPair<Integer, Integer>(Integer.valueOf(1), Integer.valueOf(2));
        final OrderedPair<Integer, Double> id4 = new OrderedPair<Integer, Double>(2, 1.2);
        assertEquals(false, id.equals(id2));
        assertEquals(false, id2.equals(id));
        assertEquals(false, id.equals(id4));
        assertEquals(id2, id3);
        assertEquals(id3, id2);
        assertEquals(false, id2.equals(null));
    }

    /**
     * Test method for {@link edu.virginia.cs.common.utils.Pair#getFirst()}.
     */
    @Test
    public final void testGetFirst() {
        final OrderedPair<Integer, Double> id = new OrderedPair<Integer, Double>(1, 1.2);
        assertEquals(Integer.valueOf(1), id.getFirst());
    }

    /**
     * Test method for {@link edu.virginia.cs.common.utils.Pair#getLast()}.
     */
    @Test
    public final void testGetLast() {
        final OrderedPair<Integer, Double> id = new OrderedPair<Integer, Double>(1, 1.2);
        assertEquals(Double.valueOf(1.2), id.getLast());
    }

    /**
     * Test method for {@link edu.virginia.cs.common.utils.Pair#toString()}.
     */
    @Test
    public final void testToString() {
        final OrderedPair<Integer, Double> id = new OrderedPair<Integer, Double>(1, 1.2);
        assertEquals("(1, 1.2)", id.toString());
        final OrderedPair<Integer, Double> id2 = new OrderedPair<Integer, Double>(null, null);
        assertEquals("(null, null)", id2.toString());
    }

}
