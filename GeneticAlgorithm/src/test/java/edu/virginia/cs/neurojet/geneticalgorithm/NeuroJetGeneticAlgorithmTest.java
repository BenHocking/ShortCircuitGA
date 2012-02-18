/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import edu.virginia.cs.data.FileLoader;

/**
 * Test harness for NeuroJetGeneticAlgorithm
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class NeuroJetGeneticAlgorithmTest {

    /**
     * Test method for
     * {@link edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetGeneticAlgorithm#main(java.lang.String[])}.
     * @throws URISyntaxException if unable to parse workingDir
     * @throws IOException if unable to find config file
     */
    @Test
    public final void testMain() throws URISyntaxException, IOException {
        final File dataDir = FileLoader.getDataDirectory();
        final File workingDir = new File(dataDir, "test_output");
        final File NJGAProp =
                new File(NeuroJetGeneticAlgorithm.class.getResource("NeuroJetGeneticAlgorithm.properties").getFile());
        assertTrue(NJGAProp.exists());
        // If the directory wasn't cleaned up from last time, delete it
        FileUtils.deleteDirectory(workingDir);
        assertFalse(workingDir.exists());
        final File propFile =
                new File(NeuroJetGeneticAlgorithmTest.class.getResource("NeuroJetGeneticAlgorithmTest.properties")
                                                           .getFile());
        assertTrue(propFile.exists());
        final String[] args = {
            propFile.getPath()
        };
        NeuroJetGeneticAlgorithm.main(args);
        assertTrue(workingDir.exists());
        /*
         * PCA is currently disabled final String[] bad_args = { "0", "0", "1" }; try {
         * NeuroJetGeneticAlgorithm.main(bad_args); //PCA is currently disabled
         * fail("Shouldn't have been able to do pca"); } catch (final RuntimeException e) {
         * assertEquals("Cannot perform PCA without history.", e.getMessage()); } final String[] bad_args2 = { "1", "1",
         * "2", neuroJet.getAbsolutePath(), workingDir.getAbsolutePath(), scriptFile.getAbsolutePath() }; try {
         * NeuroJetGeneticAlgorithm.main(bad_args2); //PCA is currently disabled
         * fail("Shouldn't have been able to do pca"); } catch (final RuntimeException e) {
         * assertEquals("Cannot perform PCA without at least two rows of data.", e.getMessage()); }
         */
        FileUtils.deleteDirectory(workingDir);
    }
}
