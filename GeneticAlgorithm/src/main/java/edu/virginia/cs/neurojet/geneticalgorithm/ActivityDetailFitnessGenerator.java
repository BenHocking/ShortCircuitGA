/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import java.util.List;

import edu.virginia.cs.common.utils.IntegerRange;
import edu.virginia.cs.neurojet.model.NeuroJetActivity;

/**
 * Calculates fitness for a particular NeuroJet activity file
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Nov 5, 2010
 */
final class ActivityDetailFitnessGenerator extends ActivityFitnessGenerator {

    final NeuroJetActivity _activityFile;

    /**
     * @param activityFile NeuroJetActivity file containing activity information
     * @param desiredAct Desired activity level (in Hz)
     */
    public ActivityDetailFitnessGenerator(final NeuroJetActivity activityFile, final double desiredAct) {
        super(desiredAct);
        _activityFile = activityFile;
    }

    @Override
    protected void generateQuickFitness() {
        setSampleStdDev(_activityFile.sampleStandardDeviation());
        final List<List<Double>> activity = _activityFile.getActivity();
        double sumSqDeviation = 0;
        int numSums = 0;
        for (final List<Double> withinTrial : activity) {
            final int lastElement = withinTrial.size(); // Not inclusive
            numSums += lastElement;
            for (final Integer i : new IntegerRange(0, 75, lastElement)) {
                final int curLast = Math.min(i + 74, lastElement); // Not inclusive
                final int curNumElems = curLast - i + 1;
                sumSqDeviation += _activityFile.squaredDeviation(i + 1, curLast + 1, getDesiredActivity()) * curNumElems;
            }
        }
        setSquaredDeviationFromDesired(sumSqDeviation / numSums);
    }
}
