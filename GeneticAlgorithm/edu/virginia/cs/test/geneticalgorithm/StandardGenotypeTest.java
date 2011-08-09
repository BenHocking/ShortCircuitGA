/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.geneticalgorithm;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.gene.IntervalGene;
import edu.virginia.cs.geneticalgorithm.gene.StandardGene;
import edu.virginia.cs.geneticalgorithm.gene.StandardGenotype;

/**
 * Test harness for StandardGenotype
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class StandardGenotypeTest {

    /**
     * @param size Number of genes in the genotype
     * @return StandardGenotype containing standard genes
     */
    public static StandardGenotype createStandardGenotype(final int size) {
        final Random rng = new Random();
        return new StandardGenotype(size, StandardGene.ONE, rng);
    }

    /**
     * @param size Number of genes in the genotype
     * @param initialValue Initial value to have the interval set to
     * @return StandardGenotype containing interval genes
     */
    public static StandardGenotype createStandardIntervalGenotype(final int size, final double initialValue) {
        final Random rng = new Random();
        return new StandardGenotype(size, new IntervalGene(initialValue), rng);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.gene.StandardGenotype#hashCode()}.
     */
    @Test
    public final void testHashCode() {
        final StandardGenotype g = createStandardIntervalGenotype(5, 0.5);
        final StandardGenotype g2 = createStandardIntervalGenotype(6, 0.5);
        final StandardGenotype g3 = createStandardIntervalGenotype(5, 0.6);
        assertNotSame(g.hashCode(), g2.hashCode());
        assertNotSame(g.hashCode(), g3.hashCode());
        assertNotSame(g3.hashCode(), g2.hashCode());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.gene.StandardGenotype#StandardGenotype()}.
     */
    @Test
    public final void testStandardGenotype() {
        assertEquals(0, new StandardGenotype().size());
        final StandardGenotype g = createStandardIntervalGenotype(5, 0.5);
        assertEquals(g, new StandardGenotype(g));
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.gene.StandardGenotype#setGene(int, edu.virginia.cs.geneticalgorithm.Gene)}
     * .
     */
    @Test
    public final void testSetGene() {
        final StandardGenotype g = createStandardIntervalGenotype(5, 0.5);
        g.setGene(0, StandardGene.ONE);
        assertEquals(StandardGene.ONE, g.getGene(0));
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.gene.StandardGenotype#getGene(int)}.
     */
    @Test
    public final void testGetGene() {
        final StandardGenotype g = createStandardIntervalGenotype(5, 0.5);
        g.setGene(0, StandardGene.ONE);
        assertEquals(StandardGene.ONE, g.getGene(0));
        assertEquals(new IntervalGene(0.5), g.getGene(1));
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.gene.StandardGenotype#getNumGenes()}.
     */
    @Test
    public final void testGetNumGenes() {
        final StandardGenotype g = createStandardIntervalGenotype(5, 0.5);
        assertEquals(5, g.getNumGenes());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.gene.StandardGenotype#clone()}.
     */
    @Test
    public final void testClone() {
        final StandardGenotype g = createStandardIntervalGenotype(5, 0.5);
        final StandardGenotype clone = (StandardGenotype) g.clone();
        g.setGene(0, StandardGene.ONE);
        clone.setGene(0, StandardGene.ZERO);
        assertEquals(StandardGene.ONE, g.getGene(0));
        assertEquals(StandardGene.ZERO, clone.getGene(0));
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.gene.StandardGenotype#equals(java.lang.Object)}.
     */
    @Test
    public final void testEqualsObject() {
        final StandardGenotype gI = createStandardIntervalGenotype(5, 0.5);
        assertEquals(false, gI.equals(null));
        assertEquals(gI, gI);
        final StandardGenotype g = createStandardGenotype(5);
        assertEquals(false, gI.equals(g));
        assertEquals(g, g);
        assertEquals(false, gI.equals(StandardGene.ONE));
        assertEquals(false, StandardGene.ZERO.equals(null));
        assertEquals(false, StandardGene.ZERO.equals(new IntervalGene(0.5)));
        assertEquals(false, StandardGene.ZERO.equals(StandardGene.ONE));
        assertEquals(true, StandardGene.ZERO.equals(StandardGene.ZERO));
        final IntervalGene ig = new IntervalGene(0.5);
        assertEquals(false, ig.equals(null));
        assertEquals(false, ig.equals(StandardGene.ZERO));
        assertEquals(false, ig.equals(new IntervalGene(0.6)));
        assertEquals(true, ig.equals(new IntervalGene(0.5)));
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.gene.StandardGenotype#compareTo(edu.virginia.cs.geneticalgorithm.Genotype)}.
     */
    @Test
    public final void testCompareTo() {
        final StandardGenotype gI = createStandardIntervalGenotype(5, 0.5);
        final StandardGenotype g = createStandardGenotype(5);
        assertEquals(0, gI.compareTo(gI));
        assertEquals(0, g.compareTo(g));
        try {
            assertEquals(0, g.compareTo(gI));
            fail("Cannot compare genotypes of different type genes");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Genes must be of the same type", e.getMessage());
        }
        try {
            assertEquals(0, g.compareTo(null));
            fail("Cannot compare genotypes to null");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Genotypes must be of the same type", e.getMessage());
        }
        final IntervalGene ig = new IntervalGene(0.5);
        try {
            ig.compareTo(null);
            fail("Cannot compare to null");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Genes must be of the same type", e.getMessage());
        }
    }
}
