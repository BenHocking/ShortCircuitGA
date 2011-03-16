/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * For testing random stuff
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 26, 2011
 */
public class SandboxTest {

    /**
     * For testing random stuff
     */
    @Test
    public void sandbox() {
        final boolean expected = false;
        final boolean result = Double.NaN >= 0;
        assertEquals(expected, result);
    }
}
