/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.geneticalgorithm;

import static org.junit.Assert.*;

import org.junit.*;

import edu.virginia.cs.geneticalgorithm.CompositeGeneInterpreter;
import edu.virginia.cs.geneticalgorithm.ConstantGeneInterpreter;
import edu.virginia.cs.geneticalgorithm.Gene;
import edu.virginia.cs.geneticalgorithm.Genotype;
import edu.virginia.cs.geneticalgorithm.IntervalGene;

/**
 * Test harness for CompositeGeneInterpreter
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 19, 2011
 */
public class CompositeGeneInterpreterTest {

    private static CompositeGeneInterpreter _gI;
    private static CompositeGeneInterpreter _gD;
    private static Genotype _g;

    /**
     * Creates instance used by other methods
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        final ConstantGeneInterpreter zero = new ConstantGeneInterpreter(Integer.valueOf(0));
        final ConstantGeneInterpreter one = new ConstantGeneInterpreter(Integer.valueOf(1));
        _gI = new CompositeGeneInterpreter(0, zero, one, true);
        _gD = new CompositeGeneInterpreter(0, zero, one, false);
        _gI = new CompositeGeneInterpreter(0, zero, one, true);
        _g = StandardGenotypeTest.createStandardIntervalGenotype(1, 0.5);
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.CompositeGeneInterpreter#generate(edu.virginia.cs.geneticalgorithm.Genotype)}.
     */
    @Test
    public final void testGenerate() {
        assertEquals("1", _gI.generate(_g));
        assertEquals("0.5", _gD.generate(_g));
        try {
            _gI.generate(StandardGenotypeTest.createStandardGenotype(1));
            fail("Cannot use composite gene interperter with non-interval gene");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("Gene being matched against is not an IntervalGene", e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.CompositeGeneInterpreter#invert(java.lang.String, edu.virginia.cs.geneticalgorithm.Genotype)}
     * .
     */
    @Test
    public final void testInvert() {
        Gene g = _gI.invert("0", _g);
        IntervalGene expected = new IntervalGene(0.25);
        assertEquals(expected, g);
        g = _gI.invert("1", _g);
        expected = new IntervalGene(0.75);
        assertEquals(expected, g);
        g = _gD.invert("0.5", _g);
        expected = new IntervalGene(0.5);
        assertEquals(expected, g);
    }

}
