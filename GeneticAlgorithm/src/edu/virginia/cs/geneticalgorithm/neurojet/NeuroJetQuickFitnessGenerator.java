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
        return (divisor > 0) ? 0.01 / divisor : 1e4; // Almost impossible for divisor to be zero. Almost.
    }

    // Initially, we want this deviation to be the primary contributor
    double getSquaredDeviationFromDesiredFitness(final double squaredDeviation) {
        return 1000.0 / squaredDeviation;
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
        double weightedDenom = 0;
        for (final List<Double> withinTrial : activity) {
            final int lastElement = withinTrial.size(); // Not inclusive
            for (final Integer i : new IntegerRange(0, 75, lastElement)) {
                final int curLast = Math.min(i + 74, lastElement); // Not inclusive
                final int curNumElems = curLast - i;
                final double weight = NeuroJetTraceFitness.generateTimeValue(i + 1) * curNumElems;
                weightedDenom += weight;
                actFitness += activityFile.squaredDeviation(desiredAct) * weight;
            }
        }
        assert (actFitness > 0);
        assert (weightedDenom > 0);
        _sqDevFromDesired = (actFitness / weightedDenom);
    }
}
