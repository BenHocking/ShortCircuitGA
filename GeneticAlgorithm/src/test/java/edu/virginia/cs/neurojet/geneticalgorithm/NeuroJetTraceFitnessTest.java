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
import java.io.File;
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
        return (NeuroJetTraceFitness) _factory.createFitness(StandardGenotypeTest.createStandardIntervalGenotype(30,
                                                                                                                 val));
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetTraceFitness#getDesiredAct()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testGetDesiredAct() throws URISyntaxException {
        createFitness();
        assertEquals(3.1, _fitness.getDesiredAct(), 0);
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetTraceFitness#getMePct()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testGetMePct() throws URISyntaxException {
        createFitness();
        assertEquals(0.275, _fitness.getMePct(), 0);
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetTraceFitness#getNumNeurons()}.
     */
    @Test
    public final void testGetNumNeurons() {
        assertEquals(2048, NeuroJetTraceFitness.getNumNeurons());
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetTraceFitness#getMe()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testGetMe() throws URISyntaxException {
        createFitness();
        assertEquals(56, _fitness.getMe());
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetTraceFitness#getPuffRange()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testGetPuffRange() throws URISyntaxException {
        createFitness();
        assertEquals(new IntegerRange(57, 112), _fitness.getPuffRange());
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetTraceFitness#fitnessValues()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    @Ignore
    // FIXME: results vary depending on where it's run from
            public final
            void testFitnessValues() throws URISyntaxException {
        createFitness();
        final List<Double> result = _fitness.fitnessValues();
        assertEquals(1.8326745894124435E-6, result.get(1), 1E-7);
        assertEquals(0.0112558878846454413, result.get(2), 1E-8); // TODO Make test more robust
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetTraceFitness#hasTargetBehavior()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    @Ignore
    // FIXME: results vary depending on where it's run from
            public final
            void testHasTargetBehavior() throws URISyntaxException {
        createFitness();
        assertEquals(0.0, _fitness.hasTargetBehavior(), 0.0);
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetTraceFitness#halt()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testHalt() throws URISyntaxException {
        createFitness();
        _fitness.halt();
        _fitness.halt();
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetTraceFitness#toString()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testToString() throws URISyntaxException {
        createFitness();
        assertTrue(_fitness.toString().contains("data/trace_"));
    }

    private List<Double> getFolderFitnessValues(final File workingDir, final int dirId, final double desiredAct,
                                                final double mePct) {
        final NeuroJetTraceFitness instance = new NeuroJetTraceFitness(workingDir, dirId) {

            @Override
            public double getDesiredAct() {
                return desiredAct;
            }

            @Override
            public double getMePct() {
                return mePct;
            }
        };
        instance.setFinished(true);
        return instance.fitnessValues();
    }

    /**
     * Finds the fitness score for a particular folder
     */
    @Test
    public final void evaluateFolder() {
        int dirId = 3814;
        final File workingDir = new File("/Users/bhocking/Documents/workspace/ShortCircuitGA/scripts");
        double desiredAct = 0.0030998257089591887;
        double mePct = 0.3107368052647797;
        List<Double> fitnessValues = getFolderFitnessValues(workingDir, dirId, desiredAct, mePct);
        // TODO: Check actual values
        System.out.println("===============");
        System.out.println(fitnessValues);
        dirId = 3952;
        desiredAct = 0.0030998257089591887;
        mePct = 0.5;
        fitnessValues = getFolderFitnessValues(workingDir, dirId, desiredAct, mePct);
        System.out.println("===============");
        System.out.println(fitnessValues);
        System.out.println("===============");
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetTraceFitness#totalFitness()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    @Ignore
    // FIXME: results vary depending on where it's run from
            public final
            void testTotalFitness() throws URISyntaxException {
        createFitness();
        assertEquals(602.5112858084858, _fitness.totalFitness(), 1E-5); // TODO Make test more robust
        Fitness f = buildFitness(0.0);
        assertEquals(275.1024912636019, f.totalFitness(), 1E-5);
        f = buildFitness(1.0);
        // FIXME assertEquals(2.511775, f.totalFitness(), 1E-5);
    }

}
