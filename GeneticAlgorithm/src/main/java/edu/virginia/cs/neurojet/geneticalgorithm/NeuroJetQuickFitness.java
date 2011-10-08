/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import static edu.virginia.cs.geneticalgorithm.fitness.AbstractFitness.*;
import static edu.virginia.cs.neurojet.geneticalgorithm.ActivitySummaryFitnessGenerator.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.virginia.cs.geneticalgorithm.fitness.Fitness;
import edu.virginia.cs.geneticalgorithm.fitness.FitnessGenerator;
import edu.virginia.cs.geneticalgorithm.fitness.ProxyFitness;
import edu.virginia.cs.neurojet.model.FileData;

/**
 * Calculates a short-circuit fitness to determine whether this is a reasonable choice of parameter settings
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public final class NeuroJetQuickFitness implements ProxyFitness {

    private final NeuroJetTraceFitness _traceFitness;
    private FitnessGenerator _trnGenerator = null;
    private FitnessGenerator _tstGenerator = null;
    private final List<Double> _fitnessValues = new ArrayList<Double>();

    // Made package just to avoid warnings about dead code
    static final boolean DELETE_WORKING_FILES = true;
    static final boolean DEBUG = false;

    /**
     * Constructor
     * @param fitness Fitness function that this is a proxy for
     */
    public NeuroJetQuickFitness(final Fitness fitness) {
        _traceFitness = (NeuroJetTraceFitness) fitness;
    }

    @Override
    public void prepare() {
        _traceFitness.prepare();
    }

    @Override
    public List<Double> fitnessValues() {
        final long beginTime = System.currentTimeMillis();
        _traceFitness.runSimulationIfNeeded(); // starts the real fitness function running
        // Remove any existing files
        if (_fitnessValues.isEmpty()) {
            // Read the resulting activity files
            final double desiredAct = _traceFitness.getDesiredAct();
            final File tempDir = _traceFitness.getWorkingDir();
            final int waitTime = 60000 * 5; // Wait up to five minutes for tstWithinAct.dat to be ready
            final File signalFile = new File(tempDir, SIGNAL_FILE);
            FileData activityFile = new FileData(tempDir, MEAN_TRAIN_ACTIVITY_DATA_FILE, waitTime, signalFile);
            FileData ssdFile = new FileData(tempDir, SSD_TRAIN_ACTIVITY_DATA_FILE, waitTime, signalFile);
            _trnGenerator = new ActivitySummaryFitnessGenerator(activityFile, ssdFile, desiredAct);
            _fitnessValues.add(1 / (Double.valueOf(System.currentTimeMillis() - beginTime + 1)));
            _fitnessValues.addAll(_trnGenerator.fitnessValues());
            activityFile = new FileData(tempDir, MEAN_TEST_ACTIVITY_DATA_FILE, waitTime, signalFile);
            ssdFile = new FileData(tempDir, SSD_TEST_ACTIVITY_DATA_FILE, waitTime, signalFile);
            _tstGenerator = new ActivitySummaryFitnessGenerator(activityFile, ssdFile, desiredAct);
            _fitnessValues.addAll(_tstGenerator.fitnessValues());
        }
        checkFitnessSize(this, _fitnessValues);
        return Collections.unmodifiableList(_fitnessValues);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.Fitness#totalFitness()
     */
    @Override
    public double totalFitness() {
        final List<Double> fitVals = fitnessValues();
        return fitVals.get(0) + _trnGenerator.overallFitness() + _tstGenerator.overallFitness();
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.ProxyFitness#generatesPostFitness()
     */
    @Override
    public boolean generatesPostFitness() {
        return true;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.ProxyFitness#getPostFitness()
     */
    @Override
    public Fitness getPostFitness() {
        return _traceFitness;
    }

    @Override
    public String toString() {
        final double fitness = totalFitness();
        return "{hash = " + hashCode() + ", fitness = '" + fitness + "'}";
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.Fitness#numFitnessValues()
     */
    @Override
    public int numFitnessValues() {
        return 1 /* elapsed time */+ _trnGenerator.numFitnessValues() + _tstGenerator.numFitnessValues();
    }
}
