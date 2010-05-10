/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

import java.util.List;

/**
 * {@link edu.virginia.cs.common.ValueGenerator ValueGenerator} that chooses a value from a list of objects.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 2, 2010
 */
public class ListValueGenerator implements ValueGenerator {

    private final List<Object> _list;

    /**
     * Constructor taking list of all possible return values. The order of these items determines how the x parameter to the
     * {@link #generate(double)} method will be interpreted.
     * @param list List of all possible return values.
     */
    public ListValueGenerator(final List<Object> list) {
        _list = list;
    }

    /**
     * @see edu.virginia.cs.common.ValueGenerator#generate(double)
     */
    @Override
    public String generate(final double x) {
        final int whichItem = MathUtils.scaleInt(0, x, _list.size() - 1);
        return _list.get(whichItem).toString();
    }

}
