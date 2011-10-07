/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static edu.virginia.cs.common.utils.EqualUtils.*;
import static org.junit.Assert.*;

/**
 * Test harness for EqualUtils
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 18, 2011
 */
public class EqualUtilsTest {

    /**
     * Tests all eq methods in EqualUtils
     */
    @Test
    public void testEq() {
        new EqualUtils(); // for coverage
        assertEquals(true, eq(true, true));
        assertEquals(false, eq(false, true));
        assertEquals(true, eq('\0', '\0'));
        assertEquals(false, eq('\0', ' '));
        assertEquals(true, eq(0, 0));
        assertEquals(false, eq(0, 1));
        assertEquals(true, eq(0L, 0L));
        assertEquals(false, eq(0L, 1L));
        assertEquals(true, eq(0.0f, 0.0f));
        assertEquals(true, eq(Float.NaN, Float.NaN));
        assertEquals(false, eq(0.0f, 1.0f));
        assertEquals(true, eq(0.0, 0.0));
        assertEquals(true, eq(Double.NaN, Double.NaN));
        assertEquals(false, eq(0.0, 1.0));
        assertEquals(true, eq(null, null));
        final Object o = new Object();
        assertEquals(false, eq(o, null));
        assertEquals(false, eq(null, o));
        assertEquals(false, eq(new Object(), o));
        assertEquals(true, eq(o, o));
        final List<Integer> l1 = new ArrayList<Integer>();
        assertEquals(false, eq(l1, o));
        assertEquals(false, eq(o, l1));
        final List<Integer> l2 = new ArrayList<Integer>();
        assertEquals(true, eq(l1, l2));
        l1.add(0);
        assertEquals(false, eq(l1, l2));
        l2.add(0);
        assertEquals(true, eq(l1, l2));
        l2.set(0, 1);
        assertEquals(false, eq(l1, l2));
        final Integer[] a1 = {
            0
        };
        final Integer[] a2 = {
            0
        };
        assertEquals(true, eq(a1, a2));
        final Integer[] a3 = {
            1
        };
        assertEquals(false, eq(a1, a3));
        final Integer[] a4 = {
            0, 1
        };
        assertEquals(false, eq(a1, a4));
    }

}
