/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.common.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.virginia.cs.common.utils.IntegerValueGenerator;

/**
 * Test harness for IntegerValueGenerator
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 18, 2011
 */
public class IntegerValueGeneratorTest {

    /**
     * Test method for {@link edu.virginia.cs.common.utils.IntegerValueGenerator#generate(double)}.
     */
    @Test
    public final void testGenerate() {
        final IntegerValueGenerator g = new IntegerValueGenerator(1, 10);
        assertEquals("1", g.generate(0.0));
        assertEquals("10", g.generate(1.0));
        assertEquals("10", g.generate(2.0));
        assertEquals("6", g.generate(0.5));
    }

    /**
     * Test method for {@link edu.virginia.cs.common.utils.IntegerValueGenerator#invert(java.lang.String)}.
     */
    @Test
    public final void testInvert() {
        final IntegerValueGenerator g = new IntegerValueGenerator(1, 10);
        assertEquals(0.05, g.invert("1"), 0.0);
        assertEquals(0.95, g.invert("10"), 1e-10);
        assertEquals(1.95, g.invert("20"), 0.0);
        assertEquals(0.55, g.invert("6"), 0.0);
    }

}
