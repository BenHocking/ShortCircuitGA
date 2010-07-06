/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for element wise operations on arrays of Number objects
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 6, 2010
 */
public final class ArrayNumberUtils {

    /**
     * Converts an array of Strings to a List of Doubles
     * @param valList Array of Strings
     * @return List of Doubles corresponding to the the Array of Strings
     * @exception NumberFormatException if one of the Strings does not contain a parsable number.
     */
    public static List<Double> toDoubleList(final String[] valList) {
        final List<Double> retval = new ArrayList<Double>();
        for (final String s : valList) {
            retval.add(Double.valueOf(s));
        }
        return retval;
    }

    /**
     * Adds up all of the values in an array of Strings.
     * @param valArray Array of Strings
     * @return Sum of values in the array
     * @exception NumberFormatException if one of the Strings does not contain a parsable number.
     */
    public static double sum(final String[] valArray) {
        return sum(toDoubleList(valArray));
    }

    /**
     * Adds up all of the values in an array of Numbers.
     * @param <T> Class extending Number (e.g., Double or Integer)
     * @param numberArray Array of Numbers
     * @return Sum of values in the array
     */
    public static <T extends Number> double sum(final T[] numberArray) {
        return sum(Arrays.asList(numberArray));
    }

    /**
     * Adds up all of the values in a List of Numbers.
     * @param <T> Class extending Number (e.g., Double or Integer)
     * @param numberList List of Numbers
     * @return Sum of values in the List
     */
    public static <T extends Number> double sum(final List<T> numberList) {
        double retval = 0;
        if (!numberList.isEmpty()) {
            for (final Integer i : new IntegerRange(numberList.size())) {
                retval += numberList.get(i).doubleValue();
            }
        }
        return retval;
    }

    /**
     * Adds up all of the squares of values in an array of Strings.
     * @param valList Array of Strings
     * @return Sum of squares of values in the array
     * @exception NumberFormatException if one of the Strings does not contain a parsable number.
     */
    public static double sumOfSquares(final String[] valList) {
        return sumOfSquares(toDoubleList(valList));
    }

    /**
     * Adds up all of the squares of values in an array of Numbers.
     * @param <T> Class extending Number (e.g., Double or Integer)
     * @param numberArray Array of Numbers
     * @return Sum of squares of values in the array
     */
    public static <T extends Number> double sumOfSquares(final T[] numberArray) {
        return sumOfSquares(Arrays.asList(numberArray));
    }

    /**
     * Adds up all of the squares of values in a List of Numbers.
     * @param <T> Class extending Number (e.g., Double or Integer)
     * @param numberList List of Numbers
     * @return Sum of squares of values in the List
     */
    public static <T extends Number> double sumOfSquares(final List<T> numberList) {
        double retval = 0;
        if (!numberList.isEmpty()) {
            for (final Integer i : new IntegerRange(numberList.size())) {
                final double v = numberList.get(i).doubleValue();
                retval += v * v;
            }
        }
        return retval;
    }

    /**
     * Adds up two equal sized Lists into a resultant List of the same size
     * @param list1 First list to add
     * @param list2 Second list to add
     * @pre list1.size() == list2.size()
     * @return Addition of two arrays
     */
    public static List<Double> add(final List<Double> list1, final List<Double> list2) {
        if (list1.size() != list2.size())
            throw new IllegalArgumentException("Lists being added together must be of the same size.");
        final List<Double> retval = new ArrayList<Double>(list1.size());
        if (!list1.isEmpty()) {
            for (final Integer i : new IntegerRange(list1.size())) {
                retval.add(list1.get(i) + list2.get(i));
            }
        }
        return retval;
    }

    /**
     * Adds up two equal sized Lists and stores the result in the first list
     * @param accumulator First list to add
     * @param toAccumulate Second list to add
     * @pre list1.size() == list2.size()
     * @exception IllegalArgumentException if two lists are not of the same size
     * @exception NullPointerException if either list is null
     */
    public static void accum(final List<Double> accumulator, final List<Double> toAccumulate) {
        if (accumulator.size() != toAccumulate.size())
            throw new IllegalArgumentException("Lists being added together must be of the same size.");
        if (!accumulator.isEmpty()) {
            for (final Integer i : new IntegerRange(accumulator.size())) {
                accumulator.set(i, accumulator.get(i) + toAccumulate.get(i));
            }
        }
    }

    /**
     * Divides elements in the first {@link java.util.List List} by elements in the second {@link java.util.List List}, and returns
     * the result in a {@link java.util.List List} of the same size.
     * @param numerator Numerator {@link java.util.List List}
     * @param denominator Denominator {@link java.util.List List}
     * @return Dividend {@link java.util.List List}
     * @exception IllegalArgumentException if two lists are not of the same size
     * @exception NullPointerException if either list is null
     */
    public static List<Double> divide(final List<Double> numerator, final List<Double> denominator) {
        if (numerator.size() != denominator.size())
            throw new IllegalArgumentException("Lists being added together must be of the same size.");
        final List<Double> retval = new ArrayList<Double>(numerator.size());
        if (!numerator.isEmpty()) {
            for (final Integer i : new IntegerRange(numerator.size())) {
                retval.add(numerator.get(i) / denominator.get(i));
            }
        }
        return retval;
    }

    /**
     * Divides elements in a {@link java.util.List List} by a constant value and returns the result in a {@link java.util.List List}
     * of the same size.
     * @param numerator Numerator {@link java.util.List List}
     * @param val Denominator
     * @return Dividend {@link java.util.List List}
     */
    public static List<Double> divide(final List<Double> numerator, final double val) {
        if (numerator == null) return null;
        final List<Double> retval = new ArrayList<Double>(numerator.size());
        if (!numerator.isEmpty()) {
            for (final Integer i : new IntegerRange(numerator.size())) {
                retval.add(numerator.get(i) / val);
            }
        }
        return retval;
    }

    /**
     * Calculates the mean of an array of Strings
     * @param values Array of Strings to find the mean of
     * @param first First element (inclusive) in the array to include in the mean
     * @param last Last element (inclusive) in the array to include in the mean
     * @return Mean value of the selected range of elements
     * @exception NumberFormatException if one of the Strings does not contain a parsable number.
     */
    public static double mean(final String[] values, final int first, final int last) {
        double sum = 0.0;
        final int trueLast = Math.min(last + 1, values.length);
        if (trueLast >= first) {
            for (final Integer i : new IntegerRange(first, trueLast)) {
                sum += Double.parseDouble(values[i]);
            }
        }
        return sum / (trueLast - first + 1);
    }

    /**
     * Divides elements in the first {@link java.util.List List} by elements in the second {@link java.util.List List}, and returns
     * the result in a {@link java.util.List List} of the same size, but with a special case that if the both the numerator and
     * denominator are zero the "dividend" is also zero.
     * @param numerator Numerator {@link java.util.List List}
     * @param denominator Denominator {@link java.util.List List}
     * @return Dividend {@link java.util.List List}
     * @exception IllegalArgumentException if two lists are not of the same size
     * @exception NullPointerException if either list is null
     */
    public static List<Double> specialDivide(final List<Double> numerator, final List<Double> denominator) {
        if (numerator.size() != denominator.size())
            throw new IllegalArgumentException("Lists being added together must be of the same size.");
        final List<Double> retval = new ArrayList<Double>(numerator.size());
        if (!numerator.isEmpty()) {
            for (final Integer i : new IntegerRange(numerator.size())) {
                retval.add(denominator.get(i).doubleValue() != 0 ? numerator.get(i) / denominator.get(i) : Double.valueOf(0));
            }
        }
        return retval;
    }
}
