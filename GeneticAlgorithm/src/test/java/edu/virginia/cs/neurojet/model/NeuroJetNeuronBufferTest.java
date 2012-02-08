/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.model;

import static edu.virginia.cs.common.utils.ArrayNumberUtils.slope;
import static edu.virginia.cs.common.utils.ArrayNumberUtils.sum;
import static org.junit.Assert.*;
import static edu.virginia.cs.data.FileLoader.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.virginia.cs.common.utils.IntegerRange;

/**
 * Test harness for NeuroJetNeuronBuffer
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
        _buffFile = new NeuroJetNeuronBuffer(getDataDirectory(), "tstBuff.dat");
    }

    /**
     * Tests the constructors in NeuroJetNeuronBuffer
     * @throws URISyntaxException shouldn't happen
     */
    @Test
    public void testConstructors() throws URISyntaxException {
        final File f1 = new NeuroJetNeuronBuffer(getDataDirectory(), "tstBuff.dat", 20);
        assertEquals(f1, _buffFile);
        final String fileName = _buffFile.getAbsolutePath();
        final File f2 = new NeuroJetNeuronBuffer(fileName, 20);
        assertEquals(f1, f2);
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
     * Tests fractionFired method in NeuroJetNeuronBuffer
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
        assertEquals(0.10005, slopeFitness, 1e-4);

        int firstBlinkTime = 601;
        int lastBlinkTime = 650; // Not inclusive
        double fracPuffFired = _buffFile.fractionFired(getPuffRange(), new IntegerRange(firstBlinkTime, lastBlinkTime));
        assertEquals(0.054404145077720206, fracPuffFired, 1e-10);
        final double fitness = fracPuffFired / (0.3 * getMePct());
        assertEquals(0.633901148629362, fitness, 1e-10);
        assert (fitness < 10);
        firstBlinkTime = 601;
        lastBlinkTime = 750; // Not inclusive
        fracPuffFired = _buffFile.fractionFired(getPuffRange(), new IntegerRange(firstBlinkTime, lastBlinkTime));
        assertEquals(0.03936269915651359, fracPuffFired, 1e-10);
        final int wayOffEnd = lastBlinkTime + 200;
        final double cfFracPuffFired = _buffFile.fractionFired(getPuffRange(), new IntegerRange(firstBlinkTime, wayOffEnd));
        assertEquals(fracPuffFired, cfFracPuffFired, 1e-10);
        assertEquals(0.0, _buffFile.fractionFired(getPuffRange(), new IntegerRange(1, 1)), 0.0);
    }

    /**
     * Tests getFiringBuffer method in NeuroJetNeuronBuffer
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public void testGetFiringBuffer() throws URISyntaxException {
        final List<Set<Integer>> buff1 = _buffFile.getFiringBuffer();
        List<Set<Integer>> buff2 = _buffFile.getFiringBuffer(true);
        assertEquals(buff1, buff2);
        final NeuroJetNeuronBuffer f1 = new NeuroJetNeuronBuffer(getDataDirectory(), "tstBuff-notthere.dat", 1);
        try {
            buff2 = f1.getFiringBuffer();
            fail("File shouldn't exist");
        }
        catch (final RuntimeException e) {
            final String fileName = f1.getAbsolutePath();
            final Throwable t = e.getCause();
            assertEquals("Couldn't find file '" + fileName + ".ready'", t.getMessage());
        }
    }
}
