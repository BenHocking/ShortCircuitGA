/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

/**
 * Interface for functor classes selecting {@link Genotype} individuals from a {@link Distribution}
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public interface Select {

    /**
     * Selects a {@link Genotype} individual from a {@link Distribution}
     * @param distribution {@link Distribution} to select from
     * @return Selected {@link Genotype} individual
     */
    public Genotype select(Distribution distribution);
}
