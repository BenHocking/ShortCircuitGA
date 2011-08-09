/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.gene.interpreter;

import edu.virginia.cs.geneticalgorithm.gene.Gene;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;
import edu.virginia.cs.geneticalgorithm.gene.IntervalGene;

/**
 * GeneInterpreter that always returns the same value, regardless of the genotype passed to it.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 28, 2010
 */
public class ConstantGeneInterpreter implements GeneInterpreter {

    private final String _constant;
    private Gene _defaultGene = new IntervalGene(0.5);

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
        this(constant.toString());
    }

    /**
     * Sets the gene to return when invert is invoked
     * @param g Gene to return when invert is invoked
     * @see ConstantGeneInterpreter#invert(String, Genotype)
     */
    public void setDefaultGene(final Gene g) {
        _defaultGene = g;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.gene.interpreter.GeneInterpreter#generate(edu.virginia.cs.geneticalgorithm.gene.Genotype)
     */
    @Override
    public String generate(final Genotype genotype) {
        return _constant;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.gene.interpreter.GeneInterpreter#invert(java.lang.String, Genotype)
     */
    @Override
    public Gene invert(final String string, final Genotype genotype) {
        return _defaultGene;
    }
}
