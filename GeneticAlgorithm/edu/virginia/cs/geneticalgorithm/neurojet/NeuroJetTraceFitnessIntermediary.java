/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.neurojet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.virginia.cs.common.utils.ArrayNumberUtils;
import edu.virginia.cs.geneticalgorithm.Fitness;
import edu.virginia.cs.geneticalgorithm.Genotype;
import edu.virginia.cs.geneticalgorithm.StandardGenotype;

/**
 * Calculates a short-circuit fitness to determine whether this is a reasonable choice of parameter settings
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public final class NeuroJetTraceFitnessIntermediary {

    static final int NUM_FIT_VALS = 2; // Run time + trace score

    /**
     * Whether or not to delete working files when finished
     */
    public static boolean DELETE_WORKING_FILES = false;
    // Made package just to avoid warnings about dead code
    static final boolean DEBUG = false;

    private static Integer _counter = 0;

    final File _mainFile;
    final File _workingDir;
    final List<File> _scriptFiles;
    final ScriptUpdater _updater;
    final File _neuroJet;
    private final int _maxSamples;
    private final List<NeuroJetTraceFitness> _instances;
    private List<Double> _summedFitnessValues;
    private double _summedTotalFitness;
    private final NeuroJetTraceFitnessFactory _parent;
    private final StandardGenotype _genotype;
    private static final Map<Genotype, NeuroJetTraceFitnessIntermediary> _fitMap = new HashMap<Genotype, NeuroJetTraceFitnessIntermediary>();

    static Fitness createFitness(final NeuroJetTraceFitnessFactory factory, final StandardGenotype individual,
                                 final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet,
                                 final File workingDir, final int maxSamples) {
        final Genotype normalizedGenotype = updater.normalizeGenotype(individual);
        NeuroJetTraceFitnessIntermediary intermediary = _fitMap.get(normalizedGenotype);
        if (intermediary == null) {
            _fitMap.put(normalizedGenotype, new NeuroJetTraceFitnessIntermediary(factory, individual, scriptFiles, updater,
                                                                                 neuroJet, workingDir, maxSamples));
            intermediary = _fitMap.get(normalizedGenotype);
        }
        return intermediary.createFitness();
    }

    Fitness createFitness() {
        if (_instances.size() < _maxSamples) {
            _instances.add(new NeuroJetTraceFitness(this, ++_counter));
        }
        return _instances.get(_instances.size() - 1);
    }

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
    private NeuroJetTraceFitnessIntermediary(final NeuroJetTraceFitnessFactory parent, final StandardGenotype individual,
                                             final List<File> scriptFiles, final ScriptUpdater updater, final File neuroJet,
                                             final File workingDir, final int maxSamples) {
        if (scriptFiles == null || scriptFiles.size() == 0)
            throw new IllegalArgumentException("Argument scriptFiles cannot be null or empty");
        if (neuroJet == null || !neuroJet.canExecute())
            throw new IllegalArgumentException("Argument neuroJet must refer to an executable");
        _parent = parent;
        _genotype = individual;
        _mainFile = scriptFiles.get(0);
        _scriptFiles = scriptFiles;
        _updater = updater;
        _neuroJet = neuroJet;
        _maxSamples = maxSamples;
        _instances = new ArrayList<NeuroJetTraceFitness>();
        _summedFitnessValues = new ArrayList<Double>();
        for (int i = 0; i < NUM_FIT_VALS; ++i) {
            _summedFitnessValues.add(0.0);
        }
        _summedTotalFitness = 0;
        _workingDir = workingDir != null ? workingDir : _mainFile.getParentFile();
    }

    NeuroJetTraceFitnessFactory getParent() {
        return _parent;
    }

    void addToSummedFitnessValues(final List<Double> listToAdd, final double totalToAdd) {
        _summedFitnessValues = ArrayNumberUtils.add(_summedFitnessValues, listToAdd);
        _summedTotalFitness += totalToAdd;
    }

    List<Double> getMeanFitnessValues() {
        return ArrayNumberUtils.divide(_summedFitnessValues, _instances.size());
    }

    double getMeanTotalFitness() {
        return _summedTotalFitness / _instances.size();
    }

    /**
     * @return The desired activity based off its previously defined generation technique
     * @see ScriptUpdater#getDesiredAct(StandardGenotype)
     */
    public double getDesiredAct() {
        return _updater.getDesiredAct(_genotype);
    }

    /**
     * @return The desired percentage of activity due to externals based off its previously defined generation technique
     * @see ScriptUpdater#getMePct(StandardGenotype)
     */
    public double getMePct() {
        return _updater.getMePct(_genotype);
    }

    /**
     * @return The desired activity based off its previously defined generation technique
     * @see ScriptUpdater#getTimeStep(StandardGenotype)
     */
    public double getTimeStep() {
        return _updater.getTimeStep(_genotype);
    }

    StandardGenotype getGenotype() {
        return _genotype;
    }
}
