/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.virginia.cs.geneticalgorithm.distribution.Distribution;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;

/**
 * Utilities useful for Select classes
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Sep 11, 2011
 */
public class SelectUtils {

    /**
     * @param distribution Distribution to enumerate fitness values on
     * @return Distribution with initial column set to index value
     */
    public static List<List<Double>> enumerateFitnessValues(final Distribution distribution) {
        final List<List<Double>> enumeratedFitnessValues = new ArrayList<List<Double>>();
        for (int i = 0; i < distribution.size(); ++i) {
            final List<Double> toAdd = new ArrayList<Double>();
            toAdd.add(Double.valueOf(i));
            toAdd.addAll(distribution.get(i).getFitnessValues());
            enumeratedFitnessValues.add(toAdd);
        }
        return enumeratedFitnessValues;
    }

    /**
     * @param rng Random number generator
     * @param distribution Distribution to choose individual from
     * @param fitnessValues Substituted fitness values to use for selection
     * @return Selected individual
     */
    public static Genotype substituteSelect(final Random rng, final Distribution distribution, final List<Double> fitnessValues) {
        final double selector = rng.nextDouble();
        double totalProb = 0;
        for (int i = 0; i < distribution.size(); ++i) {
            totalProb += fitnessValues.get(i);
            if (totalProb > selector) return distribution.get(i).getLast();
        }
        // Default to last one in case of rounding error
        return distribution.getLast().getGenotype();
    }

}
