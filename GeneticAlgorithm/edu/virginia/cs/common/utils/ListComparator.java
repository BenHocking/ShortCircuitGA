/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.util.Comparator;
import java.util.List;

/**
 * Comparator useful for sorting lists of lists by a particular element in the list
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @param <T> Type of object in the List
 * @since Jun 5, 2011
 */
public class ListComparator<T extends Comparable<T>> implements Comparator<List<T>> {

    private final int _sortPlace;

    /**
     * @param sortPlace Place to compare lists by
     */
    public ListComparator(final int sortPlace) {
        _sortPlace = sortPlace;
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final List<T> o1, final List<T> o2) {
        return o1.get(_sortPlace).compareTo(o2.get(_sortPlace));
    }

}
