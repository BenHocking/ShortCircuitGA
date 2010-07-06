/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import edu.virginia.cs.common.UnorderedPair;

/**
 * Analagous to crossovers in chromosomes
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 24, 2010
 */
public interface Crossover {

    UnorderedPair<Genotype> crossover(Genotype mother, Genotype father);
}
