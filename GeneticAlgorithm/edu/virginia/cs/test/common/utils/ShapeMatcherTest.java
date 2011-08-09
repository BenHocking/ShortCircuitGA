/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.common.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.virginia.cs.common.utils.ShapeMatcher;

/**
 * Test harness for ShapeMatcher
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jun 2, 2011
 */
public class ShapeMatcherTest {

    /**
     * Test method for {@link edu.virginia.cs.common.utils.ShapeMatcher#squareDeviation(java.util.List, java.util.List)}.
     */
    @Test
    public final void testSquareDeviation() {
        // Test parabolas
        final List<Double> targetParabola = new ArrayList<Double>();
        final List<Double> actual1 = new ArrayList<Double>();
        final List<Double> actual2 = new ArrayList<Double>();
        final int numPoints = 20;
        for (int i = 0; i < numPoints; ++i) {
            targetParabola.add(Double.valueOf(i * i));
            actual1.add(1.5 * Math.pow(i, 2) + 1.7);
            actual2.add(-1.5 * Math.pow(i, 2) + 100);
        }
        // The shapes only differ by scale and offset
        assertEquals(0.0, ShapeMatcher.squareDeviation(targetParabola, actual1), 1E-10);
        // Should not allow for inverse shape, though
        assertEquals(29760.37792, ShapeMatcher.squareDeviation(targetParabola, actual2), 1E-10);
        // Test lines
        final List<Double> targetLine = new ArrayList<Double>();
        final List<List<Double>> actuals = new ArrayList<List<Double>>();
        final int numActuals = 11;
        for (int i = 0; i < numActuals; ++i) {
            actuals.add(new ArrayList<Double>());
        }
        final int zeroPt = (numActuals - 1) / 2;
        for (int i = 0; i < numPoints; ++i) {
            targetLine.add(Double.valueOf(i));
            for (int j = 0; j < numActuals; ++j) {
                actuals.get(j).add(Double.valueOf((j - zeroPt) * i));
            }
        }
        for (int i = 0; i < numActuals; ++i) {
            if (i < zeroPt) {
                // Should not allow for inverse shape
                // (result will be the mean of the first n/2 odd squares, assuming n is even)
                double expected = 0.0;
                for (int j = 0; j < numPoints / 2; ++j) {
                    expected += Math.pow(2 * j + 1, 2);
                }
                expected /= (numPoints / 2);
                assertEquals("For actual #" + i, expected, ShapeMatcher.squareDeviation(targetLine, actuals.get(i)), 1E-10);
            }
            else if (i == zeroPt) {
                // Should not allow for flat shape (result will just be sum of first n-1 squares divided by n)
                final double expected = ((numPoints - 1) * numPoints * (2 * numPoints - 1)) / (numPoints * 6.0);
                assertEquals("For actual #" + i, expected, ShapeMatcher.squareDeviation(targetLine, actuals.get(i)), 1E-10);
            }
            else {
                // The shapes only differ by scale
                assertEquals("For actual #" + i, 0, ShapeMatcher.squareDeviation(targetLine, actuals.get(i)), 1E-10);
            }
        }
    }

}
