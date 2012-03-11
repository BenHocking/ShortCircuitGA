/*
 * Copyright (c) 2012 Ashlie B. Hocking All Rights reserved.
 */
package edu.virginia.cs.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.junit.Test;

import edu.virginia.cs.neurojet.geneticalgorithm.NeuroJetGeneticAlgorithm;

/**
 * Test harness for FileLoader
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie B. Hocking</a>
 * @since Mar 10, 2012
 */
public class FileLoaderTest {

    /**
     * Test method for
     * {@link edu.virginia.cs.data.FileLoader#getFileFromProperty(java.util.Properties, java.lang.String, java.lang.String)}
     * .
     * @throws Exception Should never happen
     */
    @Test
    public final void testGetFileFromProperty() throws Exception {
        new FileLoader(); // For trivial coverage
        final Properties configFile = new Properties();
        // Load default property values
        final File NJGAProp =
                new File(NeuroJetGeneticAlgorithm.class.getResource("NeuroJetGeneticAlgorithm.properties").getFile());
        if (NJGAProp.exists()) {
            configFile.load(new FileInputStream(NJGAProp));
        }
        assertEquals(new File("dne"), FileLoader.getFileFromProperty(configFile, "undefined", "dne"));
        assertTrue(FileLoader.getFileFromProperty(configFile, "SCRIPT_FILE", "dne")
                             .getAbsolutePath().endsWith("../scripts/trace_full.nj"));
        final Properties testConfigFile = new Properties();
        // Load default property values
        final File NJGATestProp =
                new File(NeuroJetGeneticAlgorithm.class.getResource("NeuroJetGeneticAlgorithmTest.properties")
                                                       .getFile());
        if (NJGAProp.exists()) {
            testConfigFile.load(new FileInputStream(NJGATestProp));
        }
        assertTrue(FileLoader.getFileFromProperty(testConfigFile, "SCRIPT_FILE", "dne")
                             .getAbsolutePath().endsWith("edu/virginia/cs/data/trace_full.nj"));
    }

    /**
     * Test method for {@link edu.virginia.cs.data.FileLoader#getFile(java.lang.String)}.
     * @throws URISyntaxException Should never happen
     */
    @Test
    public final void testGetFile() throws URISyntaxException {
        final String fileName = "fileName";
        assertTrue(FileLoader.getFile(fileName)
                             .getAbsolutePath()
                             .endsWith("edu/virginia/cs/data/fileName"));
    }

    /**
     * Test method for {@link edu.virginia.cs.data.FileLoader#getDataDirectory()}.
     * @throws URISyntaxException Should never happen
     */
    @Test
    public final void testGetDataDirectory() throws URISyntaxException {
        assertTrue(FileLoader.getDataDirectory()
                             .getAbsolutePath()
                             .endsWith("edu/virginia/cs/data"));
    }
}
