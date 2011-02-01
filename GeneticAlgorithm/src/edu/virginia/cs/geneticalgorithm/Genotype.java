/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

/**
 * A group of {@link Gene Genes}
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public interface Genotype extends Iterable<Gene>, Comparable<Genotype> {

    /**
     * @param i Position of {@link Gene} in the Genotype
     * @param g {@link Gene} to set at position i
     */
    public void setGene(final int i, final Gene g);

    /**
     * @param i Position of {@link Gene} in the Genotype
     * @return g {@link Gene} at position i
     */
    public Gene getGene(final int i);

    /**
     * @return Number of {@link Gene Genes} in the Genotype
     */
    public int getNumGenes();

    /**
     * @return Deep copy of this Genotype. Maps of Genotypes especially rely on this so that their underlying Genotypes won't be
     * changed from underneath them.
     */
    public Genotype clone();
}
