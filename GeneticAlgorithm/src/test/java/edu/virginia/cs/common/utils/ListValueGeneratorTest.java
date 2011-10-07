/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test Harness for ListValueGenerator
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 19, 2011
 */
public class ListValueGeneratorTest {

    /**
     * Test method for {@link edu.virginia.cs.common.utils.ListValueGenerator#generate(double)}.
     */
    @Test
    public final void testGenerate() {
        final List<Object> strList = new ArrayList<Object>();
        strList.add("Object 1");
        strList.add("Object 2");
        final ListValueGenerator g = new ListValueGenerator(strList);
        assertEquals("Object 1", g.generate(0.45));
        assertEquals("Object 2", g.generate(0.55));
    }

    /**
     * Test method for {@link edu.virginia.cs.common.utils.ListValueGenerator#invert(java.lang.String)}.
     */
    @Test
    public final void testInvert() {
        final List<Object> strList = new ArrayList<Object>();
        strList.add("Object 1");
        strList.add("Object 2");
        final ListValueGenerator g = new ListValueGenerator(strList);
        assertEquals(0.25, g.invert("Object 1"), 0.0);
        assertEquals(0.75, g.invert("Object 2"), 0.0);
        try {
            g.invert("Object 3");
            fail("Cannot invert for an object not in the specified list");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("'Object 3' is not a valid value", e.getMessage());
        }
    }

}
