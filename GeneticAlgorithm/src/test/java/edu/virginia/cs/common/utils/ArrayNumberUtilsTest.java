/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import static org.junit.Assert.*;
import static edu.virginia.cs.common.utils.ArrayNumberUtils.*;

import java.util.ArrayList;
import java.util.Collections;
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
     * Tests toDoubleList in ArrayNumberUtils
     */
    @Test
    public void testToDoubleList() {
        new ArrayNumberUtils(); // For coverage
        final String[] toConvert = {
            "0.1", "0.2", "-0.1", "1e5", "1E-3"
        };
        final List<Double> expected = new ArrayList<Double>();
        expected.add(0.1);
        expected.add(0.2);
        expected.add(-0.1);
        expected.add(1e5);
        expected.add(1e-3);
        final List<Double> result = toDoubleList(toConvert);
        assertEquals(expected, result);
    }

    /**
     * Tests sum in ArrayNumberUtils
     */
    @Test
    public void testSum() {
        final String[] toSum = {
            "0.1", "0.2", "-0.15"
        };
        assertEquals(0.15, sum(toSum), 1e-10);
        final Integer[] toAdd = {
            1, 2, 3
        };
        assertEquals(6, sum(toAdd), 0);
        assertEquals(0, sum(new ArrayList<Byte>()), 0.0);
    }

    /**
     * Tests sumOfSquares in ArrayNumberUtils
     */
    @Test
    public void testSumOfSquares() {
        final String[] toSum = {
            "0.1", "0.2", "-0.15"
        };
        assertEquals(0.0725, sumOfSquares(toSum), 1e-10);
        final Integer[] toAdd = {
            1, 2, 3
        };
        assertEquals(14, sumOfSquares(toAdd), 0);
        assertEquals(0, sumOfSquares(new ArrayList<Byte>()), 0.0);
    }

    /**
     * Tests add in ArrayNumberUtils
     */
    @Test
    public void testAdd() {
        final List<Double> list1 = new ArrayList<Double>();
        list1.add(1.5);
        list1.add(-2.1);
        final List<Double> list2 = new ArrayList<Double>();
        list2.add(2.3);
        list2.add(1.05);
        final List<Double> list3 = new ArrayList<Double>();
        list3.add(list1.get(0) + list2.get(0));
        list3.add(list1.get(1) + list2.get(1));
        final List<Double> saveList1 = Collections.unmodifiableList(list1);
        final List<Double> saveList2 = Collections.unmodifiableList(list2);
        assertEquals(list3, add(list1, list2));
        assertEquals(saveList1, list1);
        assertEquals(saveList2, list2);
        assertEquals(list3, add(list2, list1));
        assertEquals(new ArrayList<Double>(), add(new ArrayList<Double>(), new ArrayList<Double>()));
        try {
            add(new ArrayList<Double>(), list1);
            fail("Cannot add lists of unequal size");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Lists being added together must be of the same size.", e.getMessage());
        }
        try {
            add(list1, new ArrayList<Double>());
            fail("Cannot add lists of unequal size");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Lists being added together must be of the same size.", e.getMessage());
        }
    }

    /**
     * Tests accum in ArrayNumberUtils
     */
    @Test
    public void testAccum() {
        final List<Double> list1 = new ArrayList<Double>();
        list1.add(1.5);
        list1.add(-2.1);
        final List<Double> list2 = new ArrayList<Double>();
        list2.add(2.3);
        list2.add(1.05);
        final List<Double> list3 = new ArrayList<Double>();
        list3.add(list1.get(0) + list2.get(0));
        list3.add(list1.get(1) + list2.get(1));
        final List<Double> saveList2 = Collections.unmodifiableList(list2);
        accum(list1, list2);
        assertEquals(list3, list1);
        assertEquals(saveList2, list2);
        list2.add(3.14);
        list3.set(0, list3.get(0) + list2.get(0));
        list3.set(1, list3.get(1) + list2.get(1));
        list3.add(3.14);
        accum(list1, list2);
        assertEquals(list3, list1);
    }

    /**
     * Tests divide in ArrayNumberUtils
     */
    @Test
    public void testDivide() {
        final List<Double> list1 = new ArrayList<Double>();
        list1.add(1.5);
        list1.add(-2.1);
        list1.add(1.0);
        final List<Double> list2 = new ArrayList<Double>();
        list2.add(0.3);
        list2.add(7.0);
        list2.add(0.0);
        final List<Double> list3 = new ArrayList<Double>();
        list3.add(list1.get(0) / list2.get(0));
        list3.add(list1.get(1) / list2.get(1));
        list3.add(Double.POSITIVE_INFINITY);
        final List<Double> saveList1 = Collections.unmodifiableList(list1);
        final List<Double> saveList2 = Collections.unmodifiableList(list2);
        assertEquals(list3, divide(list1, list2));
        assertEquals(saveList1, list1);
        assertEquals(saveList2, list2);
        final List<Double> list4 = new ArrayList<Double>();
        final double divisor = 3.0;
        list4.add(list1.get(0) / divisor);
        list4.add(list1.get(1) / divisor);
        list4.add(list1.get(2) / divisor);
        assertEquals(list4, divide(list1, divisor));
        assertEquals(saveList1, list1);
        assertEquals(null, divide(null, divisor));
        assertEquals(new ArrayList<Double>(), divide(new ArrayList<Double>(), divisor));
        assertEquals(new ArrayList<Double>(), divide(new ArrayList<Double>(), new ArrayList<Double>()));
        try {
            divide(list1, new ArrayList<Double>());
            fail("Cannot divide lists of differing sizes");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Lists being divided must be of the same size.", e.getMessage());
        }
    }

    /**
     * Tests multiply in ArrayNumberUtils
     */
    @Test
    public void testMultiply() {
        final List<Double> list1 = new ArrayList<Double>();
        list1.add(1.5);
        list1.add(-2.1);
        final List<Double> list3 = new ArrayList<Double>();
        final double factor = 1.1;
        list3.add(list1.get(0) * factor);
        list3.add(list1.get(1) * factor);
        final List<Double> saveList1 = Collections.unmodifiableList(list1);
        assertEquals(list3, multiply(list1, factor));
        assertEquals(saveList1, list1);
        assertEquals(null, multiply(null, factor));
        assertEquals(new ArrayList<Double>(), multiply(new ArrayList<Double>(), factor));
    }

    /**
     * Tests toDoubleList in ArrayNumberUtils
     */
    @Test
    public void testMean() {
        final String[] strValues = {
            "0.1", "0.2", "-0.1", "1e5", "1E-3"
        };
        assertEquals(0.05, mean(strValues, 1, 2), 1E-10);
        assertEquals(0.2 / 3, mean(strValues, 0, 2), 1E-10);
        assertEquals(Double.NaN, mean(strValues, 1, 0), 1E-10);
        assertEquals(1e-3, mean(strValues, 4, 10), 1E-10);
        assertEquals(1e5, mean(strValues, 3, 3), 1E-10);
        final List<Double> dblValues = toDoubleList(strValues);
        assertEquals((0.2 + 1e5 + 1e-3) / 5, mean(dblValues), 1e-10);
        assertEquals(Double.NaN, mean(null), 0);
        assertEquals(Double.NaN, mean(new ArrayList<Double>()), 0);
        assertEquals(0.05, mean(dblValues, 1, 2), 1E-10);
        assertEquals(0.2 / 3, mean(dblValues, 0, 2), 1E-10);
        assertEquals(Double.NaN, mean(dblValues, 1, 0), 1E-10);
        assertEquals(1e-3, mean(dblValues, 4, 4), 1E-10);
        assertEquals(1e-3, mean(dblValues, 4, 10), 1E-10);
    }

    /**
     * Tests specialDivide in ArrayNumberUtils
     */
    @Test
    public void testSpecialDivide() {
        final List<Double> list1 = new ArrayList<Double>();
        list1.add(1.5);
        list1.add(-2.1);
        list1.add(1.0);
        final List<Double> list2 = new ArrayList<Double>();
        list2.add(0.3);
        list2.add(7.0);
        list2.add(0.0);
        final List<Double> list3 = new ArrayList<Double>();
        list3.add(list1.get(0) / list2.get(0));
        list3.add(list1.get(1) / list2.get(1));
        list3.add(0.0);
        final List<Double> saveList1 = Collections.unmodifiableList(list1);
        final List<Double> saveList2 = Collections.unmodifiableList(list2);
        assertEquals(list3, specialDivide(list1, list2));
        assertEquals(saveList1, list1);
        assertEquals(saveList2, list2);
        assertEquals(new ArrayList<Double>(), specialDivide(new ArrayList<Double>(), new ArrayList<Double>()));
        try {
            specialDivide(list1, new ArrayList<Double>());
            fail("Cannot divide lists of differing sizes");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Lists being divided must be of the same size.", e.getMessage());
        }
    }

    /**
     * Tests sampleStdDev in ArrayNumberUtils
     */
    @Test
    public void testSampleStdDev() {
        final List<Double> list1 = new ArrayList<Double>();
        final List<List<Double>> matrix = new ArrayList<List<Double>>();
        matrix.add(list1);
        list1.add(1.5);
        assertEquals(Double.NaN, sampleStdDev(matrix), 0);
        list1.add(-2.1);
        list1.add(1.0);
        assertEquals(1.950213663508, sampleStdDev(matrix), 1e-8);
        list1.add(Double.POSITIVE_INFINITY);
        assertEquals(Double.NaN, sampleStdDev(matrix), 0);
        list1.clear();
        list1.add(Double.NaN);
        list1.add(0.0);
        assertEquals(Double.NaN, sampleStdDev(matrix), 0);
    }

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
        final List<Double> pntList = new ArrayList<Double>();
        pntList.add(1.0); // Can't get a slope from a single point
        assertEquals(Double.NaN, slope(pntList), 0);
    }

    /**
     * Tests add in ArrayNumberUtils
     */
    @Test
    public void testSubtract() {
        final List<Double> list1 = new ArrayList<Double>();
        list1.add(1.5);
        list1.add(-2.1);
        final List<Double> list2 = new ArrayList<Double>();
        list2.add(2.3);
        list2.add(1.05);
        final List<Double> expected = new ArrayList<Double>();
        expected.add(1.5 - 2.3);
        expected.add(-2.1 - 1.05);
        assertEquals(expected, subtract(list1, list2));
        assertEquals(new ArrayList<Double>(), subtract(new ArrayList<Double>(), new ArrayList<Double>()));
        try {
            subtract(list1, new ArrayList<Double>());
            fail("Should not be able to subtract lists of differing sizes.");
        }
        catch (final IllegalArgumentException ex) {
            assertEquals("Lists being subtracted must be of the same size.", ex.getMessage());
        }
    }
}
