/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.neurojet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.virginia.cs.geneticalgorithm.Fitness;
import edu.virginia.cs.geneticalgorithm.ProxyFitness;
import edu.virginia.cs.neurojet.model.NeuroJetActivity;

/**
 * Calculates a short-circuit fitness to determine whether this is a reasonable choice of parameter settings
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public final class NeuroJetQuickFitness implements ProxyFitness {

    private final NeuroJetTraceFitness _traceFitness;
    private final NeuroJetQuickFitnessGenerator _trnGenerator = new NeuroJetQuickFitnessGenerator();
    private final NeuroJetQuickFitnessGenerator _tstGenerator = new NeuroJetQuickFitnessGenerator();
    private final List<Double> _fitnessValues = new ArrayList<Double>();
    private static final int NUM_FIT_VALS = 5; // Run time + test ssd + test dev + train ssd + train dev

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
    public List<Double> fitnessValues() {
        final long beginTime = System.currentTimeMillis();
        _traceFitness.runSimulationIfNeeded(); // starts the real fitness function running
        // Remove any existing files
        if (_fitnessValues.isEmpty()) {
            // Read the resulting activity files
            final double desiredAct = _traceFitness.getDesiredAct();
            final double timeStep = _traceFitness.getTimeStep();
            final File tempDir = _traceFitness.getTempDir();
            final NeuroJetActivity trnAct = new NeuroJetActivity(tempDir, "trnWithinAct.dat", timeStep);
            trnAct.setWaitTime(60000 * 5); // Wait up to five minutes for tstWithinAct.dat to be ready
            _trnGenerator.generateQuickFitness(trnAct, desiredAct);
            _fitnessValues.add(Double.valueOf(System.currentTimeMillis() - beginTime + 1));
            _fitnessValues.add(_trnGenerator.getSampleStdDev());
            _fitnessValues.add(_trnGenerator.getSquaredDeviationFromDesired());
            final NeuroJetActivity tstAct = new NeuroJetActivity(tempDir, "tstWithinAct.dat", timeStep);
            tstAct.setWaitTime(60000 * 1); // Wait up to one minute for tstWithinAct.dat to be ready (after tstWithinAct.dat is
                                           // ready)
            _tstGenerator.generateQuickFitness(tstAct, desiredAct);
            _fitnessValues.add(_tstGenerator.getSampleStdDev());
            _fitnessValues.add(_tstGenerator.getSquaredDeviationFromDesired());
            assert (_fitnessValues.size() == NUM_FIT_VALS);
        }
        return _fitnessValues;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Fitness#totalFitness()
     */
    @Override
    public double totalFitness() {
        final List<Double> fitVals = fitnessValues();
        return 1 / fitVals.get(0) + _trnGenerator.overallFitness(fitVals.get(1), fitVals.get(2))
               + _tstGenerator.overallFitness(fitVals.get(3), fitVals.get(4));
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.ProxyFitness#generatesPostFitness()
     */
    @Override
    public boolean generatesPostFitness() {
        return true;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.ProxyFitness#getPostFitness()
     */
    @Override
    public Fitness getPostFitness() {
        return _traceFitness;
    }
}
