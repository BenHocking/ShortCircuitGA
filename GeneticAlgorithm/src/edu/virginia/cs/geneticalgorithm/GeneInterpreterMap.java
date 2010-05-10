/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

import edu.virginia.cs.common.ValueGenerator;
import edu.virginia.cs.common.OrderedPair;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 2, 2010
 */
public class GeneInterpreterMap extends HashMap<Pattern, OrderedPair<ValueGenerator, Integer>> {

    // Maps a pattern to a number generator and its position in a genome
    public void put(final Pattern p, final ValueGenerator g, final Integer pos) {
        put(p, new OrderedPair<ValueGenerator, Integer>(g, pos));
    }

    public Set<Pattern> getAllPatterns() {
        return keySet();
    }

    public String generateValue(final Pattern p, final StandardGenotype genotype) {
        final OrderedPair<ValueGenerator, Integer> pair = get(p);
        final int pos = pair.getLast();
        final Gene g = genotype.get(pos);
        if (!(g instanceof IntervalGene)) throw new IllegalArgumentException("Gene being matched against is not an IntervalGene");
        final IntervalGene ig = (IntervalGene) g;
        return pair.getFirst().generate(ig.getValue()).toString();
    }

}
