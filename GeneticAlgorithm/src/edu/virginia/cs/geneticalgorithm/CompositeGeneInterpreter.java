/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import edu.virginia.cs.common.utils.MathUtils;

/**
 * GeneInterpeter that depends on other GeneInterpeters to determine its lower and upper bounds
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 28, 2010
 */
public class CompositeGeneInterpreter implements GeneInterpreter {

    private final int _genePos;
    private final GeneInterpreter _lower;
    private final GeneInterpreter _upper;
    private final boolean _integerOnly;

    /**
     * @param genePos Which {@link Gene} in a {@link Genotype} this interpreter corresponds to
     * @param lower {@link GeneInterpreter} that defines this interpreter's lower bound
     * @param upper {@link GeneInterpreter} that defines this interpreter's upper bound
     * @param integerOnly Whether this interpreter should return integer values only or not
     */
    public CompositeGeneInterpreter(final int genePos, final GeneInterpreter lower, final GeneInterpreter upper,
                                    final boolean integerOnly) {
        _genePos = genePos;
        _lower = lower;
        _upper = upper;
        _integerOnly = integerOnly;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.GeneInterpreter#generate(edu.virginia.cs.geneticalgorithm.Genotype)
     */
    @Override
    public String generate(final Genotype genotype) {
        final Gene g = genotype.getGene(_genePos);
        if (!(g instanceof IntervalGene)) throw new IllegalArgumentException("Gene being matched against is not an IntervalGene");
        final IntervalGene ig = (IntervalGene) g;
        if (_integerOnly) {
            final Integer lb = Integer.parseInt(_lower.generate(genotype));
            final Integer ub = Integer.parseInt(_upper.generate(genotype));
            return String.valueOf(MathUtils.scaleInt(lb, ig.getValue(), ub));
        }
        final Double lb = Double.parseDouble(_lower.generate(genotype));
        final Double ub = Double.parseDouble(_upper.generate(genotype));
        return String.valueOf(MathUtils.scale(lb, ig.getValue(), ub));
    }

}
