/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.ArrayList;

import java.util.Random;

import edu.virginia.cs.common.utils.ArrayGenericUtils;
import static edu.virginia.cs.common.utils.EqualUtils.*;
import static edu.virginia.cs.common.utils.HashUtils.*;

/**
 * {@link Genotype} that is just a {@link java.util.List List} of {@link Gene Genes}.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public final class StandardGenotype extends ArrayList<Gene> implements Genotype {

    /**
     * Constructor
     * @param numGenes Number of {@link Gene Genes} in the genotype.
     * @param generator {@link Gene} used to generate more of its same {@link java.lang.Class Class}
     * @param rng Random number generator used for generating initial {@link Gene Genes}
     */
    public StandardGenotype(final int numGenes, final Gene generator, final Random rng) {
        for (int i = 0; i < numGenes; ++i) {
            add(generator.generate(rng));
        }
    }

    /**
     * Empty genotype constructor
     */
    public StandardGenotype() {
        super();
    }

    /**
     * Copy constructor
     * @param c StandardGenotype to copy
     */
    public StandardGenotype(final StandardGenotype c) {
        super(c);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Genotype#setGene(int, edu.virginia.cs.geneticalgorithm.Gene)
     */
    @Override
    public void setGene(final int i, final Gene g) {
        set(i, g);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Genotype#getGene(int)
     */
    @Override
    public Gene getGene(final int i) {
        return get(i);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Genotype#getNumGenes()
     */
    @Override
    public int getNumGenes() {
        return size();
    }

    /**
     * @see java.util.ArrayList#clone()
     */
    @Override
    public Genotype clone() {
        return new StandardGenotype(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o.getClass().equals(getClass()))) return false;
        return eq(this, o);
    }

    @Override
    public int hashCode() {
        return hash(SEED, this);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Genotype#compareTo(edu.virginia.cs.geneticalgorithm.Genotype)
     */
    @Override
    public int compareTo(final Genotype g) {
        if (!(g instanceof StandardGenotype)) throw new IllegalArgumentException("Genotypes must be of the same type");
        final StandardGenotype sg = (StandardGenotype) g;
        return ArrayGenericUtils.compare(this, sg);
    }

}
