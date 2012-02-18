/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.virginia.cs.geneticalgorithm.gene.Gene;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;
import edu.virginia.cs.geneticalgorithm.gene.StandardGenotype;

/**
 * Generates random individuals
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 16, 2011
 */
public class NeuroJetRandomGenerator {

    private final List<Genotype> _population;
    private final ScriptUpdater _scriptUpdater;
    private final String _randType;
    private final int _seed;

    private static List<Genotype> createPopulation(final int seed, final Gene basis, final int popSize) {
        final List<Genotype> population = new ArrayList<Genotype>();
        final Random _rng = new Random(seed);
        for (int i = 0; i < popSize; ++i) {
            population.add(new StandardGenotype(NeuroJetGeneticAlgorithm.GENOTYPE_SIZE, basis, _rng));
        }
        return population;
    }

    /**
     * Constructor
     * @param seed Random seed to use in generating individuals
     * @param basis Basis gene used to generate population (use default IntervalGene to create uniform distribution)
     * @param randType Description of random distribution (e.g., "Uniform")
     * @param popSize Number of individuals to generate
     */
    public NeuroJetRandomGenerator(final int seed, final Gene basis, final String randType, final int popSize) {
        _seed = seed;
        _randType = randType;
        _population = createPopulation(seed, basis, popSize);
        _scriptUpdater = NeuroJetGeneticAlgorithm.buildScriptUpdater();
    }

    /**
     * Generates the random scripts
     * @throws IOException if an error happens while reading the template script file or writing the generated script file
     */
    public void generateScripts() throws IOException {
        int dirID = _seed - 1;
        int seed = _seed;
        for (final Genotype i : _population) {
            final File tempDir = new File(NeuroJetGeneticAlgorithm.WORKING_DIR, "rand" + _randType + "_" + String.valueOf(++dirID));
            final File script = new File(tempDir, NeuroJetGeneticAlgorithm.SCRIPT_FILE.getName());
            _scriptUpdater.createScriptFromTemplate(script, NeuroJetGeneticAlgorithm.SCRIPT_FILE, (StandardGenotype) i, ++seed);
        }
    }
}
