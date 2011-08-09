/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.gene;

import java.util.Random;


/**
 * Interface for classes representing genes in a {@link Genotype}
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public interface Gene extends Comparable<Gene> {

    /**
     * @param rng Random number generator used to perform the next mutation
     * @return a Gene from one of the genes in getValues(), dependent on the value of this
     */
    Gene mutate(Random rng);

    /**
     * @param rng Random number generator to use for generating Genes
     * @return a Gene from one of the genes in getValues(), independent of the value of this
     */
    Gene generate(Random rng);
}
