/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.neurojet;

import static edu.virginia.cs.common.utils.ArrayNumberUtils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.virginia.cs.common.utils.Condition;
import edu.virginia.cs.common.utils.IntegerRange;
import edu.virginia.cs.common.utils.Pause;
import edu.virginia.cs.geneticalgorithm.Genotype;
import edu.virginia.cs.geneticalgorithm.HaltableFitness;
import edu.virginia.cs.neurojet.model.NeuroJetActivity;
import edu.virginia.cs.neurojet.model.NeuroJetNeuronBuffer;

/**
 * Standard trace fitness function
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public class NeuroJetTraceFitness implements HaltableFitness, Runnable {

    /**
     * Run time + 2 actvty measures + trend measure + perf measure + target measure
     */
    public static final int NUM_FIT_VALS = 6;
    private static final int WAIT_TIME = 20 /* minutes */* 60 /* seconds per minute */* 1000 /* ms per sec */;
    private final NeuroJetQuickFitnessGenerator _tstGenerator = new NeuroJetQuickFitnessGenerator();
    private final NeuroJetTraceFitnessIntermediary _parent;
    private final File _tempDir;
    private Process _process = null;
    private boolean _started = false;
    private boolean _halted = false;
    private long _start = -1;
    private long _end = -1;
    private NeuroJetNeuronBuffer _tstBuff = null;
    private final Object _lock = new Object();
    private final Object _lock2 = new Object();
    private final StringBuffer _out = new StringBuffer();
    private final StringBuffer _err = new StringBuffer();
    private final List<Double> _fitVals = new ArrayList<Double>();

    NeuroJetTraceFitness(final NeuroJetTraceFitnessIntermediary parent, final int dirID) {
        _parent = parent;
        _tempDir = new File(_parent.getParent().getWorkingDir(), "trace_" + String.valueOf(dirID));
        deleteExistingFiles();
    }

    NeuroJetTraceFitnessFactory getGrandparent() {
        return _parent.getParent();
    }

    /**
     * @return Desired activity
     */
    public double getDesiredAct() {
        return _parent.getDesiredAct();
    }

    /**
     * @return Percentage of activity due to external activation
     */
    public double getMePct() {
        return _parent.getMePct();
    }

    /**
     * @return Number of neurons in simulation
     */
    public static int getNumNeurons() {
        return 2048;
    }

    /**
     * @return Pattern size
     */
    public int getMe() {
        return (int) Math.round(getNumNeurons() * getMePct() / 10);
    }

    double getTimeStep() {
        return _parent.getTimeStep();
    }

    /**
     * @return Which neurons are in the puff pattern
     */
    public IntegerRange getPuffRange() {
        final int me = getMe();
        // Tone neurons are from 1 to me
        // Puff neurons are from me + 1 to 2 * me
        final int firstBlinkNeuron = me + 1;
        final int lastBlinkNeuron = 2 * me;
        return new IntegerRange(firstBlinkNeuron, lastBlinkNeuron);
    }

    File getTempDir() {
        return _tempDir;
    }

    boolean isFinished() {
        return _end > 0;
    }

    private void addActivityMeasures(final List<Double> fitnessValues) {
        final NeuroJetActivity tstAct = new NeuroJetActivity(_tempDir, "tstWithinAct_final.dat", getTimeStep());
        tstAct.setWaitTime(WAIT_TIME);
        _tstGenerator.generateQuickFitness(tstAct, getDesiredAct());
        fitnessValues.add(_tstGenerator.getSampleStdDev());
        fitnessValues.add(_tstGenerator.getSquaredDeviationFromDesired());
    }

    private void addTrendMeasure(final List<Double> fitnessValues) {
        final NeuroJetNeuronBuffer tstBuff = getTestBufferFile();
        final int lastElement = tstBuff.numTimeSteps(); // Not inclusive
        if (lastElement < 100) {
            fitnessValues.add(0.0);
        }
        else {
            final List<Double> fracTrend = new ArrayList<Double>();
            final IntegerRange puffRange = getPuffRange();
            // Only want the slope up to shortly after the puff was introduced
            for (final Integer i : new IntegerRange(0, 10, Math.min(lastElement, 680))) {
                fracTrend.add(tstBuff.fractionFired(puffRange, new IntegerRange(i, i + 9)));
            }
            final double trendSlope = slope(fracTrend);
            fitnessValues.add(trendSlope);
        }
    }

    private double slopeContribution(final double slope) {
        final double minSlope = -0.05;
        if (slope < minSlope) return 0;
        return 1000 * Math.pow(slope - minSlope, 2);
    }

    public List<Double> fitnessValues() {
        if (_fitVals.isEmpty()) {
            runSimulationIfNeeded();
            if (_halted) {
                for (int i = 0; i < NUM_FIT_VALS; ++i) {
                    _fitVals.add(0.0);
                }
            }
            else {
                Pause.untilConditionMet(new FitnessFinished(this), WAIT_TIME);
                final double timeDiff = _end - _start + 1;
                _fitVals.add(timeDiff > 0 ? timeDiff : 1);
                addActivityMeasures(_fitVals);
                addTrendMeasure(_fitVals);
                final double fitness = generateTraceFitness();
                assert (fitness >= 0);
                _fitVals.add(fitness);
                _fitVals.add(hasTargetBehavior());
            }
        }
        return _fitVals;
    }

    void runSimulationIfNeeded() {
        synchronized (_lock) {
            if (!_started) {
                _halted = false;
                _started = true;
                invoke();
            }
        }
    }

    private void deleteExistingFiles() {
        // Remove any existing files
        final File[] prevFiles = _tempDir.listFiles();
        if (prevFiles != null) {
            for (final File f : prevFiles) {
                f.delete();
            }
        }
    }

    /**
     * Preprocessor prior to invoking thread
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        deleteExistingFiles();
        if (NeuroJetTraceFitnessIntermediary.DELETE_WORKING_FILES) {
            _tempDir.deleteOnExit();
        }
        final ScriptUpdater updater = getGrandparent().getUpdater();
        for (final File f : getGrandparent().getScriptFiles()) {
            final File script = new File(_tempDir, f.getName());
            try {
                updater.createScriptFromTemplate(script, f, _parent.getGenotype());
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    // TODO Add listener to process
    private void runSimulationInThread() {
        synchronized (_lock2) {
            try {
                _start = System.currentTimeMillis();
                // Each fitness calculation happens in its own directory, allowing this function to be run in parallel
                File scriptFile = getGrandparent().getMainFile();
                deleteExistingFiles();
                final ScriptUpdater updater = getGrandparent().getUpdater();
                if (NeuroJetTraceFitnessIntermediary.DELETE_WORKING_FILES) {
                    _tempDir.deleteOnExit();
                }
                for (final File f : getGrandparent().getScriptFiles()) {
                    final File script = new File(_tempDir, f.getName());
                    if (f.equals(getGrandparent().getMainFile())) {
                        scriptFile = script;
                    }
                    try {
                        updater.createScriptFromTemplate(script, f, _parent.getGenotype());
                    }
                    catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                // Launch NeuroJet
                final List<String> command = new ArrayList<String>();
                try {
                    command.add(getGrandparent().getNeuroJet().getCanonicalPath());
                    command.add(scriptFile.getCanonicalPath());
                    final ProcessBuilder builder = new ProcessBuilder(command);
                    builder.directory(_tempDir);
                    _process = builder.start();
                    final BufferedReader out = new BufferedReader(new InputStreamReader(_process.getInputStream()));
                    String line;
                    while ((line = out.readLine()) != null) {
                        _out.append(line);
                    }
                    try {
                        final BufferedReader err = new BufferedReader(new InputStreamReader(_process.getErrorStream()));
                        while ((line = err.readLine()) != null) {
                            _err.append(line);
                        }
                    }
                    catch (final IOException e) {
                        _err.append(e.getStackTrace().toString());
                    }
                }
                catch (final IOException e) {
                    if (!_halted) {
                        throw new RuntimeException(e);
                    }
                }
            }
            finally {
                _end = System.currentTimeMillis();
            }
        }
    }

    static double generateTimeValue(final int when) {
        // Puff normally starts at time step 651 and lasts to 750
        // Responding in this time window demonstrates memory but not prediction
        if (when >= 651) return 0.4;
        // Ideal are responses in the 551 to 650 window
        if (when >= 551) return 1.0;
        // The 100 ms before that isn't so bad
        if (when >= 451) return 0.5;
        // The 200 ms before that isn't so good
        if (when >= 251) return 0.05;
        // The first few hundred ms is too early
        return 0.01;
    }

    private NeuroJetNeuronBuffer getTestBufferFile() {
        if (_tstBuff == null) {
            _tstBuff = new NeuroJetNeuronBuffer(_tempDir, "tstBuff.dat", WAIT_TIME);
        }
        return _tstBuff;
    }

    /**
     * @return Whether the fitness function acquired the target behavior (i.e., blinked at the right time)
     */
    public double hasTargetBehavior() {
        final int firstBlinkTime = 601;
        final int lastBlinkTime = 650; // Not inclusive
        final double fracPuffFired = getTestBufferFile().fractionFired(getPuffRange(),
                                                                       new IntegerRange(firstBlinkTime, lastBlinkTime));
        return (fracPuffFired / (0.3 * getMePct()));
    }

    private double generateTraceFitness() {
        double maxFracPuffFired = 0.0;
        int maxEra = -1;
        final NeuroJetNeuronBuffer tstBuff = getTestBufferFile();
        final int lastElement = tstBuff.numTimeSteps(); // Not inclusive
        final IntegerRange puffRange = getPuffRange();
        for (final Integer i : new IntegerRange(0, 50, lastElement)) {
            final double fracPuffFired = tstBuff.fractionFired(puffRange, new IntegerRange(i, i + 50));
            if (maxFracPuffFired < fracPuffFired) {
                maxFracPuffFired = fracPuffFired;
                maxEra = i;
            }
        }
        final int early = 200;
        final double fracPuffEarlyFired = (maxEra > early) ? tstBuff.fractionFired(puffRange, new IntegerRange(1, early)) : 0.0;
        final double retval = (maxFracPuffFired - fracPuffEarlyFired) * generateTimeValue(maxEra + 1) / getMePct();
        return retval;
    }

    @Override
    public void halt() {
        if (!_halted) {
            _halted = true;
            _process.destroy();
        }
    }

    @Override
    public String toString() {
        return "{hash = " + hashCode() + ", directory = '" + _tempDir + "'}";
    }

    /**
     * Begin calculating the fitness function in a separate thread. This need to handle multithreading correctly.
     * @param individual {@link Genotype} to return the multi-objective fitness values for.
     */
    private void invoke() {
        final Thread thread = new InvokerThread(this);
        thread.start();
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Fitness#totalFitness()
     */
    @Override
    public double totalFitness() {
        final List<Double> fitVals = fitnessValues();
        final double actFitness = 1E-5 * _tstGenerator.overallFitness(fitVals.get(1), fitVals.get(2));
        final double slopeFitness = slopeContribution(fitVals.get(3));
        return 1 / (fitVals.get(0) + 1) + actFitness + slopeFitness + fitVals.get(4) + fitVals.get(5);
    }

    private static class InvokerThread extends Thread {

        private final NeuroJetTraceFitness _invokee;

        InvokerThread(final NeuroJetTraceFitness invokee) {
            _invokee = invokee;
        }

        @Override
        public void run() {
            _invokee.runSimulationInThread();
        }
    }

    private static class FitnessFinished implements Condition {

        private final NeuroJetTraceFitness _fitness;

        FitnessFinished(final NeuroJetTraceFitness f) {
            _fitness = f;
        }

        /**
         * @see edu.virginia.cs.common.utils.Condition#met()
         */
        @Override
        public boolean met() {
            return _fitness.isFinished();
        }

    }

}
