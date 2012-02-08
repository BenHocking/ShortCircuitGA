/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.mutator;

import edu.virginia.cs.geneticalgorithm.gene.Genotype;

/**
 * Interface for mutating functor classes
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public interface Mutator {

    /**
     * Mutates a {@link Genotype}
     * @param toMutate {@link Genotype} to mutate
     * @return Mutated {@link Genotype}
     */
    public Genotype mutate(Genotype toMutate);
}
