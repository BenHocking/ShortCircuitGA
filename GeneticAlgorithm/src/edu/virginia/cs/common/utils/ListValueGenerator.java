/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.util.List;

/**
 * {@link edu.virginia.cs.common.utils.ValueGenerator ValueGenerator} that chooses a value from a list of objects.
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 2, 2010
 */
public final class ListValueGenerator implements ValueGenerator {

    private final List<Object> _list;

    /**
     * Constructor taking a {@link java.util.List List} of all possible return values. The order of these items determines how the x
     * parameter to the {@link #generate(double)} method will be interpreted.
     * @param list {@link java.util.List List} of all possible return values.
     */
    public ListValueGenerator(final List<Object> list) {
        _list = list;
    }

    /**
     * @see edu.virginia.cs.common.utils.ValueGenerator#generate(double)
     */
    @Override
    public String generate(final double x) {
        final int whichItem = MathUtils.scaleInt(0, x, _list.size() - 1);
        return _list.get(whichItem).toString();
    }

    /**
     * @see edu.virginia.cs.common.utils.ValueGenerator#invert(java.lang.String)
     */
    @Override
    public double invert(final String s) {
        for (int i = 0; i < _list.size(); ++i) {
            if (_list.get(i).toString().equals(s)) {
                return MathUtils.scaleIntInverse(0, i, _list.size());
            }
        }
        throw new IllegalArgumentException("'" + s + "' is not a valid value");
    }

}
