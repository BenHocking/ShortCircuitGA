/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import edu.virginia.cs.common.UnorderedPair;

/**
 * Analogous to crossovers in chromosomes
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public interface Crossover {

    /**
     * Interface method for crossing two {@link Genotype Genotypes} to create two children.
     * @param mother First {@link Genotype} parent
     * @param father Second {@link Genotype} parent
     * @return Children {@link Genotype Genotypes} as a result of the crossover.
     */
    UnorderedPair<Genotype> crossover(Genotype mother, Genotype father);
}
