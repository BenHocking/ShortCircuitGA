/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for element wise operations on arrays of Number objects
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 6, 2010
 */
public final class ArrayNumberUtils {

    public static <T extends Number> double sum(final List<T> numberList) {
        double retval = 0;
        if (!numberList.isEmpty()) {
            for (final Integer i : new IntegerRange(numberList.size())) {
                retval += numberList.get(i).doubleValue();
            }
        }
        return retval;
    }

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

    public static void accum(final List<Double> list1, final List<Double> list2) {
        if (list1.size() != list2.size())
            throw new IllegalArgumentException("Lists being added together must be of the same size.");
        if (!list1.isEmpty()) {
            for (final Integer i : new IntegerRange(list1.size())) {
                list1.set(i, list1.get(i) + list2.get(i));
            }
        }
    }

    public static List<Double> divide(final List<Double> list1, final List<Double> list2) {
        if (list1.size() != list2.size())
            throw new IllegalArgumentException("Lists being added together must be of the same size.");
        final List<Double> retval = new ArrayList<Double>(list1.size());
        if (!list1.isEmpty()) {
            for (final Integer i : new IntegerRange(list1.size())) {
                retval.add(list1.get(i) / list2.get(i));
            }
        }
        return retval;
    }

    public static List<Double> divide(final List<Double> list, final double val) {
        final List<Double> retval = new ArrayList<Double>(list.size());
        if (!list.isEmpty()) {
            for (final Integer i : new IntegerRange(list.size())) {
                retval.add(list.get(i) / val);
            }
        }
        return retval;
    }

    /**
     * 
     * @param values
     * @param first (inclusive)
     * @param last (inclusive)
     * @return
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
     * Special divide is just like divide except that 0/0 becomes 0 instead of NaN
     * @param list1
     * @param list2
     * @return
     */
    public static List<Double> specialDivide(final List<Double> list1, final List<Double> list2) {
        if (list1.size() != list2.size())
            throw new IllegalArgumentException("Lists being added together must be of the same size.");
        final List<Double> retval = new ArrayList<Double>(list1.size());
        if (!list1.isEmpty()) {
            for (final Integer i : new IntegerRange(list1.size())) {
                retval.add(list2.get(i).doubleValue() != 0 ? list1.get(i) / list2.get(i) : Double.valueOf(0));
            }
        }
        return retval;
    }
}
