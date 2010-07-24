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

import edu.virginia.cs.common.utils.Pause;
import edu.virginia.cs.geneticalgorithm.Fitness;
import edu.virginia.cs.geneticalgorithm.Genotype;
import edu.virginia.cs.geneticalgorithm.StandardGenotype;

/**
 * Calculates a short-circuit fitness to determine whether this is a reasonable choice of parameter settings
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public final class NeuroJetTraceFitness implements Fitness {

    private final File _mainFile;
    private final List<File> _scriptFiles;
    private final ScriptUpdater _updater;
    private final File _neuroJet;
    private final File _workingDir;
    private static final int NUM_FIT_VALS = 3; // Run time + trace score + directory ID
    private static Integer _counter = 0;
    private final Map<Genotype, List<Double>> _fitMap = new HashMap<Genotype, List<Double>>();

    private int calcPtnNeuronNum(final int totalNeurons, final double desiredAct, final double mePct) {
        return (int) Math.round(totalNeurons * desiredAct * mePct / 22.5);
    }

    private double generateTimeValue(final int when) {
        // Puff normally starts at time step 651 and lasts to 750
        // Responding in this time window demonstrates memory but not prediction
        if (when >= 651) return 0.5;
        // Ideal are responses in the 551 to 650 window
        if (when >= 551) return 1.0;
        // The 100 ms before that isn't so bad
        if (when >= 451) return 0.8;
        // The 200 ms before that isn't so good
        if (when >= 251) return 0.5;
        // The first few hundred ms is too early
        return 0.0;
    }

    private double generateTraceFitness(final File bufferFile, final double desiredAct, final double timeStep, final double mePct) {
        double retval = 0.0;
        try {
            final int me = calcPtnNeuronNum(2048, desiredAct, mePct);
            final BufferedReader actReader = new BufferedReader(new FileReader(bufferFile));
            String line;
            // Tone neurons are from 1 to me
            // Puff neurons are from me + 1 to 2 * me
            final int firstBlinkNeuron = me + 1;
            final int lastBlinkNeuron = 2 * me;
            int numOn = 0;
            while ((line = actReader.readLine()) != null) {
                final String[] lineData = line.split("\\s");
                final boolean isOn = (Integer.parseInt(lineData[2]) != 0);
                if (isOn) {
                    ++numOn;
                    final int nPos = Integer.parseInt(lineData[1]);
                    if (nPos >= firstBlinkNeuron && nPos <= lastBlinkNeuron) {
                        final int timePos = Integer.parseInt(lineData[0]);
                        retval += generateTimeValue(timePos);
                    }
                }
            }
            actReader.close();
            if (numOn > 1) {
                retval /= numOn;
            }
        }
        catch (final IOException e) {
            System.out.println(e);
        }
        return retval;
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
    public NeuroJetTraceFitness(final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet,
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
    public NeuroJetTraceFitness(final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet) {
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
        Integer dirID;
        synchronized (_counter) {
            dirID = ++_counter;
        }
        final File tempDir = new File(_workingDir, "full_" + String.valueOf(dirID));
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
            final BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                if (DEBUG) {
                    System.out.println(s);
                }
            }
            p.waitFor();
            final BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            final StringBuilder err = new StringBuilder();
            String sErr;
            while ((sErr = stdError.readLine()) != null) {
                err.append(sErr);
            }
            if (err.length() > 0) {
                System.out.println("Errors:\n" + err.toString());
            }
            retval.add(Double.valueOf(System.currentTimeMillis() - start + 1));
            // Read the resulting activity files
            final double desiredAct = _updater.getDesiredAct(genotype);
            final double mePct = _updater.getMePct(genotype);
            final double timeStep = _updater.getTimeStep(genotype);
            final File tstBuff = new File(tempDir, "tstBuff.dat");
            final boolean fileExists = Pause.untilExists(tstBuff, 2000);
            if (!fileExists) {
                throw new IOException("Couldn't find file '" + tstBuff.getPath() + "'");
            }
            final double fitness = generateTraceFitness(tstBuff, desiredAct, timeStep, mePct);
            retval.add(fitness);
            retval.add(Double.valueOf(dirID));
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
        _fitMap.put(individual.clone(), new ArrayList<Double>(retval));
        assert (retval.size() == NUM_FIT_VALS);
        return retval;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Fitness#totalFitness(edu.virginia.cs.geneticalgorithm.Genotype)
     */
    @Override
    public double totalFitness(final Genotype individual) {
        final List<Double> fitVals = fitnessValues(individual);
        return 1 / fitVals.get(0) + fitVals.get(1);
    }
}
