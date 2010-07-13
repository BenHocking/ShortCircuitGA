/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import edu.virginia.cs.common.ArrayGenericUtils;

/**
 * Test harness for {@link ArrayGenericUtils} class.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 3, 2010
 */
public class ArrayGenericUtilsTest {

    /**
     * Verifies that null lists are compared in the correct way
     */
    @Test
    public void testNullLists() {
        final List<Double> nullList = null;
        final List<Double> nonNullList = Collections.singletonList(1.2);
        assertTrue(ArrayGenericUtils.compare(nullList, nullList) == 0);
        assertTrue(ArrayGenericUtils.compare(nullList, nonNullList) < 0);
        assertTrue(ArrayGenericUtils.compare(nonNullList, nullList) > 0);
    }

    /**
     * Verifies that lists of the same size compare correctly
     */
    @Test
    public void testSameSizeLists() {
        final List<Double> lessList = new ArrayList<Double>();
        lessList.add(1.2);
        lessList.add(2.3);
        final List<Double> greaterList = new ArrayList<Double>();
        greaterList.add(1.2);
        greaterList.add(2.4);
        assertTrue(ArrayGenericUtils.compare(lessList, lessList) == 0);
        assertTrue(ArrayGenericUtils.compare(lessList, greaterList) < 0);
        assertTrue(ArrayGenericUtils.compare(greaterList, lessList) > 0);
    }

    /**
     * Verifies that lists of different sizes compare correctly
     */
    @Test
    public void testDifferentSizeLists() {
        final List<Double> lessList = new ArrayList<Double>();
        lessList.add(1.2);
        lessList.add(2.3);
        lessList.add(3.0);
        final List<Double> greaterList = new ArrayList<Double>();
        greaterList.add(1.2);
        greaterList.add(2.4);
        assertTrue(ArrayGenericUtils.compare(lessList, lessList) == 0);
        assertTrue(ArrayGenericUtils.compare(lessList, greaterList) < 0);
        assertTrue(ArrayGenericUtils.compare(greaterList, lessList) > 0);
        final List<Double> leastList = new ArrayList<Double>();
        leastList.add(1.2);
        leastList.add(2.3);
        assertTrue(ArrayGenericUtils.compare(leastList, lessList) < 0);
        assertTrue(ArrayGenericUtils.compare(lessList, leastList) > 0);
    }
}
