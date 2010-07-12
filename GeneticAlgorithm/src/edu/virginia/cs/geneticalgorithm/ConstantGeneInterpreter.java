/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

/**
 * GeneInterpreter that always returns the same value, regardless of the genotype passed to it.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 28, 2010
 */
public class ConstantGeneInterpreter implements GeneInterpreter {

    private final String _constant;

    /**
     * Constructor taking a {@link java.lang.String String} argument, which might or might not be parsable as a number.
     * @param constant Constant value to return by {@link #generate(Genotype)}, which might or might not be parsable as a number.
     */
    public ConstantGeneInterpreter(final String constant) {
        _constant = constant;
    }

    /**
     * Constructor taking a {@link java.lang.Number Number} argument
     * @param constant Constant value to return by {@link #generate(Genotype)}
     */
    public ConstantGeneInterpreter(final Number constant) {
        _constant = constant.toString();
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.GeneInterpreter#generate(edu.virginia.cs.geneticalgorithm.Genotype)
     */
    @Override
    public String generate(final Genotype genotype) {
        return _constant;
    }
}
