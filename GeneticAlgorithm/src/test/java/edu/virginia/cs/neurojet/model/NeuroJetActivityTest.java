/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.model;

import static org.junit.Assert.*;
import static edu.virginia.cs.common.utils.ArrayNumberUtils.*;
import static edu.virginia.cs.data.FileLoader.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test harness for NeuroJetActivity
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 21, 2011
 */
public class NeuroJetActivityTest {

    private NeuroJetActivity _activityFile;

    public static NeuroJetActivity getActivityFile() throws Exception {
        return new NeuroJetActivity(getDataDirectory(), "trnWithinAct.dat", 1);
    }

    /**
     * @throws java.lang.Exception If the file cannot be accessed
     */
    @Before
    public void setUp() throws Exception {
        _activityFile = getActivityFile();
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.model.NeuroJetActivity#acrossTrialActivity()}.
     */
    @Test
    public final void testAcrossTrialActivity() {
        final List<Double> aTA = _activityFile.acrossTrialActivity();
        assertEquals(1, aTA.size());
        assertEquals(4.486979196, aTA.get(0), 1e-8);
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.model.NeuroJetActivity#withinTrialActivity()}.
     */
    @Test
    public final void testWithinTrialActivity() {
        final List<Double> wTA = _activityFile.withinTrialActivity();
        assertEquals(750, wTA.size());
        final List<Double> aTA = _activityFile.acrossTrialActivity();
        assertEquals(mean(aTA), mean(wTA), 1E-10);
        final List<Double> wTA1 = _activityFile.withinTrialActivity(0);
        assertEquals(aTA.get(0), mean(wTA1), 0);
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.model.NeuroJetActivity#getActivity()}.
     */
    @Test
    public final void testGetActivity() {
        final List<List<Double>> act = _activityFile.getActivity();
        assertEquals(1, act.size());
        assertEquals(750, act.get(0).size());
        final List<Double> wTA1 = _activityFile.withinTrialActivity(0);
        assertEquals(wTA1, act.get(0));
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.model.NeuroJetActivity#averageActivity()}.
     */
    @Test
    public final void testAverageActivity() {
        double avgAct = _activityFile.averageActivity(600, 649);
        assertEquals(6.184895156862744, avgAct, 0.0);
        avgAct = _activityFile.averageActivity();
        final List<Double> aTA = _activityFile.acrossTrialActivity();
        assertEquals(mean(aTA), avgAct, 1E-10);
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.model.NeuroJetActivity#squaredDeviation(double)}.
     */
    @Test
    public final void testSquaredDeviation() {
        double devSqr = _activityFile.squaredDeviation(5);
        assertEquals(0.01052761381347238, devSqr, 1E-8);
        devSqr = _activityFile.squaredDeviation(600, 649, 5);
        assertEquals(0.056159061310271485, devSqr, 1E-8);
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.model.NeuroJetActivity#sampleStandardDeviation()}.
     */
    @Test
    public final void testSampleStandardDeviation() {
        final double ssd = _activityFile.sampleStandardDeviation();
        assertEquals(2.786638477907005, ssd, 1E-8);
    }

    /**
     * Test method for {@link edu.virginia.cs.neurojet.model.NeuroJetActivity#setWaitTime(int)}.
     */
    @Test
    public final void testSetWaitTime() {
        _activityFile.setWaitTime(0); // Only tests for hard error
    }

}
