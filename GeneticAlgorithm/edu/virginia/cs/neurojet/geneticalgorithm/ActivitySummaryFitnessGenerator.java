/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import java.util.List;

import edu.virginia.cs.neurojet.model.FileData;

/**
 * Fitness generator dealing with activity summary files
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Mar 20, 2011
 */
final class ActivitySummaryFitnessGenerator extends ActivityFitnessGenerator {

    public static final String MEAN_TRAIN_ACTIVITY_DATA_FILE = "fit_trn_mean_act.dat";
    public static final String SSD_TRAIN_ACTIVITY_DATA_FILE = "fit_trn_ssd_act.dat";
    public static final String MEAN_TEST_ACTIVITY_DATA_FILE = "fit_tst_mean_act.dat";
    public static final String SSD_TEST_ACTIVITY_DATA_FILE = "fit_tst_ssd_act.dat";
    public static final String SIGNAL_FILE = "fit_quick.ready";

    private final FileData _activityFile;
    private final FileData _ssdFile;

    /**
     * @param activityFile File containing average activity information
     * @param ssdFile File containing sample standard deviation information
     * @param desiredAct Desired activity level (in Hz)
     */
    public ActivitySummaryFitnessGenerator(final FileData activityFile, final FileData ssdFile, final double desiredAct) {
        super(desiredAct);
        _activityFile = activityFile;
        _ssdFile = ssdFile;
    }

    @Override
    protected void generateQuickFitness() {
        final List<Double> actData = _activityFile.getData();
        double sumSqDeviation = 0;
        int numSums = 0;
        final double desiredAct = getDesiredActivity();
        for (final Double activity : actData) {
            final double deviation = (activity - desiredAct) / desiredAct;
            ++numSums;
            sumSqDeviation += deviation * deviation;
        }
        setSquaredDeviationFromDesired(sumSqDeviation / numSums);
        final List<Double> ssdData = _ssdFile.getData();
        setSampleStdDev(ssdData.get(0));
    }
}
