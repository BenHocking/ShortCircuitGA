/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.gene.interpreter;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.gene.IntervalGene;
import edu.virginia.cs.geneticalgorithm.gene.StandardGene;

/**
 * Test harness for ConstantGeneInterpreter
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 19, 2011
 */
public class ConstantGeneInterpreterTest {

    private static ConstantGeneInterpreter _g;

    /**
     * Creates instance used by other methods
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        _g = new ConstantGeneInterpreter(Integer.valueOf(0));
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.gene.interpreter.ConstantGeneInterpreter#setDefaultGene(edu.virginia.cs.geneticalgorithm.Gene)} and
     * {@link edu.virginia.cs.geneticalgorithm.gene.interpreter.ConstantGeneInterpreter#invert(java.lang.String, edu.virginia.cs.geneticalgorithm.Genotype)}
     * .
     */
    @Test
    public final void testSetDefaultGeneAndInvert() {
        assertEquals(new IntervalGene(0.5), _g.invert(null, null));
        _g.setDefaultGene(StandardGene.ZERO);
        assertEquals(StandardGene.ZERO, _g.invert(null, null));
        _g.setDefaultGene(StandardGene.ONE);
        assertEquals(StandardGene.ONE, _g.invert(null, null));
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.gene.interpreter.ConstantGeneInterpreter#generate(edu.virginia.cs.geneticalgorithm.Genotype)}.
     */
    @Test
    public final void testGenerate() {
        assertEquals("0", _g.generate(null)); // O was set as the constant value in the setUpBeforeClass routine
    }

}
