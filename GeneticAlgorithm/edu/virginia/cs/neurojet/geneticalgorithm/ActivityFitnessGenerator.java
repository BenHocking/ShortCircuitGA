/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import java.util.ArrayList;
import java.util.List;

import edu.virginia.cs.geneticalgorithm.fitness.FitnessGenerator;

/**
 * Fitness generator dealing with activity calculations
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Mar 20, 2011
 */
public abstract class ActivityFitnessGenerator implements FitnessGenerator {

    private final static double MATCHING_SSD = 0.015; // The value we want the sampleStdDev to be
    private final static double MAX_SSD_FIT = 1e4;
    private final static double MAX_SQ_DEV_FIT = 1e6;
    /** sample standard deviation and squared deviation from desired */
    private static final int NUM_FIT_VALS = 2;

    private final double _desiredAct;
    private boolean _fitnessCalculated;
    private double _sampleStdDev = Double.NaN;
    private double _sqDevFromDesired = Double.NaN;
    private final Object _lock = new Object();

    protected ActivityFitnessGenerator(final double desiredAct) {
        _desiredAct = desiredAct;
        _fitnessCalculated = false;
    }

    protected abstract void generateQuickFitness();

    private double getSampleStdDev() {
        synchronized (_lock) {
            if (!_fitnessCalculated) {
                generateQuickFitness();
                _fitnessCalculated = true;
            }
        }
        return _sampleStdDev;
    }

    protected void setSampleStdDev(final double ssd) {
        _sampleStdDev = ssd;
    }

    private double getSquaredDeviationFromDesired() {
        synchronized (_lock) {
            if (!_fitnessCalculated) {
                generateQuickFitness();
                _fitnessCalculated = true;
            }
        }
        return _sqDevFromDesired;
    }

    protected void setSquaredDeviationFromDesired(final double sqDevFromDesired) {
        _sqDevFromDesired = sqDevFromDesired;
    }

    private double getSampleStdDevFitness(final double sampleStdDev) {
        final double divisor = (sampleStdDev < MATCHING_SSD ? 100 * (MATCHING_SSD - sampleStdDev) : (sampleStdDev - MATCHING_SSD))
                               / (_desiredAct * _desiredAct);
        return Math.min((divisor > 0) ? 0.01 / divisor : MAX_SSD_FIT, MAX_SSD_FIT); // Almost impossible for divisor to be zero.
    }

    // Initially, we want this deviation to be the primary contributor
    private double getSquaredDeviationFromDesiredFitness(final double squaredDeviation) {
        return Math.min(1000.0 / squaredDeviation, MAX_SQ_DEV_FIT);
    }

    protected double getDesiredActivity() {
        return _desiredAct;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.FitnessGenerator#fitnessValues()
     */
    @Override
    public final List<Double> fitnessValues() {
        final List<Double> retval = new ArrayList<Double>();
        // retval.add(getSampleStdDev());
        // retval.add(getSquaredDeviationFromDesired());
        // Use fitness components instead of actual values
        retval.add(getSampleStdDevFitness(getSampleStdDev()));
        retval.add(getSquaredDeviationFromDesiredFitness(getSquaredDeviationFromDesired()));
        return retval;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.FitnessGenerator#overallFitness()
     */
    @Override
    public final double overallFitness() {
        final double x = getSampleStdDevFitness(getSampleStdDev());
        final double y = getSquaredDeviationFromDesiredFitness(getSquaredDeviationFromDesired());
        return x + y;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.FitnessGenerator#numFitnessValues()
     */
    @Override
    public final int numFitnessValues() {
        return NUM_FIT_VALS;
    }

}
