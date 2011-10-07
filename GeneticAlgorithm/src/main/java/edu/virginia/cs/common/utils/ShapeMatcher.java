/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.util.Collections;
import java.util.List;

/**
 * Utility class for matching shapes
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jun 2, 2011
 */
public final class ShapeMatcher {

    private static final double EPSILON = Math.sqrt(Double.MIN_VALUE);

    private static void expandToMatch(final List<Double> matcher, final List<Double> expander) {
        while (expander.size() < matcher.size()) {
            expander.add(Double.valueOf(0));
        }
    }

    /**
     * Generates a mean square deviation value for one list of numbers from another list of numbers, in a manner invariant to the
     * mean value of the second list of numbers
     * @param expected List of numbers describing the desired shape
     * @param actual List of numbers describing the actual shape
     * @return Mean-invariant square deviation
     */
    public static double squareDeviation(final List<Double> expected, final List<Double> actual) {
        // Add zeroes to smaller list until sizes match
        expandToMatch(expected, actual);
        expandToMatch(actual, expected);
        final double minExpected = Collections.min(expected);
        final double minActual = Collections.min(actual);
        List<Double> actualAdjusted = ArrayNumberUtils.add(actual, minExpected - minActual);
        final double sumExpected = ArrayNumberUtils.sum(expected);
        final double sumActual = ArrayNumberUtils.sum(actualAdjusted);
        if (Math.abs(sumActual) <= EPSILON) {
            if (Math.abs(sumExpected) <= EPSILON) {
                return 0.0;
            }
            return ArrayNumberUtils.sumOfSquares(expected) / expected.size();
        }
        actualAdjusted = ArrayNumberUtils.multiply(actualAdjusted, sumExpected / sumActual);
        final List<Double> scaledDiff = ArrayNumberUtils.subtract(actualAdjusted, expected);
        return ArrayNumberUtils.sumOfSquares(scaledDiff) / actual.size();
    }
}
