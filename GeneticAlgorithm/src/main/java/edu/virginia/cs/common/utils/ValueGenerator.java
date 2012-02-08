/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

/**
 * Interface for classes that convert a value between 0 and 1 to some other value
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 2, 2010
 */
public interface ValueGenerator {

    /**
     * Generates a String representation of an object based off the input parameter.
     * @param x Value between 0 and 1 to use for generating the return value
     * @return String representation of the object returned
     */
    public String generate(double x);

    /**
     * Returns a double capable of generating the requested String
     * @param s String to invert
     * @return double capable of generating the requested String
     */
    public double invert(String s);
}
