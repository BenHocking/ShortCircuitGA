/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.neurojet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.virginia.cs.common.utils.Condition;
import edu.virginia.cs.common.utils.IntegerRange;
import edu.virginia.cs.common.utils.Pause;
import edu.virginia.cs.geneticalgorithm.Genotype;
import edu.virginia.cs.geneticalgorithm.HaltableFitness;
import edu.virginia.cs.neurojet.model.NeuroJetNeuronBuffer;

class NeuroJetTraceFitness implements HaltableFitness, Runnable {

    public static final int NUM_FIT_VALS = 3; // Run time + performance measure + target measure
    private final NeuroJetTraceFitnessIntermediary _parent;
    private final File _tempDir;
    private Process _process = null;
    private boolean _started = false;
    private boolean _halted = false;
    private long _start = -1;
    private long _end = -1;
    private final Object _lock = new Object();
    private final Object _lock2 = new Object();
    private final StringBuffer _out = new StringBuffer();
    private final StringBuffer _err = new StringBuffer();

    NeuroJetTraceFitness(final NeuroJetTraceFitnessIntermediary parent, final int dirID) {
        _parent = parent;
        _tempDir = new File(_parent.getParent().getWorkingDir(), "trace_" + String.valueOf(dirID));
        deleteExistingFiles();
    }

    NeuroJetTraceFitnessFactory getGrandparent() {
        return _parent.getParent();
    }

    double getDesiredAct() {
        return _parent.getDesiredAct();
    }

    double getMePct() {
        return _parent.getMePct();
    }

    double getTimeStep() {
        return _parent.getTimeStep();
    }

    File getTempDir() {
        return _tempDir;
    }

    boolean isFinished() {
        return _end > 0;
    }

    public List<Double> fitnessValues() {
        runSimulationIfNeeded();
        final List<Double> retval = new ArrayList<Double>();
        if (_halted) {
            retval.add(0.0);
            retval.add(0.0);
            retval.add(0.0);
        }
        else {
            // Read the resulting activity files
            final double desiredAct = getDesiredAct();
            final double mePct = getMePct();
            final double timeStep = getTimeStep();
            final int waitTime = 20 /* minutes */* 60 /* seconds per minute */* 1000 /* ms per sec */;
            final NeuroJetNeuronBuffer tstBuff = new NeuroJetNeuronBuffer(_tempDir, "tstBuff.dat", waitTime);
            // Make sure that the rest of the fitness routine is finished before proceeding
            Pause.untilConditionMet(new FitnessFinished(this), waitTime); // We shouldn't have to wait that long or else something
            // is very wrong
            final double timeDiff = _end - _start + 1;
            assert (timeDiff > 0);
            retval.add(timeDiff > 0 ? timeDiff : 1);
            final double fitness = generateTraceFitness(tstBuff, desiredAct, timeStep, mePct);
            assert (!Double.isNaN(fitness));
            retval.add(fitness);
            retval.add(hasTargetBehavior(desiredAct, mePct) ? 1.0 : 0.0);
        }
        return retval;
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

    private int calcPtnNeuronNum(final int totalNeurons, final double desiredAct, final double mePct) {
        return (int) Math.round(totalNeurons * desiredAct * mePct / 22.5);
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

    /**
     * @param desiredAct Desired activity level
     * @param mePct Fraction of activity due to external neurons
     * @return Whether the fitness function acquired the target behavior (i.e., blinked at the right time)
     */
    public boolean hasTargetBehavior(final double desiredAct, final double mePct) {
        final NeuroJetNeuronBuffer tstBuff = new NeuroJetNeuronBuffer(_tempDir, "tstBuff.dat", 0);
        final int me = calcPtnNeuronNum(2048, desiredAct, mePct);
        // Tone neurons are from 1 to me
        // Puff neurons are from me + 1 to 2 * me
        final int firstBlinkNeuron = me + 1;
        final int lastBlinkNeuron = 2 * me;
        final List<Set<Integer>> neuronBuff = tstBuff.getFiringBuffer();
        final int lastElement = neuronBuff.size(); // Not inclusive
        final int first = 601;
        final int last = Math.min(650, lastElement); // Not inclusive
        int totalNumFired = 0;
        double totalPuffFired = 0;
        if (first <= last) {
            for (final Integer i : new IntegerRange(first, last)) {
                final Set<Integer> firingNeurons = neuronBuff.get(i);
                if (!firingNeurons.isEmpty()) {
                    totalNumFired += firingNeurons.size();
                    firingNeurons.retainAll(new IntegerRange(firstBlinkNeuron, lastBlinkNeuron).asSet());
                    totalPuffFired += firingNeurons.size();
                }
            }
        }
        final double fracPuffFired = totalPuffFired / totalNumFired;
        return (fracPuffFired >= 0.3 * mePct);
    }

    private double generateTraceFitness(final NeuroJetNeuronBuffer bufferFile, final double desiredAct, final double timeStep,
                                        final double mePct) {
        final int me = calcPtnNeuronNum(2048, desiredAct, mePct);
        // Tone neurons are from 1 to me
        // Puff neurons are from me + 1 to 2 * me
        final int firstBlinkNeuron = me + 1;
        final int lastBlinkNeuron = 2 * me;
        final List<Set<Integer>> neuronBuff = bufferFile.getFiringBuffer();
        double maxIntervalFitness = 0.0;
        final int lastElement = neuronBuff.size(); // Not inclusive
        for (final Integer i : new IntegerRange(0, 50, lastElement)) {
            final int curLast = Math.min(i + 49, lastElement); // Not inclusive
            int totalNumFired = 0;
            double totalPuffFired = 0;
            // TODO: Eliminate redundancies with above function
            for (final Integer j : new IntegerRange(i, curLast)) {
                final Set<Integer> firingNeurons = neuronBuff.get(j);
                if (!firingNeurons.isEmpty()) {
                    totalNumFired += firingNeurons.size();
                    firingNeurons.retainAll(new IntegerRange(firstBlinkNeuron, lastBlinkNeuron).asSet());
                    totalPuffFired += firingNeurons.size();
                }
            }
            final double intervalFitness = (totalPuffFired / (totalNumFired * mePct)) * generateTimeValue(i + 1);
            if (maxIntervalFitness < intervalFitness) {
                maxIntervalFitness = intervalFitness;
            }
        }
        // Using the maximum interval fitness avoids creating "fitter" individuals by smearing the response
        return maxIntervalFitness;
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
        return 1 / fitVals.get(0) + fitVals.get(1);
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
