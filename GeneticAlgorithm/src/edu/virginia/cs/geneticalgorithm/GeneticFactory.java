/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.List;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public interface GeneticFactory {

    public Select getSelectFunction();

    public Crossover getCrossoverFunction();

    public List<Genotype> createPopulation(final int numIndividuals, final int genotypeLength);
}
