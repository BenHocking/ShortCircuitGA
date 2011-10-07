/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import java.io.IOException;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.gene.IntervalGene;

/**
 * Test harness for NeuroJetRandomGenerator
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 17, 2011
 */
public class NeuroJetRandomGeneratorTest {

    /**
     * @throws IOException If script files cannot be read or output directory cannot be written to
     */
    @Test
    public void testGenerator() throws IOException {
        final IntervalGene basis = new IntervalGene();
        final NeuroJetRandomGenerator instance = new NeuroJetRandomGenerator(10001, basis, "Vol", 10000);
        instance.generateScripts();
    }
}
