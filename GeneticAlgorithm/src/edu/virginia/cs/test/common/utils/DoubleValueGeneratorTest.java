/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.common.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.virginia.cs.common.utils.DoubleValueGenerator;

/**
 * Test harness for DoubleValueGenerator
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 18, 2011
 */
public class DoubleValueGeneratorTest {

    /**
     * Tests generate in ArrayNumberUtils
     */
    @Test
    public void testGenerate() {
        final DoubleValueGenerator instance = new DoubleValueGenerator(1, 2);
        assertEquals(1.0, Double.valueOf(instance.generate(0.0)), 0.0);
        assertEquals(2.0, Double.valueOf(instance.generate(1.0)), 0.0);
        assertEquals(1.5, Double.valueOf(instance.generate(0.5)), 0.0);
        assertEquals(2.0, Double.valueOf(instance.generate(2.0)), 0.0); // Clamped
    }

    /**
     * Tests invert in ArrayNumberUtils
     */
    @Test
    public void testInvert() {
        final DoubleValueGenerator instance = new DoubleValueGenerator(1, 2);
        assertEquals(0.0, instance.invert("1.0"), 0.0);
        assertEquals(1.0, instance.invert("2.0"), 0.0);
        assertEquals(0.5, instance.invert("1.5"), 0.0);
        assertEquals(2.0, instance.invert("3.0"), 0.0); // Not clamped
    }
}
