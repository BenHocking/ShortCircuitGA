/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */

package edu.virginia.cs.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test harness for ListHistory
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 */
public class ListHistoryTest {

    /**
     * Test of add method, of class ListHistory.
     */
    @Test
    public void testAdd() {
        ListHistory instance = new ListHistory();
        assertTrue(instance.add(new Object()));
    }

    /**
     * Test of get method, of class ListHistory.
     */
    @Test
    public void testGet() {
        Object someObject = new Object();
        ListHistory instance = new ListHistory();
        instance.add(someObject);
        assertEquals(someObject, instance.get(0));
    }

}