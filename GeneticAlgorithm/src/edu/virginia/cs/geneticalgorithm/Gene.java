/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.Random;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public interface Gene {

    /**
     * @return a Gene from one of the genes in getValues(), dependent on the value of this
     */
    Gene mutate(Random rng);

    /**
     * @param rng Random number generator to use for generating Genes
     * @return a Gene from one of the genes in getValues(), independent of the value of
     * this
     */
    Gene generate(Random rng);
}
