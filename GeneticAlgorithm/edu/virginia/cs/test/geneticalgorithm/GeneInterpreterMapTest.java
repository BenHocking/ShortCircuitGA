/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.geneticalgorithm;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.regex.Pattern;

import org.junit.Test;

import edu.virginia.cs.geneticalgorithm.gene.interpreter.ConstantGeneInterpreter;
import edu.virginia.cs.geneticalgorithm.gene.interpreter.GeneInterpreterMap;

/**
 * Test harness for GeneInterpeterMap
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class GeneInterpreterMapTest {

    private Pattern generatePattern(final String varName) {
        return Pattern.compile("insert" + varName + "here", Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.gene.interpreter.GeneInterpreterMap#getAllPatterns()}.
     */
    @Test
    public final void testGetAllPatterns() {
        final GeneInterpreterMap<Pattern> mapping = new GeneInterpreterMap<Pattern>();
        final Pattern a = generatePattern("A");
        mapping.put(a, new ConstantGeneInterpreter(0.3));
        assertEquals(Collections.singleton(a), mapping.getAllPatterns());
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.gene.interpreter.GeneInterpreterMap#generateValue(java.lang.Object, edu.virginia.cs.geneticalgorithm.StandardGenotype)}
     * .
     */
    @Test
    public final void testGenerateValue() {
        final GeneInterpreterMap<Pattern> mapping = new GeneInterpreterMap<Pattern>();
        final Pattern a = generatePattern("A");
        mapping.put(a, new ConstantGeneInterpreter(0.3));
        assertEquals("0.3", mapping.generateValue(a, null));
    }

}
