/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.neurojet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class NeuroJetQuickFitness implements Fitness {

    private final File _mainFile;
    private final List<File> _scriptFiles;
    private final ScriptUpdater _updater;
    private final File _neuroJet;
    private final File _workingDir;
    private static Integer _counter = 0;
    // Made package just to avoid warnings about dead code
    static final boolean DELETE_WORKING_FILES = true;

    public NeuroJetQuickFitness(final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet,
                                final File workingDir) {
        if (scriptFiles == null || scriptFiles.size() == 0)
            throw new IllegalArgumentException("Argument scriptFiles cannot be null or empty");
        _mainFile = scriptFiles.get(0);
        _scriptFiles = scriptFiles;
        _updater = updater;
        _neuroJet = neuroJet;
        _workingDir = workingDir != null ? workingDir : _mainFile.getParentFile();
    }

    public NeuroJetQuickFitness(final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet) {
        this(scriptFiles, updater, neuroJet, null);
    }

    private double generateQuickFitness(final File activityFile, final double desiredAct, final double timeStep) throws IOException {
        final BufferedReader actReader = new BufferedReader(new FileReader(activityFile));
        String line;
        // timeStep is in ms. This should be equivalent to 1/timestep measured in seconds
        final double HzConvFactor = 1000.0 / timeStep;
        double actFitness = 0;
        while ((line = actReader.readLine()) != null) {
            final String[] activities = line.split("\\s");
            final int lastElement = activities.length;
            for (final Integer i : new IntegerRange(0, 100, lastElement)) {
                final double deviationFrac = (desiredAct - HzConvFactor * ArrayNumberUtils.mean(activities, i, i + 99))
                                             / desiredAct;
                actFitness += deviationFrac * deviationFrac;
            }
        }
        actReader.close();
        if (actFitness == 0) actFitness += 1000000; // Never encountered any input
        return actFitness;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Fitness#fitness(edu.virginia.cs.geneticalgorithm.Genotype)
     */
    @Override
    public List<Double> fitness(final Genotype individual) {

        if (!(individual instanceof StandardGenotype)) throw new RuntimeException("individual must be of type StandardGenotype");
        final StandardGenotype genotype = (StandardGenotype) individual;
        // Each fitness calculation happens in its own directory, allowing this function to be run in parallel
        File tempDir;
        synchronized (_counter) {
            tempDir = new File(_workingDir, String.valueOf(++_counter));
        }
        for (final File f : _scriptFiles) {
            final File script = new File(tempDir, f.getName());
            try {
                _updater.createScriptFromTemplate(script, f, genotype);
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        final List<Double> retval = new ArrayList<Double>();
        try {
            // Launch NeuroJet
            final List<String> command = new ArrayList<String>();
            command.add(_neuroJet.getCanonicalPath());
            command.add(_mainFile.getCanonicalPath());
            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(tempDir);
            final Process p = builder.start();
            final long start = System.currentTimeMillis();
            p.waitFor();
            retval.add(Double.valueOf(System.currentTimeMillis() - start));
            // Read the resulting activity files
            final double desiredAct = _updater.getDesiredAct(genotype);
            final double timeStep = _updater.getTimeStep(genotype);
            final File trnAct = new File(tempDir, "trnWithinAct.dat");
            final double trnFitness = generateQuickFitness(trnAct, desiredAct, timeStep);
            retval.add(Double.valueOf(trnFitness));
            final File tstAct = new File(tempDir, "tstWithinAct.dat");
            final double tstFitness = generateQuickFitness(tstAct, desiredAct, timeStep);
            retval.add(Double.valueOf(tstFitness));
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
        return retval;
    }
}
