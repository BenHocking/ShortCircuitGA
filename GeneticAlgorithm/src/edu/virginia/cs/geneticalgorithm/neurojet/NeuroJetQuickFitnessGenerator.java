/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.neurojet;

import java.util.List;

import edu.virginia.cs.common.utils.IntegerRange;
import edu.virginia.cs.neurojet.model.NeuroJetActivity;

/**
 * Calculates fitness for a particular activity file
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Nov 5, 2010
 */
final class NeuroJetQuickFitnessGenerator {

    private final static double MATCHING_SSD = 0.015; // The value we want the sampleStdDev to be
    private final static double MAX_SSD_FIT = 1e4;
    private final static double MAX_SQ_DEV_FIT = 1e6;
    private double _sampleStdDev = Double.NaN;
    private double _sqDevFromDesired = Double.NaN;
    private double _desiredAct = Double.NaN;

    double getSampleStdDev() {
        return _sampleStdDev;
    }

    double getSquaredDeviationFromDesired() {
        return _sqDevFromDesired;
    }

    double getSampleStdDevFitness(final double sampleStdDev) {
        final double divisor = (sampleStdDev < MATCHING_SSD ? 100 * (MATCHING_SSD - sampleStdDev) : (sampleStdDev - MATCHING_SSD))
                               / (_desiredAct * _desiredAct);
        return Math.min((divisor > 0) ? 0.01 / divisor : MAX_SSD_FIT, MAX_SSD_FIT); // Almost impossible for divisor to be zero.
    }

    // Initially, we want this deviation to be the primary contributor
    double getSquaredDeviationFromDesiredFitness(final double squaredDeviation) {
        return Math.min(1000.0 / squaredDeviation, MAX_SQ_DEV_FIT);
    }

    double overallFitness(final double sampleStdDev, final double squaredDeviation) {
        final double x = getSampleStdDevFitness(sampleStdDev);
        final double y = getSquaredDeviationFromDesiredFitness(squaredDeviation);
        assert (y > x);
        return x + y;
    }

    void generateQuickFitness(final NeuroJetActivity activityFile, final double desiredAct) {
        _sampleStdDev = activityFile.sampleStandardDeviation();
        final List<List<Double>> activity = activityFile.getActivity();
        _desiredAct = desiredAct;
        double actFitness = 0;
        int numSums = 0;
        for (final List<Double> withinTrial : activity) {
            final int lastElement = withinTrial.size(); // Not inclusive
            numSums += lastElement;
            assert (lastElement == 750);
            for (final Integer i : new IntegerRange(0, 75, lastElement)) {
                final int curLast = Math.min(i + 74, lastElement); // Not inclusive
                final int curNumElems = curLast - i + 1;
                actFitness += activityFile.squaredDeviation(i + 1, curLast + 1, desiredAct) * curNumElems;
            }
        }
        assert (actFitness > 0);
        _sqDevFromDesired = actFitness /= numSums;
        assert ((1000.0 / _sqDevFromDesired) < MAX_SQ_DEV_FIT);
    }
}
