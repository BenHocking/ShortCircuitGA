/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
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
}
