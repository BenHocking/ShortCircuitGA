/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.HashMap;
import java.util.Set;

/**
 * Mapping between a arbitrary variable (represented by an Object which will typically be either a String or a Pattern) and a pair
 * of {@link edu.virginia.cs.common.utils.ValueGenerator ValueGenerator} (which determines how a value between 0 and 1 gets mapped to
 * another Object) and an Integer which determines which position in a {@link edu.virginia.cs.geneticalgorithm.StandardGenotype
 * StandardGenotye} is used to generate the value.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @param <T> Class type corresponding to the arbitrary variable being mapped
 * @since May 2, 2010
 */
public class GeneInterpreterMap<T> extends HashMap<T, GeneInterpreter> {

    /**
     * @return A {@link java.util.Set Set} of all the variables in this map.
     */
    public Set<T> getAllPatterns() {
        return keySet();
    }

    /**
     * Generates a value based off the GeneInterpreter previously defined, using a {@link StandardGenotype}
     * @param p Variable to get the GeneInterpreter for
     * @param genotype {@link StandardGenotype} to use for generating the value
     * @return Value generated from the specified pattern and {@link StandardGenotype}
     */
    public String generateValue(final T p, final StandardGenotype genotype) {
        final GeneInterpreter interpeter = get(p);
        return interpeter.generate(genotype);
    }

}
