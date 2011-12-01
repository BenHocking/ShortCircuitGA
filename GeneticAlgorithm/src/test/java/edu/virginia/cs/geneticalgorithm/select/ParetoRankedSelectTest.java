/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */

package edu.virginia.cs.geneticalgorithm.select;

import edu.virginia.cs.geneticalgorithm.gene.StandardGenotypeTest;
import edu.virginia.cs.geneticalgorithm.distribution.DistributionTest;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import edu.virginia.cs.geneticalgorithm.distribution.Distribution;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test harness for ParetoRankedSelect
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 */
public class ParetoRankedSelectTest {

    @Test
    public void testConstructors() {
        Random rng = new Random();
        ParetoRankedSelect instance = new ParetoRankedSelect(rng);
        assertTrue(instance._rankWeighting.isEmpty());
        List<Double> weights = Arrays.asList(0.5, 2.0);
        instance = new ParetoRankedSelect(rng, weights);
        assertEquals(weights, instance._rankWeighting);
    }

    /**
     * Test of select method, of class ParetoRankedSelect.
     */
    @Test
    public void testSelect() {
        Random rng = new Random();
        Distribution distribution = DistributionTest.createDistribution();
        ParetoRankedSelect instance = new ParetoRankedSelect(rng);
        final Genotype expResult = StandardGenotypeTest.createStandardIntervalGenotype(5, 0.5);
        Genotype result = instance.select(distribution);
        assertEquals(expResult, result);
    }

}