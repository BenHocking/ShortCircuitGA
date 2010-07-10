/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.neurojet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.virginia.cs.common.ArrayNumberUtils;
import edu.virginia.cs.common.IntegerRange;
import edu.virginia.cs.geneticalgorithm.Fitness;
import edu.virginia.cs.geneticalgorithm.Genotype;
import edu.virginia.cs.geneticalgorithm.StandardGenotype;

/**
 * Calculates a short-circuit fitness to determine whether this is a reasonable choice of parameter settings
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public final class NeuroJetQuickFitness implements Fitness {

    private final File _mainFile;
    private final List<File> _scriptFiles;
    private final ScriptUpdater _updater;
    private final File _neuroJet;
    private final File _workingDir;
    private final NeuroJetQuickFitnessGenerator _trnGenerator = new NeuroJetQuickFitnessGenerator();
    private final NeuroJetQuickFitnessGenerator _tstGenerator = new NeuroJetQuickFitnessGenerator();
    private static final int NUM_FIT_VALS = 5; // Run time + test ssd + test dev + train ssd + train dev
    private static Integer _counter = 0;
    private final Map<Genotype, List<Double>> _fitMap = new HashMap<Genotype, List<Double>>();

    private final class NeuroJetQuickFitnessGenerator {

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

        double getSampleStdDevFitness() {
            final double divisor = (_sampleStdDev < MATCHING_SSD ? 100 * (MATCHING_SSD - _sampleStdDev)
                                                                : (_sampleStdDev - MATCHING_SSD))
                                   / (_desiredAct * _desiredAct);
            return (divisor > 0) ? 0.01 / divisor : 1e4; // Almost impossible for divisor to be zero. Almost.
        }

        // Initially, we want this deviation to be the primary contributor
        double getSquaredDeviationFromDesiredFitness() {
            return 1000.0 / _sqDevFromDesired;
        }

        double overallFitness() {
            final double x = getSampleStdDevFitness();
            final double y = getSquaredDeviationFromDesiredFitness();
            assert (y > x);
            return x + y;
        }

        private void generateQuickFitness(final File activityFile, final double desiredAct, final double timeStep) {
            try {
                final BufferedReader actReader = new BufferedReader(new FileReader(activityFile));
                _desiredAct = desiredAct;
                String line;
                // timeStep is in ms. This should be equivalent to 1/timestep measured in seconds
                final double HzConvFactor = 1000.0 / timeStep;
                double actFitness = 0;
                int numSums = 0;
                double totalSum = 0.0;
                double totalSoS = 0.0;
                while ((line = actReader.readLine()) != null) {
                    final String[] activities = line.split("\\s");
                    final int lastElement = activities.length; // Not inclusive
                    totalSum += ArrayNumberUtils.sum(activities);
                    totalSoS += ArrayNumberUtils.sumOfSquares(activities);
                    numSums += lastElement;
                    for (final Integer i : new IntegerRange(0, 100, lastElement)) {
                        final int curLast = Math.min(i + 99, lastElement); // Not inclusive
                        final int curNumElems = curLast - i;
                        final double curActivity = HzConvFactor * ArrayNumberUtils.mean(activities, i, curLast);
                        final double deviationFrac = (desiredAct - curActivity) / desiredAct;
                        actFitness += deviationFrac * deviationFrac * curNumElems;
                    }
                }
                actReader.close();
                if (numSums > 1 && !Double.isNaN(actFitness) && !Double.isInfinite(actFitness)) {
                    // Calculate sample standard deviation
                    _sampleStdDev = Math.sqrt((numSums * totalSoS - totalSum * totalSum) / (numSums * (numSums - 1)));
                    // Calculate average squared deviation
                    assert (actFitness > 0);
                    _sqDevFromDesired = actFitness /= numSums;
                }
            }
            catch (final IOException e) {
                System.out.println(e);
            }
        }
    }

    // Made package just to avoid warnings about dead code
    static final boolean DELETE_WORKING_FILES = true;
    static final boolean DEBUG = false;

    /**
     * Constructor
     * @param scriptFiles {@link java.util.List List} of script files that NeuroJet needs to run the experiment. The first file in
     * the list is the one actually run, with the other files presumably a dependency of it.
     * @param updater {@link ScriptUpdater} that contains the parameter values to map to the
     * {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}.
     * @param neuroJet Location of the NeuroJet executable
     * @param workingDir Directory that subdirectories will be created off (if null, this uses the location of the first file in the
     * scriptFiles {@link java.util.List List}).
     */
    public NeuroJetQuickFitness(final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet,
                                final File workingDir) {
        if (scriptFiles == null || scriptFiles.size() == 0)
            throw new IllegalArgumentException("Argument scriptFiles cannot be null or empty");
        if (neuroJet == null || !neuroJet.canExecute())
            throw new IllegalArgumentException("Argument neuroJet must refer to an executable");
        _mainFile = scriptFiles.get(0);
        _scriptFiles = scriptFiles;
        _updater = updater;
        _neuroJet = neuroJet;
        _workingDir = workingDir != null ? workingDir : _mainFile.getParentFile();
    }

    /**
     * Constructor
     * @param scriptFiles {@link java.util.List List} of script files that NeuroJet needs to run the experiment. The first file in
     * the list is the one actually run (and determines where the working directory will be), with the other files presumably a
     * dependency of it.
     * @param updater {@link ScriptUpdater} that contains the parameter values to map to the
     * {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}.
     * @param neuroJet Location of the NeuroJet executable
     */
    public NeuroJetQuickFitness(final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet) {
        this(scriptFiles, updater, neuroJet, null);
    }

    @Override
    public List<Double> fitnessValues(final Genotype individual) {
        if (!(individual instanceof StandardGenotype)) throw new RuntimeException("individual must be of type StandardGenotype");
        List<Double> retval = _fitMap.get(individual);
        if (retval != null) return retval;
        final StandardGenotype genotype = (StandardGenotype) individual;
        // Each fitness calculation happens in its own directory, allowing this function to be run in parallel
        File scriptFile = _mainFile;
        File tempDir;
        synchronized (_counter) {
            tempDir = new File(_workingDir, String.valueOf(++_counter));
        }
        // Remove any existing files
        final File[] prevFiles = tempDir.listFiles();
        if (prevFiles != null) {
            for (final File f : prevFiles) {
                f.delete();
            }
        }
        if (DELETE_WORKING_FILES) {
            tempDir.deleteOnExit();
        }
        for (final File f : _scriptFiles) {
            final File script = new File(tempDir, f.getName());
            if (f.equals(_mainFile)) {
                scriptFile = script;
            }
            try {
                _updater.createScriptFromTemplate(script, f, genotype);
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        retval = new ArrayList<Double>();
        try {
            // Launch NeuroJet
            final List<String> command = new ArrayList<String>();
            command.add(_neuroJet.getCanonicalPath());
            command.add(scriptFile.getCanonicalPath());
            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(tempDir);
            final Process p = builder.start();
            final long start = System.currentTimeMillis();
            if (DEBUG) {
                final BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }
            }
            final BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            final StringBuilder err = new StringBuilder();
            String sErr;
            while ((sErr = stdError.readLine()) != null) {
                err.append(sErr);
            }
            if (err.length() > 0) {
                System.out.println("Errors:\n" + err.toString());
            }
            p.waitFor();
            retval.add(Double.valueOf(System.currentTimeMillis() - start + 1));
            // Read the resulting activity files
            final double desiredAct = _updater.getDesiredAct(genotype);
            final double timeStep = _updater.getTimeStep(genotype);
            final File trnAct = new File(tempDir, "trnWithinAct.dat");
            if (!trnAct.exists()) {
                // TODO: Improve upon this hack
                Thread.sleep(1000); // Make sure we catch up with O/S
            }
            _trnGenerator.generateQuickFitness(trnAct, desiredAct, timeStep);
            retval.add(_trnGenerator.getSampleStdDev());
            retval.add(_trnGenerator.getSquaredDeviationFromDesired());
            final File tstAct = new File(tempDir, "tstWithinAct.dat");
            _tstGenerator.generateQuickFitness(tstAct, desiredAct, timeStep);
            retval.add(_tstGenerator.getSampleStdDev());
            retval.add(_tstGenerator.getSquaredDeviationFromDesired());
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        catch (final InterruptedException e) {
            // We might want to handle this more gracefully. For example, if the process were interrupted by a bad connection
            // (imagine we're running this remotely), we might want to just try again. If the process were interrupted by the user,
            // however, such an approach would get old fast.
            throw new RuntimeException(e);
        }
        // Create copy in case the individual is changed after the fitness has been calculated
        _fitMap.put(individual.clone(), retval);
        assert (retval.size() == NUM_FIT_VALS);
        return retval;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Fitness#totalFitness(edu.virginia.cs.geneticalgorithm.Genotype)
     */
    @Override
    public double totalFitness(final Genotype individual) {
        final List<Double> fitVals = fitnessValues(individual);
        return 1 / fitVals.get(0) + _trnGenerator.overallFitness() + _tstGenerator.overallFitness();
    }
}
