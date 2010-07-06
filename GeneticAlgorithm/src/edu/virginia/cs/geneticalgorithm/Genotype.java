/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public interface Genotype extends Iterable<Gene>, Comparable<Genotype> {

    /**
     * @param i
     * @param g
     */
    public void setGene(final int i, final Gene g);

    /**
     * @param i
     * @return
     */
    public Gene getGene(final int i);

    /**
     * @return
     */
    public int getNumGenes();

    /**
     * @return
     */
    public Genotype clone();
}
