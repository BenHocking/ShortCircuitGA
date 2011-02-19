/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.common.utils;

import static edu.virginia.cs.common.utils.HashUtils.hash;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.virginia.cs.common.utils.HashUtils;

/**
 * Test harness for HashUtilities
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 18, 2011
 */
public class HashUtilsTest {

    private static class SimpleClass {

        private final int _i;

        SimpleClass(final int i) {
            _i = i;
        }

        @Override
        public int hashCode() {
            return _i;
        }
    }

    /**
     * Tests all hash methods in HashUtils
     */
    @Test
    public void testEq() {
        new HashUtils(); // for coverage
        assertEquals(0, hash(0, false));
        assertEquals(1, hash(0, true));
        assertEquals(32, hash(0, ' '));
        assertEquals(-1, hash(0, -1));
        assertEquals(1, hash(0, -2L));
        assertEquals(0, hash(0, 0.0f));
        assertEquals(2143289344, hash(0, Float.NaN));
        assertEquals(0, hash(0, 0.0));
        assertEquals(2146959360, hash(0, Double.NaN));
        assertEquals(0, hash(0, null));
        final Object o = new SimpleClass(0);
        assertEquals(0, hash(0, o));
        final List<Integer> l1 = new ArrayList<Integer>();
        assertEquals(0, hash(0, l1));
        l1.add(0);
        assertEquals(0, hash(0, l1));
        final Integer[] a1 = {
            0
        };
        assertEquals(0, hash(0, a1));
    }
}
