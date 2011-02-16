/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.neurojet.model;

import static edu.virginia.cs.common.utils.ArrayNumberUtils.slope;
import static edu.virginia.cs.common.utils.ArrayNumberUtils.sum;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.virginia.cs.common.utils.IntegerRange;
import edu.virginia.cs.neurojet.model.NeuroJetNeuronBuffer;

/**
 * TODO Add description
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 10, 2011
 */
public class NeuroJetNeuronBufferTest {

    private NeuroJetNeuronBuffer _buffFile;

    /**
     * @throws java.lang.Exception If the file cannot be accessed
     */
    @Before
    public void setUp() throws Exception {
        final int dirID = 430;
        final File mainDir = new File("/Users/bhocking/Documents/workspace/ShortCircuitGA/scripts");
        final File tempDir = new File(mainDir, "trace_" + String.valueOf(dirID));
        _buffFile = new NeuroJetNeuronBuffer(tempDir, "tstBuff.dat");
    }

    private double getMePct() {
        return 0.286081119510796;
    }

    private IntegerRange getPuffRange() {
        final int ni = 2048;
        final int me = (int) Math.round(ni * getMePct() / 10);
        // Tone neurons are from 1 to me
        // Puff neurons are from me + 1 to 2 * me
        final int firstBlinkNeuron = me + 1;
        final int lastBlinkNeuron = 2 * me;
        return new IntegerRange(firstBlinkNeuron, lastBlinkNeuron);
    }

    /**
     * 
     */
    @Test
    public void testFractionFired() {
        final int lastElement = _buffFile.numTimeSteps(); // Not inclusive
        final List<Double> fracTrend = new ArrayList<Double>();
        final IntegerRange puffRange = getPuffRange();
        // Only want the slope up to shortly after the puff was introduced
        for (final Integer i : new IntegerRange(0, 10, Math.min(lastElement, 680))) {
            fracTrend.add(_buffFile.fractionFired(puffRange, new IntegerRange(i, i + 9)));
        }
        final double sumFrac = sum(fracTrend);
        final double trendSlope = slope(fracTrend);
        final double minSlope = -0.1;
        final double slopeFitness = (trendSlope < minSlope || sumFrac < 1E-5) ? 0 : trendSlope - minSlope;
        assertEquals(0.10014862889, slopeFitness, 1e-5);

        final int firstBlinkTime = 601;
        final int lastBlinkTime = 650; // Not inclusive
        final double fracPuffFired = _buffFile.fractionFired(getPuffRange(), new IntegerRange(firstBlinkTime, lastBlinkTime));
        System.out.println("fracPuffFired = " + fracPuffFired);
        assert (fracPuffFired < 1);
        final double fitness = fracPuffFired / (0.3 * getMePct());
        System.out.println("fitness = " + fitness);
        assert (fitness < 10);
    }

}
