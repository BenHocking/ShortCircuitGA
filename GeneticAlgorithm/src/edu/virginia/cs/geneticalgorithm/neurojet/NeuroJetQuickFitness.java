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
import edu.virginia.cs.geneticalgorithm.AbstractFitness;
import edu.virginia.cs.geneticalgorithm.Genotype;
import edu.virginia.cs.geneticalgorithm.StandardGenotype;

/**
 * Calculates a short-circuit fitness to determine whether this is a reasonable choice of parameter settings
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public final class NeuroJetQuickFitness extends AbstractFitness {

    private final File _mainFile;
    private final List<File> _scriptFiles;
    private final ScriptUpdater _updater;
    private final File _neuroJet;
    private final File _workingDir;
    private static Integer _counter = 0;
    // Made package just to avoid warnings about dead code
    static final boolean DELETE_WORKING_FILES = true;
    static final boolean DEBUG = false;
    private final Map<Genotype, List<Double>> _fitMap = new HashMap<Genotype, List<Double>>();

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

    public NeuroJetQuickFitness(final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet) {
        this(scriptFiles, updater, neuroJet, null);
    }

    private double generateQuickFitness(final File activityFile, final double desiredAct, final double timeStep) {
        try {
            final BufferedReader actReader = new BufferedReader(new FileReader(activityFile));
            String line;
            // timeStep is in ms. This should be equivalent to 1/timestep measured in seconds
            final double HzConvFactor = 1000.0 / timeStep;
            double actFitness = 0;
            int numSums = 0;
            while ((line = actReader.readLine()) != null) {
                final String[] activities = line.split("\\s");
                final int lastElement = activities.length;
                for (final Integer i : new IntegerRange(0, 100, lastElement)) {
                    final double deviationFrac = (desiredAct - HzConvFactor * ArrayNumberUtils.mean(activities, i, i + 99))
                                                 / desiredAct;
                    actFitness += deviationFrac * deviationFrac;
                    ++numSums;
                }
            }
            actReader.close();
            if (numSums == 0 || Double.isNaN(actFitness) || Double.isInfinite(actFitness)) {
                actFitness = 1000000; // Never encountered any input
            }
            else {
                // Calculate average squared deviation
                actFitness /= numSums;
            }
            assert (actFitness > 0);
            // Small deviations are what we want, but large fitness values are considered better
            return 1.0 / actFitness;
        }
        catch (final IOException e) {
            System.out.println(e);
            return 0; // Something went wrong with the program. Label this as a DOA individual
        }
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Fitness#fitness(edu.virginia.cs.geneticalgorithm.Genotype)
     */
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
            retval.add(1.0 / (System.currentTimeMillis() - start + 1));
            // Read the resulting activity files
            final double desiredAct = _updater.getDesiredAct(genotype);
            final double timeStep = _updater.getTimeStep(genotype);
            // TODO: Improve upon this hack
            Thread.sleep(1000); // Make sure we catch up with O/S
            final File trnAct = new File(tempDir, "trnWithinAct.dat");
            final double trnFitness = generateQuickFitness(trnAct, desiredAct, timeStep);
            retval.add(trnFitness);
            final File tstAct = new File(tempDir, "tstWithinAct.dat");
            final double tstFitness = generateQuickFitness(tstAct, desiredAct, timeStep);
            retval.add(tstFitness);
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
        return retval;
    }
}
