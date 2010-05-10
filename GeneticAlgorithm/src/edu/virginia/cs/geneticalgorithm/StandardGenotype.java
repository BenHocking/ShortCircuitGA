/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.ArrayList;

import java.util.Random;
import static edu.virginia.cs.common.HashUtils.*;
import static edu.virginia.cs.common.EqualUtils.*;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public final class StandardGenotype extends ArrayList<Gene> implements Genotype {

    public StandardGenotype(final int numGenes, final Gene generator, final Random rng) {
        for (int i = 0; i < numGenes; ++i) {
            add(generator.generate(rng));
        }
    }

    /**
     * 
     */
    public StandardGenotype() {
        super();
    }

    /**
     * @param parameterizedGenotype
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

}
