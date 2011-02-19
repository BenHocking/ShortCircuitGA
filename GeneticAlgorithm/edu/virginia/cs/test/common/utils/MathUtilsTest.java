/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import edu.virginia.cs.common.utils.MathUtils;

import static edu.virginia.cs.common.utils.MathUtils.*;

/**
 * Test of the {@link edu.virginia.cs.common.utils.MathUtils MathUtils} class.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 30, 2010
 */
public class MathUtilsTest {

    /**
     * Test of {@link edu.virginia.cs.common.utils.MathUtils#imposeBounds(double, double, double) MathUtils.imposeBounds(double,
     * double, double)}
     */
    @Test
    public void imposeBoundsTest() {
        new MathUtils(); // For coverage
        final double min = Math.E;
        final double max = Math.PI;
        Assert.assertEquals(min, imposeBounds(min, 0, max), 0.0);
        Assert.assertEquals(max, imposeBounds(min, Double.POSITIVE_INFINITY, max), 0.0);
        Assert.assertEquals(Double.NaN, imposeBounds(min, Double.NaN, max), 0.0);
        Assert.assertEquals(Double.NaN, imposeBounds(Double.NaN, 0, max), 0.0);
        Assert.assertEquals(Double.NaN, imposeBounds(min, 0, Double.NaN), 0.0);
        final double avg = (min + max) / 2;
        Assert.assertEquals(avg, imposeBounds(min, avg, max), 0.0);
        Assert.assertEquals(min, imposeBounds(max, avg, min), 0.0);
        Assert.assertEquals(min, imposeBounds(max, 0, min), 0.0);
        Assert.assertEquals(min, imposeBounds(max, 2 * max, min), 0.0);
    }

    /**
     * Test of {@link edu.virginia.cs.common.utils.MathUtils#scale(double, double, double) MathUtils.scale(double, double, double)}
     * and {@link edu.virginia.cs.common.utils.MathUtils#scale(double, double, double, boolean) MathUtils.scale(double, double,
     * double, boolean)}
     */
    @Test
    public void scaleTest() {
        final double min = Math.E;
        final double max = Math.PI;
        Assert.assertEquals(min, scale(min, 0, max), 0.0);
        Assert.assertEquals(max, scale(min, 1, max), 0.0);
        Assert.assertEquals(Double.NaN, scale(min, Double.NaN, max), 0.0);
        final double avg = (min + max) / 2;
        Assert.assertEquals(avg, scale(min, 0.5, max), 0.0);
        final int minInt = 0;
        final int maxInt = 10;
        // This one goes to 11
        Assert.assertEquals(11, scale(minInt, 1.1, maxInt), 0.0);
        // As does this one
        Assert.assertEquals(11, scale(minInt, 1.1, maxInt, false), 0.0);
        // This one stops at 10
        Assert.assertEquals(10, scale(minInt, 1.1, maxInt, true), 0.0);
    }

    /**
     * Test of {@link edu.virginia.cs.common.utils.MathUtils#scaleInt(int, double, int) MathUtils.scaleInt(int, double, int)}
     */
    @Test
    public void scaleIntTest() {
        final int min = 1;
        final int max = 10;
        Assert.assertEquals(min, scaleInt(min, 0, max));
        Assert.assertEquals(max, scaleInt(min, 1, max));
        // This one doesn't go to 11
        Assert.assertEquals(max, scaleInt(min, 1.1, max));
        final int bucketSize = 11; // 10 + 1 for 0th element
        final List<Integer> bucketCnt = new ArrayList<Integer>(bucketSize);
        // Initialize
        for (int i = 0; i < bucketSize; ++i) {
            bucketCnt.add(0);
        }
        // Verify that the distribution over [1, 10] is approximately uniform
        final int randomSeed = 1;
        final Random rng = new Random(randomSeed);
        for (int i = 0; i < 1000000; ++i) {
            final int randInt = scaleInt(min, rng.nextDouble(), max);
            bucketCnt.set(randInt, bucketCnt.get(randInt) + 1);
        }
        Assert.assertEquals(Integer.valueOf(0), bucketCnt.get(0));
        Assert.assertEquals(Integer.valueOf(99948), bucketCnt.get(1));
        Assert.assertEquals(Integer.valueOf(100203), bucketCnt.get(2));
        Assert.assertEquals(Integer.valueOf(100330), bucketCnt.get(3));
        Assert.assertEquals(Integer.valueOf(99853), bucketCnt.get(4));
        Assert.assertEquals(Integer.valueOf(99786), bucketCnt.get(5));
        Assert.assertEquals(Integer.valueOf(100207), bucketCnt.get(6));
        Assert.assertEquals(Integer.valueOf(99970), bucketCnt.get(7));
        Assert.assertEquals(Integer.valueOf(99974), bucketCnt.get(8));
        Assert.assertEquals(Integer.valueOf(99966), bucketCnt.get(9));
        Assert.assertEquals(Integer.valueOf(99763), bucketCnt.get(10));
    }

    /**
     * Test of {@link edu.virginia.cs.common.utils.MathUtils#scaleIntInverse(int, int, int) MathUtils.scaleIntInverse(int, int,
     * int)}
     */
    @Test
    public void scaleIntInverseTest() {
        Assert.assertEquals(0.5, scaleIntInverse(1, 2, 3), 0.0);
    }

}
