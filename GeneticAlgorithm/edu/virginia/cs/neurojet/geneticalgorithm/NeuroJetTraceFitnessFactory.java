/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import java.io.File;
import java.util.List;

import edu.virginia.cs.geneticalgorithm.fitness.Fitness;
import edu.virginia.cs.geneticalgorithm.fitness.FitnessFactory;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;
import edu.virginia.cs.geneticalgorithm.gene.StandardGenotype;

/**
 * Factory to generate a fitness function to determine whether this is a reasonable choice of parameter settings for learning trace
 * conditioning
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Nov 20, 2010
 */
public class NeuroJetTraceFitnessFactory implements FitnessFactory {

    private final File _mainFile;
    private final File _workingDir;
    private final List<File> _scriptFiles;
    private final ScriptUpdater _updater;
    private final File _neuroJet;

    /**
     * Constructor
     * @param scriptFiles {@link java.util.List List} of script files that NeuroJet needs to run the experiment. The first file in
     * the list is the one actually run, with the other files presumably a dependency of it.
     * @param updater {@link ScriptUpdater} that contains the parameter values to map to the
     * {@link edu.virginia.cs.geneticalgorithm.gene.Genotype Genotype}.
     * @param neuroJet Location of the NeuroJet executable
     * @param workingDir Directory that subdirectories will be created off (if null, this uses the location of the first file in the
     * scriptFiles {@link java.util.List List}).
     */
    public NeuroJetTraceFitnessFactory(final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet,
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
     * {@link edu.virginia.cs.geneticalgorithm.gene.Genotype Genotype}.
     * @param neuroJet Location of the NeuroJet executable
     */
    public NeuroJetTraceFitnessFactory(final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet) {
        this(scriptFiles, updater, neuroJet, null);
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.FitnessFactory#createFitness(edu.virginia.cs.geneticalgorithm.gene.Genotype)
     */
    @Override
    public Fitness createFitness(final Genotype individual) {
        if (!(individual instanceof StandardGenotype))
            throw new IllegalArgumentException("individual must be of type StandardGenotype");
        return NeuroJetTraceFitnessIntermediary.createFitness(this, (StandardGenotype) individual, _scriptFiles, _updater,
                                                              _neuroJet, _workingDir, 1);
    }

    File getMainFile() {
        return _mainFile;
    }

    File getWorkingDir() {
        return _workingDir;
    }

    List<File> getScriptFiles() {
        return _scriptFiles;
    }

    ScriptUpdater getUpdater() {
        return _updater;
    }

    File getNeuroJet() {
        return _neuroJet;
    }
}
