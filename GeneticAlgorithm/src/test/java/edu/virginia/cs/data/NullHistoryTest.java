/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */

package edu.virginia.cs.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test harness for NullHistory
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 */
public class NullHistoryTest {


    /**
     * Test of add method, of class NullHistory.
     */
    @Test
    public void testAdd() {
        NullHistory instance = new NullHistory();
        assertFalse(instance.add(null));
    }

    /**
     * Test of get method, of class NullHistory.
     */
    @Test
    public void testGet() {
        NullHistory instance = new NullHistory();
        assertNull(instance.get(0));
    }

}