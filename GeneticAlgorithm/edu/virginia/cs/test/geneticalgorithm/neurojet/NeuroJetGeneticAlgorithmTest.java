/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.geneticalgorithm.neurojet;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetGeneticAlgorithm;
import edu.virginia.cs.test.data.TestFileLoader;

/**
 * Test harness for NeuroJetGeneticAlgorithm
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class NeuroJetGeneticAlgorithmTest {

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.neurojet.NeuroJetGeneticAlgorithm#main(java.lang.String[])}.
     * @throws URISyntaxException if unable to parse workingDir
     */
    @Test
    public final void testMain() throws URISyntaxException {
        final File dataDir = TestFileLoader.getDataDirectory();
        final File neuroJet = new File("/Users/bhocking/Documents/workspace/NeuroJet/build/NeuroJet");
        final File workingDir = new File(dataDir, "test_output");
        final File scriptFile = new File(dataDir, "trace_full.nj");
        workingDir.deleteOnExit();
        final String[] args = {
            "2", "2", neuroJet.getAbsolutePath(), workingDir.getAbsolutePath(), scriptFile.getAbsolutePath()
        };
        NeuroJetGeneticAlgorithm.main(args);
        assertTrue(workingDir.exists());
        final String[] bad_args = {
            "0", "0"
        };
        try {
            NeuroJetGeneticAlgorithm.main(bad_args);
            fail("Shouldn't have been able to do pca");
        }
        catch (final RuntimeException e) {
            assertEquals("Cannot perform PCA without history.", e.getMessage());
        }
        final String[] bad_args2 = {
            "1", "1", neuroJet.getAbsolutePath(), workingDir.getAbsolutePath(), scriptFile.getAbsolutePath()
        };
        try {
            NeuroJetGeneticAlgorithm.main(bad_args2);
            fail("Shouldn't have been able to do pca");
        }
        catch (final RuntimeException e) {
            assertEquals("Cannot perform PCA without at least two rows of data.", e.getMessage());
        }
    }

}
