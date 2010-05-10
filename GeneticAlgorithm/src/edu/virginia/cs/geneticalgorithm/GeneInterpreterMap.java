/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.HashMap;
import java.util.Set;

import edu.virginia.cs.common.ValueGenerator;
import edu.virginia.cs.common.OrderedPair;

/**
 * Mapping between a arbitrary variable (represented by an Object which will typically be either a String or a Pattern) and a pair
 * of {@link edu.virginia.cs.common.ValueGenerator ValueGenerator} (which determines how a value between 0 and 1 gets mapped to
 * another Object) and an Integer which determines which position in a {@link edu.virginia.cs.geneticalgorithm.StandardGenotype
 * StandardGenotye} is used to generate the value.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 2, 2010
 */
public class GeneInterpreterMap<T> extends HashMap<T, OrderedPair<ValueGenerator, Integer>> {

    // Maps a pattern to a number generator and its position in a genome
    public void put(final T p, final ValueGenerator g, final Integer pos) {
        put(p, new OrderedPair<ValueGenerator, Integer>(g, pos));
    }

    public Set<T> getAllPatterns() {
        return keySet();
    }

    public String generateValue(final T p, final StandardGenotype genotype) {
        final OrderedPair<ValueGenerator, Integer> pair = get(p);
        final int pos = pair.getLast();
        final Gene g = genotype.get(pos);
        if (!(g instanceof IntervalGene)) throw new IllegalArgumentException("Gene being matched against is not an IntervalGene");
        final IntervalGene ig = (IntervalGene) g;
        return pair.getFirst().generate(ig.getValue()).toString();
    }

}
