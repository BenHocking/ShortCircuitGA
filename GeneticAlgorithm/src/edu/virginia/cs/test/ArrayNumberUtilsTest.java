/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test;

import static org.junit.Assert.*;
import static edu.virginia.cs.common.utils.ArrayNumberUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * Test harness for ArrayNumberUtils class
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 7, 2011
 */
public class ArrayNumberUtilsTest {

    /**
     * Verifies that null lists are compared in the correct way
     */
    @Test
    public void testSlope() {
        final double yintercept = 100;
        final List<Double> slope1 = new ArrayList<Double>();
        for (int i = 0; i < 100; ++i) {
            slope1.add(i + yintercept);
        }
        assertEquals(1.0, slope(slope1), 0.0);
        final List<Double> slope2 = new ArrayList<Double>();
        for (int i = 0; i < 100; ++i) {
            slope2.add(2 * i + yintercept);
        }
        assertEquals(2.0, slope(slope2), 0.0);
        final List<Double> slopeneg = new ArrayList<Double>();
        for (int i = 0; i < 100; ++i) {
            slopeneg.add(-i + yintercept);
        }
        assertEquals(-1.0, slope(slopeneg), 0.0);
        final List<Double> sloperand = new ArrayList<Double>();
        final Random rand = new Random();
        for (int i = 0; i < 1000; ++i) {
            sloperand.add(2 * i + yintercept + 100 * rand.nextDouble());
        }
        final double actual = slope(sloperand);
        assertEquals(2.0, actual, 0.01); // Should be close to 2
        assertTrue(Math.abs(actual - 2.0) > 1e-5); // But not too close
    }
}
