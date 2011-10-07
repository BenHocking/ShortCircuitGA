/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test harness for {@link IntegerRange} class.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 16, 2010
 */
public class IntegerRangeTest {

    /**
     * Tests IntegerRange constructor with single parameter (upper bound if greater than zero, or lower bound if less than zero).
     * Other bound should be 0, with step size 1 if single parameter is upper bound and step size -1 if single parameter is lower
     * bound. If parameter is zero, then the range is empty.
     */
    @Test
    public void testSingleValue() {
        final List<Integer> values = new ArrayList<Integer>();
        for (final Integer i : new IntegerRange(3)) {
            values.add(i);
        }
        assertEquals("Range size", 3, values.size());
        for (int i = 0; i < values.size(); ++i) {
            assertEquals("values[" + i + "]", Integer.valueOf(i), values.get(i));
        }
        values.clear();
        for (final Integer i : new IntegerRange(-3)) {
            values.add(i);
        }
        assertEquals("Range size", 3, values.size());
        for (int i = 0; i < values.size(); ++i) {
            assertEquals("values[" + i + "]", Integer.valueOf(-i), values.get(i));
        }
        values.clear();
        for (final Integer i : new IntegerRange(0)) {
            values.add(i);
        }
        assertEquals("Range size", 0, values.size());
    }

    /**
     * Tests IntegerRange constructor with two bounds (lower and upper bound). Step size should be 1 if the lower bound comes first,
     * and -1 if the lower bound (smaller value) comes second. If the two values are equal, then the range should be empty.
     */
    @Test
    public void testDoubleValue() {
        final List<Integer> values = new ArrayList<Integer>();
        for (final Integer i : new IntegerRange(0, 3)) {
            values.add(i);
        }
        assertEquals("Range size", 3, values.size());
        for (int i = 0; i < values.size(); ++i) {
            assertEquals("values[" + i + "]", Integer.valueOf(i), values.get(i));
        }
        values.clear();
        for (final Integer i : new IntegerRange(1, 4)) {
            values.add(i);
        }
        assertEquals("Range size", 3, values.size());
        for (int i = 0; i < values.size(); ++i) {
            assertEquals("values[" + i + "]", Integer.valueOf(i + 1), values.get(i));
        }
        values.clear();
        for (final Integer i : new IntegerRange(3, 0)) {
            values.add(i);
        }
        assertEquals("Range size", 3, values.size());
        for (int i = 0; i < values.size(); ++i) {
            assertEquals("values[" + i + "]", Integer.valueOf(3 - i), values.get(i));
        }
        values.clear();
        for (final Integer i : new IntegerRange(1, 1)) {
            values.add(i);
        }
        assertEquals("Range size", 0, values.size());
    }

    /**
     * Tests IntegerRange with all lower bound, step size, and upper bound.
     */
    @Test
    public void testTrebleValue() {
        final List<Integer> values = new ArrayList<Integer>();
        for (final Integer i : new IntegerRange(0, 1, 3)) {
            values.add(i);
        }
        assertEquals("Range size", 3, values.size());
        for (int i = 0; i < values.size(); ++i) {
            assertEquals("values[" + i + "]", Integer.valueOf(i), values.get(i));
        }
        values.clear();
        for (final Integer i : new IntegerRange(2, 2, 4)) {
            values.add(i);
        }
        assertEquals("Range size", 1, values.size());
        assertEquals("values[" + 0 + "]", Integer.valueOf(2), values.get(0));
        values.clear();
        for (final Integer i : new IntegerRange(2, 2, 3)) {
            values.add(i);
        }
        assertEquals("Range size", 1, values.size());
        assertEquals("values[" + 0 + "]", Integer.valueOf(2), values.get(0));
        values.clear();
        for (final Integer i : new IntegerRange(2, 2, 5)) {
            values.add(i);
        }
        assertEquals("Range size", 2, values.size());
        assertEquals("values[" + 0 + "]", Integer.valueOf(2), values.get(0));
        assertEquals("values[" + 1 + "]", Integer.valueOf(4), values.get(1));
        values.clear();
        for (final Integer i : new IntegerRange(5, -2, 2)) {
            values.add(i);
        }
        assertEquals("Range size", 2, values.size());
        assertEquals("values[" + 0 + "]", Integer.valueOf(5), values.get(0));
        assertEquals("values[" + 1 + "]", Integer.valueOf(3), values.get(1));
        values.clear();
        for (final Integer i : new IntegerRange(2, -2, 5)) {
            values.add(i);
        }
        assertEquals("Range size", 0, values.size());
    }

}
