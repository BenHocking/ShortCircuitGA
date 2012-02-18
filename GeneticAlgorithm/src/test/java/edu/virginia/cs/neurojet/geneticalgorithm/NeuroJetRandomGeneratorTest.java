/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.junit.Test;

import edu.virginia.cs.data.FileLoader;
import edu.virginia.cs.geneticalgorithm.gene.IntervalGene;

/**
 * Test harness for NeuroJetRandomGenerator
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 17, 2011
 */
public class NeuroJetRandomGeneratorTest {

    /**
     * @throws IOException If script files cannot be read or output directory cannot be written to
     * @throws URISyntaxException If file name specified in properties is invalid
     */
    @Test
    public void testGenerator() throws IOException, URISyntaxException {
        final IntervalGene basis = new IntervalGene();
        final Properties configFile = new Properties();
        final File propFile =
                new File(NeuroJetGeneticAlgorithmTest.class.getResource("NeuroJetGeneticAlgorithmTest.properties")
                                                           .getFile());
        configFile.load(new FileInputStream(propFile));
        NeuroJetGeneticAlgorithm.WORKING_DIR = FileLoader.getFileFromProperty(configFile, "WORKING_DIR", null);
        if (!NeuroJetGeneticAlgorithm.WORKING_DIR.exists()) {
            NeuroJetGeneticAlgorithm.WORKING_DIR.mkdir();
        }
        NeuroJetGeneticAlgorithm.SCRIPT_FILE = FileLoader.getFileFromProperty(configFile, "SCRIPT_FILE", null);
        final NeuroJetRandomGenerator instance = new NeuroJetRandomGenerator(20001, basis, "Vol", 2);
        instance.generateScripts();
    }
}
