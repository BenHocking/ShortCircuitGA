/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

/**
 * Interface for classes able to get a String value from a Genotype
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 28, 2010
 */
public interface GeneInterpreter {

    /**
     * @param genotype Individual {@link Genotype} to generate a value for
     * @return Value corresponding to the requested {@link Genotype}
     */
    public String generate(final Genotype genotype);

    /**
     * Generates a {@link Gene} capable of generating the requested value
     * @param string Value to base Gene on
     * @param genotype Genotype to use to decode (used by complicated GeneInterpreters)
     * @return Generated Gene
     */
    public Gene invert(final String string, final Genotype genotype);
}
