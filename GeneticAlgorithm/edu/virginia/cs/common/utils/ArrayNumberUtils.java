/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for element wise operations on arrays of Number objects
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
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
            for (int i = 0; i < numberList.size(); ++i) {
                final double v = numberList.get(i).doubleValue();
                retval += v * v;
            }
        }
        return retval;
    }

    /**
     * Normalizes a list of doubles such that the sum of the list equals 1
     * @param list List to normalize
     * @return Normalized list (note that original list is unmodified)
     */
    public static List<Double> normalize(final List<Double> list) {
        final double listSum = sum(list);
        return divide(list, listSum);
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
     * @param list List to add to
     * @param toAdd Amount to add to the list
     * @return List with all values increased by toAdd
     */
    public static List<Double> add(final List<Double> list, final double toAdd) {
        final List<Double> retval = new ArrayList<Double>(list.size());
        if (!list.isEmpty()) {
            for (final Integer i : new IntegerRange(list.size())) {
                retval.add(list.get(i) + toAdd);
            }
        }
        return retval;
    }

    /**
     * Adds up two equal sized Lists and stores the result in the first list
     * @param accumulator First list to add
     * @param toAccumulate Second list to add
     * @exception NullPointerException if either list is null
     */
    public static void accum(final List<Double> accumulator, final List<Double> toAccumulate) {
        for (final Integer i : new IntegerRange(toAccumulate.size())) {
            if (i < accumulator.size()) {
                accumulator.set(i, accumulator.get(i) + toAccumulate.get(i));
            }
            else {
                accumulator.add(toAccumulate.get(i));
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
            throw new IllegalArgumentException("Lists being divided must be of the same size.");
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
     * Multiplies elements in a {@link java.util.List List} by a constant value and returns the result in a {@link java.util.List
     * List} of the same size.
     * @param dblList {@link java.lang.Double Double} {@link java.util.List List}
     * @param factor Multiplier
     * @return Resultant {@link java.util.List List}
     */
    public static List<Double> multiply(final List<Double> dblList, final double factor) {
        if (dblList == null) return null;
        final List<Double> retval = new ArrayList<Double>(dblList.size());
        if (!dblList.isEmpty()) {
            for (final Integer i : new IntegerRange(dblList.size())) {
                retval.add(dblList.get(i) * factor);
            }
        }
        return retval;
    }

    /**
     * Calculates the mean of an array of Strings
     * @param values Array of {@link java.lang.String Strings} to find the mean of
     * @param first First element (inclusive) in the array to include in the mean
     * @param last Last element (inclusive) in the array to include in the mean
     * @return Mean value of the selected range of elements
     * @exception NumberFormatException if one of the Strings does not contain a parsable number.
     */
    public static double mean(final String[] values, final int first, final int last) {
        final int trueLast = Math.min(last + 1, values.length);
        if (trueLast <= first) {
            return Double.NaN;
        }
        double sum = 0.0;
        for (final Integer i : new IntegerRange(first, trueLast)) {
            sum += Double.parseDouble(values[i]);
        }
        return sum / (trueLast - first);
    }

    /**
     * Calculates the mean of an {@link java.util.List List} of {@link java.lang.Double Doubles}
     * @param dblList {@link java.util.List List} of {@link java.lang.Double Doubles} to find the mean of
     * @return Mean value of the provided {@link java.util.List List}
     */
    public static double mean(final List<Double> dblList) {
        if (dblList == null || dblList.size() == 0) return Double.NaN;
        double sum = 0.0;
        for (final Double d : dblList) {
            sum += d;
        }
        return sum / dblList.size();
    }

    /**
     * Calculates the mean of an {@link java.util.List List} of {@link java.lang.Double Doubles}
     * @param dblList {@link java.util.List List} of {@link java.lang.Double Doubles} to find the mean of
     * @param first First element (inclusive) in the list to include in the mean
     * @param last Last element (inclusive) in the list to include in the mean
     * @return Mean value of the selected range of elements
     */
    public static double mean(final List<Double> dblList, final int first, final int last) {
        final int trueLast = Math.min(last + 1, dblList.size());
        if (trueLast <= first) {
            return Double.NaN;
        }
        double sum = 0.0;
        for (final Integer i : new IntegerRange(first, trueLast)) {
            sum += dblList.get(i);
        }
        return sum / (trueLast - first);
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
            throw new IllegalArgumentException("Lists being divided must be of the same size.");
        final List<Double> retval = new ArrayList<Double>(numerator.size());
        if (!numerator.isEmpty()) {
            for (final Integer i : new IntegerRange(numerator.size())) {
                retval.add(denominator.get(i).doubleValue() != 0 ? numerator.get(i) / denominator.get(i) : Double.valueOf(0));
            }
        }
        return retval;
    }

    /**
     * Calculates the sample standard deviation of a list of list of doubles
     * @param dblList List of list of doubles to calculate sample standard deviation for
     * @return Sample standard deviation
     */
    public static double sampleStdDev(final List<List<Double>> dblList) {
        int numSums = 0;
        double totalSum = 0.0;
        double totalSoS = 0.0;
        for (final List<Double> withinTrial : dblList) {
            totalSum += ArrayNumberUtils.sum(withinTrial);
            totalSoS += ArrayNumberUtils.sumOfSquares(withinTrial);
            numSums += withinTrial.size();
        }
        if (numSums > 1 && !Double.isNaN(totalSum)) {
            // Calculate sample standard deviation
            return Math.sqrt((numSums * totalSoS - totalSum * totalSum) / (numSums * (numSums - 1)));
        }
        return Double.NaN;
    }

    /**
     * @param dblList List to calculate the slope of (x values are taken as integer locations in list)
     * @return Slope of list
     */
    public static double slope(final List<Double> dblList) {
        final int listSize = dblList.size();
        if (listSize < 2) return Double.NaN;
        double sumxy = 0;
        for (int i = 0; i < listSize; ++i) {
            sumxy += i * dblList.get(i);
        }
        final double sumx = (listSize - 1) * listSize / 2;
        final double sumy = sum(dblList);
        final double sumxx = (listSize - 1) * listSize * (2 * listSize - 1) / 6;
        return (listSize * sumxy - sumx * sumy) / (listSize * sumxx - sumx * sumx);
    }

    /**
     * @param first List to be subtracted form
     * @param second List to subtract from the first
     * @return Difference between lists
     */
    public static List<Double> subtract(final List<Double> first, final List<Double> second) {
        if (first.size() != second.size()) throw new IllegalArgumentException("Lists being subtracted must be of the same size.");
        final List<Double> retval = new ArrayList<Double>(first.size());
        if (!first.isEmpty()) {
            final int listSize = first.size();
            for (int i = 0; i < listSize; ++i) {
                retval.add(first.get(i) - second.get(i));
            }
        }
        return retval;
    }
}
