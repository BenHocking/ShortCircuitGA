/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.List;

/**
 * Interface for factory classes providing {@link Select} and {@link Crossover} functor classes, and capable of generating a
 * population of {@link Genotype Genotypes}.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public interface GeneticFactory {

    /**
     * @return {@link Select} functor class
     */
    public Select getSelectFunction();

    /**
     * @return {@link Crossover} functor class
     */
    public Crossover getCrossoverFunction();

    /**
     * @param numIndividuals Number of individuals in the population
     * @param genotypeLength Number of {@link Gene Genes} in each {@link Genotype}
     * @return Population of {@link Genotype Genotypes}
     */
    public List<Genotype> createPopulation(final int numIndividuals, final int genotypeLength);

    /**
     * @param mutator Mutator to use when reproducing
     */
    public void setMutator(Mutator mutator);
}
