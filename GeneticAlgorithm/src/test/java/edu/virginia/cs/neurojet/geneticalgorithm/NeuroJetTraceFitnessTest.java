/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

import edu.virginia.cs.common.utils.IntegerRange;
import edu.virginia.cs.geneticalgorithm.fitness.Fitness;
import edu.virginia.cs.geneticalgorithm.gene.StandardGenotypeTest;
import org.junit.Ignore;

/**
 * Test harness for NeuroJetTraceFitness
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 22, 2011
 */
public class NeuroJetTraceFitnessTest {

    private static NeuroJetTraceFitness _fitness = null;
    private static NeuroJetTraceFitnessFactory _factory = null;
    private final static Object _factoryLock = new Object();

    private static void createFitness() throws URISyntaxException {
        if (_fitness == null) _fitness = buildFitness(0.5);
    }

    /**
     * @param val Value to assign to all IntervalGene objects
     * @return NeuroJetTraceFitness suitable for testing
     * @throws URISyntaxException Shouldn't happen
     */
    public static NeuroJetTraceFitness buildFitness(final double val) throws URISyntaxException {
        synchronized (_factoryLock) {
            if (_factory == null) {
                _factory = NeuroJetTraceFitnessFactoryTest.createNeuroJetTraceFitness();
            }
        }
        return (NeuroJetTraceFitness) _factory.createFitness(StandardGenotypeTest.createStandardIntervalGenotype(30, val));
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness#getDesiredAct()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testGetDesiredAct() throws URISyntaxException {
        createFitness();
        assertEquals(3.0, _fitness.getDesiredAct(), 0);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness#getMePct()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testGetMePct() throws URISyntaxException {
        createFitness();
        assertEquals(0.35, _fitness.getMePct(), 0);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness#getNumNeurons()}.
     */
    @Test
    public final void testGetNumNeurons() {
        assertEquals(2048, NeuroJetTraceFitness.getNumNeurons());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness#getMe()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testGetMe() throws URISyntaxException {
        createFitness();
        assertEquals(72, _fitness.getMe());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness#getPuffRange()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testGetPuffRange() throws URISyntaxException {
        createFitness();
        assertEquals(new IntegerRange(73, 144), _fitness.getPuffRange());
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness#fitnessValues()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    @Ignore // FIXME: results vary depending on where it's run from
    public final void testFitnessValues() throws URISyntaxException {
        createFitness();
        final List<Double> result = _fitness.fitnessValues();
        assertEquals(1.8326745894124435E-6, result.get(1), 1E-7);
        assertEquals(0.0112558878846454413, result.get(2), 1E-8); // TODO Make test more robust
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness#hasTargetBehavior()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    @Ignore // FIXME: results vary depending on where it's run from
    public final void testHasTargetBehavior() throws URISyntaxException {
        createFitness();
        assertEquals(0.0, _fitness.hasTargetBehavior(), 0.0);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness#halt()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testHalt() throws URISyntaxException {
        createFitness();
        _fitness.halt();
        _fitness.halt();
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness#toString()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testToString() throws URISyntaxException {
        createFitness();
        assertTrue(_fitness.toString().contains("data/trace_"));
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness#totalFitness()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    @Ignore // FIXME: results vary depending on where it's run from
    public final void testTotalFitness() throws URISyntaxException {
        createFitness();
        assertEquals(602.5112858084858, _fitness.totalFitness(), 1E-5); // TODO Make test more robust
        Fitness f = buildFitness(0.0);
        assertEquals(275.1024912636019, f.totalFitness(), 1E-5);
        f = buildFitness(1.0);
//FIXME        assertEquals(2.511775, f.totalFitness(), 1E-5);
    }

}
