/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.fitness.Fitness;
import edu.virginia.cs.data.FileLoader;
import edu.virginia.cs.geneticalgorithm.gene.StandardGenotypeTest;

/**
 * Test harness for NeuroJetTraceFitnessFactory
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 22, 2011
 */
public class NeuroJetTraceFitnessFactoryTest {

    private class MockFile extends File {
        private final boolean _canExecute;
        private final boolean _exists;
        MockFile(String fileName, boolean canExecute, boolean exists) {
            super(fileName);
            _canExecute = canExecute;
            _exists = exists;
        }
        @Override
        public boolean canExecute() {
            return _canExecute;
        }
        @Override
        public boolean exists() {
            return _exists;
        }
    }

    /**
     * @return NeuroJet executable
     * @throws URISyntaxException Shouldn't happen
     */
    public static File getNeuroJet() throws URISyntaxException {
        return FileLoader.getFile("NeuroJet");
    }

    /**
     * @return NeuroJetTraceFitness suitable for testing
     * @throws URISyntaxException Shouldn't happen
     */
    public static NeuroJetTraceFitnessFactory createNeuroJetTraceFitness() throws URISyntaxException {
        return createNeuroJetTraceFitness(getNeuroJet(), null);
    }

    /**
     * @param NeuroJet NeuroJet executable
     * @param prepareScript Executable to run prior to launching NeuroJet instances (null to skip that step)
     * @return NeuroJetTraceFitness suitable for testing
     * @throws URISyntaxException Shouldn't happen
     */
    public static NeuroJetTraceFitnessFactory createNeuroJetTraceFitness(File NeuroJet, File prepareScript) throws URISyntaxException {
        final File scriptFile = FileLoader.getFile("trace_full.nj");
        final List<File> scriptFiles = Collections.singletonList(scriptFile);
        return prepareScript == null ?
            new NeuroJetTraceFitnessFactory(scriptFiles, NeuroJetGeneticAlgorithm.buildScriptUpdater(), NeuroJet,
                                               FileLoader.getDataDirectory()) :
            new NeuroJetTraceFitnessFactory(scriptFiles, NeuroJetGeneticAlgorithm.buildScriptUpdater(), NeuroJet,
                                               FileLoader.getDataDirectory(), prepareScript);
    }

    /**
     * Test method for constructors
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public final void testConstructors() throws URISyntaxException {
        try {
            new NeuroJetTraceFitnessFactory(null, null, null, null, null);
            fail("Null script files");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Argument scriptFiles cannot be null or empty", e.getMessage());
        }
        try {
            new NeuroJetTraceFitnessFactory(new ArrayList<File>(), null, null, null, null);
            fail("No script files");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Argument scriptFiles cannot be null or empty", e.getMessage());
        }
        final File scriptFile = FileLoader.getFile("trace_full.nj");
        final List<File> scriptFiles = Collections.singletonList(scriptFile);
        try {
            new NeuroJetTraceFitnessFactory(scriptFiles, null, null, null, null);
            fail("No NeuroJet");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Argument neuroJet must refer to an executable", e.getMessage());
        }
        try {
            new NeuroJetTraceFitnessFactory(scriptFiles, null, new MockFile("dne", true, false), null, null);
            fail("No NeuroJet");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Argument neuroJet must refer to an executable", e.getMessage());
        }
        try {
            new NeuroJetTraceFitnessFactory(scriptFiles, null, new MockFile("dne", false, true), null, null);
            fail("No NeuroJet");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Argument neuroJet must refer to an executable", e.getMessage());
        }
        new NeuroJetTraceFitnessFactory(scriptFiles, new ScriptUpdater(), getNeuroJet());
        new NeuroJetTraceFitnessFactory(scriptFiles, new ScriptUpdater(), getNeuroJet(), FileLoader.getDataDirectory());
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitnessFactory#createFitness(edu.virginia.cs.geneticalgorithm.Genotype)}
     * .
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testCreateFitness() throws URISyntaxException {
        final NeuroJetTraceFitnessFactory factory = createNeuroJetTraceFitness();
        try {
            factory.createFitness(null);
            fail("Need a genotype");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("individual must be of type StandardGenotype", e.getMessage());
        }
        try {
            factory.createFitness(StandardGenotypeTest.createStandardGenotype(30));
            fail("Shouldn't work with a Standard 0/1 gene");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Gene being matched against is not an IntervalGene", e.getMessage());
        }
        final Fitness f = factory.createFitness(StandardGenotypeTest.createStandardIntervalGenotype(30, 0.5));
        assertTrue(f instanceof NeuroJetTraceFitness);
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitnessFactory#ready()}
     * .
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testReady() throws URISyntaxException {
        NeuroJetTraceFitnessFactory factory = createNeuroJetTraceFitness();
        factory.ready(); // Just tests to make sure no error is thrown
        factory = createNeuroJetTraceFitness(getNeuroJet(), getNeuroJet());
        factory.ready(); // Just tests to make sure no error is thrown
        factory = createNeuroJetTraceFitness(getNeuroJet(), new MockFile("dne", true, true));
        factory.ready(); // Just tests to make sure no error is thrown
    }
}
