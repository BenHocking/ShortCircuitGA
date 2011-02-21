/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.geneticalgorithm;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.DistributionMember;
import edu.virginia.cs.geneticalgorithm.Genotype;

/**
 * Test harness for DistributionMember
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class DistributionMemberTest {

    /**
     * Sets up common objects
     * @param fitness Fitness to assign to the distribution member
     * @return generic DistributionMember
     */
    public static DistributionMember createDistributionMember(final Double fitness) {
        return createDistributionMember(fitness, 0.5);
    }

    /**
     * Sets up common objects
     * @param fitness Fitness to assign to the distribution member
     * @param val Value to assign to genes in the distribution member's genotype
     * @return generic DistributionMember
     */
    public static DistributionMember createDistributionMember(final Double fitness, final double val) {
        final Genotype g = StandardGenotypeTest.createStandardIntervalGenotype(5, val);
        return new DistributionMember(fitness, Collections.singletonList(fitness), g);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.DistributionMember#toString()}.
     */
    @Test
    public final void testToString() {
        DistributionMember dm = createDistributionMember(0.5);
        assertEquals("Fitness: 0.5, [0.5], " + dm.getGenotype(), dm.toString());
        final Genotype g = StandardGenotypeTest.createStandardIntervalGenotype(5, 0.5);
        dm = new DistributionMember(0.5, Collections.unmodifiableList(Collections.nCopies(3, Double.valueOf(0.5))), g);
        assertEquals("Fitness: 0.5, [0.5, 0.5, 0.5], " + dm.getGenotype(), dm.toString());
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.DistributionMember#DistributionMember(edu.virginia.cs.geneticalgorithm.DistributionMember)}
     * .
     */
    @Test
    public final void testDistributionMemberDistributionMember() {
        final DistributionMember dm = createDistributionMember(0.5);
        final DistributionMember dmc = new DistributionMember(dm);
        assertEquals(dm.getValue(), dmc.getValue());
        assertEquals(dm.getFitnessValues(), dmc.getFitnessValues());
        assertEquals(dm.getGenotype(), dmc.getGenotype());
    }

    /**
     * @param dm1 First distribution member to compare
     * @param dm2 Second distribution member to compare
     * @return Whether they describe the same distribution
     */
    public static boolean membersHaveSameValues(final DistributionMember dm1, final DistributionMember dm2) {
        return dm1.getValue().equals(dm2.getValue()) && dm1.getFitnessValues().equals(dm2.getFitnessValues())
               && dm1.getGenotype().equals(dm2.getGenotype());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.DistributionMember#getValue()}.
     */
    @Test
    public final void testGetValue() {
        final DistributionMember dm = createDistributionMember(0.5);
        assertEquals(0.5, dm.getValue());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.DistributionMember#getFitnessValues()}.
     */
    @Test
    public final void testGetFitnessValues() {
        final DistributionMember dm = createDistributionMember(0.5);
        assertEquals(Collections.singletonList(0.5), dm.getFitnessValues());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.DistributionMember#getGenotype()}.
     */
    @Test
    public final void testGetGenotype() {
        final DistributionMember dm = createDistributionMember(0.5);
        final Genotype expected = StandardGenotypeTest.createStandardIntervalGenotype(5, 0.5);
        assertEquals(expected, dm.getGenotype());
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.DistributionMember#compareTo(edu.virginia.cs.geneticalgorithm.DistributionMember)}.
     */
    @Test
    public final void testCompareTo() {
        final DistributionMember dm = createDistributionMember(0.5);
        final DistributionMember dm2 = createDistributionMember(0.6);
        assertTrue("0.5 == 0.5", dm.compareTo(dm) == 0);
        // This ordering might seem backwards, but we want largest values first
        assertTrue("0.5 > 0.6", dm.compareTo(dm2) > 0);
        assertTrue("0.6 < 0.5", dm2.compareTo(dm) < 0);
        final Genotype gi = StandardGenotypeTest.createStandardIntervalGenotype(5, 0.5);
        final DistributionMember dm3 = new DistributionMember(0.5, Collections.singletonList(0.6), gi);
        assertTrue("0.5 > 0.6", dm.compareTo(dm3) > 0);
        assertTrue("0.6 < 0.5", dm3.compareTo(dm) < 0);
        final Genotype gi2 = StandardGenotypeTest.createStandardIntervalGenotype(4, 0.5);
        final DistributionMember dm4 = new DistributionMember(0.5, Collections.singletonList(0.5), gi2);
        assertTrue("dm > dm4", dm.compareTo(dm4) > 0);
        assertTrue("dm4 < dm", dm4.compareTo(dm) < 0);
    }

}
