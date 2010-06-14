/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import edu.virginia.cs.common.ValueGenerator;

/**
 * GeneInterpeter that uses a ValueGenerator and a gene position to generate a value
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 28, 2010
 */
public final class SimpleGeneInterpreter implements GeneInterpreter {

    private final int _genePos;
    private final ValueGenerator _generator;

    public SimpleGeneInterpreter(final int genePos, final ValueGenerator vg) {
        _genePos = genePos;
        _generator = vg;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.GeneInterpreter#generate(edu.virginia.cs.geneticalgorithm.Genotype)
     */
    @Override
    public String generate(final Genotype genotype) {
        final Gene g = genotype.getGene(_genePos);
        if (!(g instanceof IntervalGene)) throw new IllegalArgumentException("Gene being matched against is not an IntervalGene");
        final IntervalGene ig = (IntervalGene) g;
        return _generator.generate(ig.getValue());
    }

}
