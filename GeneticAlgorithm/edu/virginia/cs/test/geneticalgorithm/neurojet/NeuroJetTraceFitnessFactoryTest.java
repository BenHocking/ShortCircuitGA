/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.geneticalgorithm.neurojet;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.Fitness;
import edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitness;
import edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetTraceFitnessFactory;
import edu.virginia.cs.geneticalgorithm.neurojet.ScriptUpdater;
import edu.virginia.cs.test.data.TestFileLoader;
import edu.virginia.cs.test.geneticalgorithm.StandardGenotypeTest;

/**
 * Test harness for NeuroJetTraceFitnessFactory
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 22, 2011
 */
public class NeuroJetTraceFitnessFactoryTest {

    /**
     * @return NeuroJet executable
     * @throws URISyntaxException Shouldn't happen
     */
    public static File getNeuroJet() throws URISyntaxException {
        return TestFileLoader.getFile("NeuroJet");
    }

    /**
     * @return NeuroJetTraceFitness suitable for testing
     * @throws URISyntaxException Shouldn't happen
     */
    public static NeuroJetTraceFitnessFactory createNeuroJetTraceFitness() throws URISyntaxException {
        final File scriptFile = TestFileLoader.getFile("trace_full.nj");
        final List<File> scriptFiles = Collections.singletonList(scriptFile);
        return new NeuroJetTraceFitnessFactory(scriptFiles, new ScriptUpdater(), getNeuroJet(), TestFileLoader.getDataDirectory());
    }

    /**
     * Test method for constructors
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testConstructors() throws URISyntaxException {
        try {
            new NeuroJetTraceFitnessFactory(null, null, null, null);
            fail("Null script files");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Argument scriptFiles cannot be null or empty", e.getMessage());
        }
        try {
            new NeuroJetTraceFitnessFactory(new ArrayList<File>(), null, null, null);
            fail("No script files");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Argument scriptFiles cannot be null or empty", e.getMessage());
        }
        final File scriptFile = TestFileLoader.getFile("trace_full.nj");
        final List<File> scriptFiles = Collections.singletonList(scriptFile);
        try {
            new NeuroJetTraceFitnessFactory(scriptFiles, null, null, null);
            fail("No NeuroJet");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Argument neuroJet must refer to an executable", e.getMessage());
        }
        try {
            new NeuroJetTraceFitnessFactory(scriptFiles, null, scriptFile, null);
            fail("Bad NeuroJet");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Argument neuroJet must refer to an executable", e.getMessage());
        }
        new NeuroJetTraceFitnessFactory(scriptFiles, new ScriptUpdater(), getNeuroJet());
        new NeuroJetTraceFitnessFactory(scriptFiles, new ScriptUpdater(), getNeuroJet(), TestFileLoader.getDataDirectory());
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
        final Fitness f = factory.createFitness(StandardGenotypeTest.createStandardGenotype(30));
        assertTrue(f instanceof NeuroJetTraceFitness);
    }
}
