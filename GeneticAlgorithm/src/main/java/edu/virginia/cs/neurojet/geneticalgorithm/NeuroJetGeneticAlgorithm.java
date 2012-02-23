/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

// import edu.tufts.cs.geometry.PCA;
// import edu.tufts.cs.geometry.PCA.PrincipalComponent;
import edu.virginia.cs.data.FileLoader;
import edu.virginia.cs.geneticalgorithm.fitness.FitnessFactory;
import edu.virginia.cs.geneticalgorithm.fitness.ShortCircuitFitnessFactory;
import edu.virginia.cs.geneticalgorithm.gene.GeneticFactory;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;
import edu.virginia.cs.geneticalgorithm.gene.IntervalGeneticFactory;
import edu.virginia.cs.geneticalgorithm.mutator.DecayingIntervalMutator;
import edu.virginia.cs.geneticalgorithm.mutator.Mutator;
import edu.virginia.cs.geneticalgorithm.reproduction.Reproduction;
import edu.virginia.cs.geneticalgorithm.select.BonusSelect;
import edu.virginia.cs.geneticalgorithm.select.Select;

/**
 * Driver for genetic algorithm exploring NeuroJet space
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 16, 2010
 */
public final class NeuroJetGeneticAlgorithm {

    // TODO: Generalize the location of these File objects
    static File NJ = new File("/Users/bhocking/Documents/workspace/NeuroJet/build/src/main/c++/NeuroJet");
    static File WORKING_DIR = new File("/Users/bhocking/Documents/workspace/ShortCircuitGA/scripts");
    static File SCRIPT_FILE = null;
    static File PREPARE_FILE = null;
    final static int GENOTYPE_SIZE = 21; // 0 - 20
    private final static double PRE_THRESHOLD = 1e5;
    private final static double POST_SCALE_FACTOR = 0.5 * PRE_THRESHOLD;
    private final FitnessFactory _fitnessFactory;
    private final GeneticFactory _factory;
    private final Reproduction _reproduction;
    private final Select _select;

    private List<Genotype> _population;

    /**
     * Creates a genetic factory that determines how NeuroJet scripts are generated and how they evolve
     * @param seed Random seed to use
     * @return Genetic factory for use with NeuroJet scripts
     */
    public static GeneticFactory createGeneticFactory(final int seed) {
        final double xOverProb = 0.6;
        final double geneXOverProb = 0.25;
        final double mutateProb = 0.03;
        // final double probDecay = -0.0002; // Negative value meaning the mutate probability actually increases
        // (slightly)
        final double probDecay = 0;
        final double mutateSigma = 0.2;
        // final double sigmaDecay = 0.00005; // Sigma decays by about 5% (0.9995^100) every generation
        final double sigmaDecay = 0;
        final Mutator mutator =
                new DecayingIntervalMutator(mutateProb, probDecay, mutateSigma, sigmaDecay, new Random(seed));
        return new IntervalGeneticFactory(seed, xOverProb, geneXOverProb, mutator);
    }

    /**
     * Creates a population of individuals useful for the genetic algorithm, but also usable for generating random
     * scripts
     * @param factory Factor to use to generate the individuals
     * @param seed Random seed to use
     * @param popSize Number of individuals to create
     * @return Population of individuals
     */
    public static List<Genotype> createPopulation(final GeneticFactory factory, final int seed, final int popSize) {
        return factory.createPopulation(popSize, GENOTYPE_SIZE);
    }

    /**
     * Runs a genetic algorithm designed to find settings applicable for running Trace Conditioning experiments using
     * NeuroJet.
     * 
     * @param seed Random seed to use for running the algorithm
     * @param popSize Number of individuals to use in the population.
     * @param useProxy Whether to use NeuroJetQuickFitnessFactory as a proxy when appropriate
     */
    public NeuroJetGeneticAlgorithm(final int seed, final int popSize, final boolean useProxy) {
        final List<File> traceScriptFiles = Collections.singletonList(SCRIPT_FILE);
        final NeuroJetTraceFitnessFactory traceFitnessFactory =
                new NeuroJetTraceFitnessFactory(traceScriptFiles,
                                                buildScriptUpdater(), NJ,
                                                WORKING_DIR, PREPARE_FILE);
        _fitnessFactory =
                useProxy ? new ShortCircuitFitnessFactory(new NeuroJetQuickFitnessFactory(traceFitnessFactory),
                                                          Collections.singletonList(PRE_THRESHOLD),
                                                          NeuroJetTraceFitness.NUM_FIT_VALS) : traceFitnessFactory;
        ((ShortCircuitFitnessFactory) _fitnessFactory).setPostScale(POST_SCALE_FACTOR);
        // _fitnessFactory = traceFitnessFactory;
        _factory = createGeneticFactory(seed);
        _population = createPopulation(_factory, seed, popSize);
        final boolean allowDuplicates = false;
        final boolean keepHistory = true;
        _reproduction = new Reproduction(allowDuplicates, keepHistory);
        _reproduction.setEndPrepareAction(PREPARE_FILE);
        _reproduction.setNumElites(Math.round(popSize * 0.1f));
        final List<Double> ranking =
                Arrays.asList(0.0, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.2, 0.2, 0.6, 0.6, 1.4, 1.4, 3.0);
        // final List<Double> ranking = Arrays.asList(1.0, 0.5);
        _select = new BonusSelect(new Random(seed), ranking);
    }

    /**
     * Uses an existing population to find the next generation of the population.
     */
    public void reproduce() {
        _population = _reproduction.reproduce(_population, _fitnessFactory, _select, _factory.getCrossoverFunction());
    }

    /**
     * For access to historical information
     * @return Reference to this genetic algorithms {@link edu.virginia.cs.geneticalgorithm.reproduction.Reproduction
     *         Reproduction} object
     */
    public Reproduction getReproduction() {
        return _reproduction;
    }

    /**
     * @return ScriptUpdater that replaces wild cards with parameter values
     */
    public static ScriptUpdater buildScriptUpdater() {
        final ScriptUpdater updater = new ScriptUpdater();
        updater.addDoubleMapping(0, "A", 2.0, 8.0); // InternrnExcDecay
        updater.addIntegerMapping(1, "C", 1, 4); // InternrnAxonalDelay
        updater.addIntegerMapping(2, "D", 3, 15); // dendFilterWidth
        updater.addIntegerMapping(3, "E", 1, 5); // minAxDelay
        updater.addIntegerMapping(4, "F", "E", 7); // maxAxDelay
        updater.addDesiredActMapping(5, "G", 1, 5); // Desired activity (Hz)
        updater.addDoubleMapping(6, "H", 0, 1); // Synaptic failure
        updater.addDoubleMapping(7, "I", 90, 190); // Off-rate time constant
        updater.addDoubleMapping(8, "J", 2, 19); // On-rate time constant
        updater.addDoubleMapping(9, "K", 0, 0.1); // KFB
        updater.addDoubleMapping(10, "L", 0, 0.1); // KFF
        updater.addDoubleMapping(11, "M", 0, 0.1); // K0
        updater.addConstantMapping(-1, "N", 500); // Trace duration (constant)
        updater.addDoubleMapping(12, "O", 0.005, 0.1); // mu
        updater.addDoubleMapping(13, "P", 0, 2); // lambda
        updater.addDoubleMapping(14, "Q", 0.01, 0.03); // IzhA
        updater.addDoubleMapping(15, "R", -0.1, -0.05); // IzhB
        updater.addDoubleMapping(16, "S", -70, -50); // IzhC
        updater.addDoubleMapping(17, "T", 5, 7); // IzhD
        updater.addDoubleMapping(18, "U", -70, -50); // IzhvStart
        updater.addDoubleMapping(19, "W", 5, 15); // IzhIMult
        updater.addMePctMapping(20, "X", 0.2, 0.5); // mePct
        return updater;
    }

    /*
     * private void doPCA() { final List<Distribution> history = _reproduction.getHistory(); if (history.isEmpty()) {
     * throw new RuntimeException("Cannot perform PCA without history."); } final Distribution first = history.get(0);
     * final int numIndividualsPerGeneration = first.size(); // Needs to be constant across generations final int
     * num_rows = history.size() * numIndividualsPerGeneration; // Total number of individuals if (num_rows < 2) { throw
     * new RuntimeException("Cannot perform PCA without at least two rows of data."); } final DistributionMember
     * firstMember = first.get(0); final int num_cols = firstMember.getFitnessValues().size() +
     * firstMember.getGenotype().getNumGenes(); final double[] dmatrix = new double[num_rows * num_cols]; for (int
     * generation = 0; generation < history.size(); ++generation) { final Distribution d = history.get(generation); for
     * (int individual = 0; individual < numIndividualsPerGeneration; ++individual) { final int row = (generation *
     * numIndividualsPerGeneration) + individual; final DistributionMember dm = d.get(individual); final List<Double>
     * fitVals = dm.getFitnessValues(); final StandardGenotype g = (StandardGenotype) dm.getGenotype(); final int
     * rowOffset = row * num_cols; for (int col = 0; col < fitVals.size(); ++col) { dmatrix[rowOffset + col] =
     * fitVals.get(col); } final int colOffset = fitVals.size(); for (int col = 0; col < g.getNumGenes(); ++col) {
     * dmatrix[rowOffset + colOffset + col] = ((IntervalGene) g.getGene(col)).getValue(); } } } final GMatrix gmatrix =
     * new GMatrix(num_rows, num_cols, dmatrix); final PrincipalComponent[] pc = PCA.getPrincipalComponents(gmatrix);
     * FileOutputStream outFile = null; try { outFile = new FileOutputStream("pca_results.txt"); final PrintStream out =
     * new PrintStream(outFile); out.println("Number of principal components: " + pc.length); for (int i = 0; i <
     * pc.length; ++i) { out.println("Strength of pc[" + i + "]: " + pc[i].mag); } for (int row = 0; row < pc.length;
     * ++row) { out.print("pc[" + row + "].vec: ["); for (int i = 0; i < pc[row].vec.length; ++i) { if (i > 0)
     * out.print(", "); out.print(pc[row].vec[i]); } out.println("]"); } } catch (final IOException e) { /* do nothing,
     * for now
     *//*
        * } finally { if (outFile != null) try { outFile.close(); } catch (final IOException e) { /* do nothing
        *//*
           * } } }
           */

    /**
     * @param args Ignored
     * @throws IOException If specified config file does not exist or cannot be read
     * @throws URISyntaxException If file or directory name specified in config file is invalid
     */
    public static void main(final String[] args) throws IOException, URISyntaxException {
        final Properties configFile = new Properties();
        // Load default property values
        final File NJGAProp =
                new File(NeuroJetGeneticAlgorithm.class.getResource("NeuroJetGeneticAlgorithm.properties").getFile());
        if (NJGAProp.exists()) {
            configFile.load(new FileInputStream(NJGAProp));
        }
        if (args.length > 0) {
            final InputStream inStream = new FileInputStream(new File(args[0]));
            configFile.load(inStream);
        }
        final int pop_size = Integer.valueOf(configFile.getProperty("POP_SIZE", "100"));
        final int num_generations = Integer.valueOf(configFile.getProperty("NUM_GENERATIONS", "150"));
        final int seed = Integer.valueOf(configFile.getProperty("SEED", "101"));
        NJ = new File(configFile.getProperty("NJ", NJ.getPath()));
        if (!NJ.canExecute())
            throw new IllegalArgumentException("Property NJ ('" + NJ.getAbsolutePath()
                                               + "') must refer to an executable");
        WORKING_DIR = FileLoader.getFileFromProperty(configFile, "WORKING_DIR", WORKING_DIR.getPath());
        if (!WORKING_DIR.exists()) {
            WORKING_DIR.mkdir();
        }
        SCRIPT_FILE = FileLoader.getFileFromProperty(configFile, "SCRIPT_FILE",
                                                     new File(WORKING_DIR, "trace_full.nj").getPath());
        if (!SCRIPT_FILE.exists()) {
            throw new IllegalArgumentException("Property SCRIPT_FILE ('" + SCRIPT_FILE.getAbsolutePath()
                                               + "') must refer to an existing file");
        }
        final String prepareFileName = configFile.getProperty("PREPARE_FILE");
        if (prepareFileName != null && !prepareFileName.isEmpty()) {
            PREPARE_FILE = new File(prepareFileName);
            if (!PREPARE_FILE.canExecute())
                throw new IllegalArgumentException("Property PREPARE_FILE ('" + NJ.getAbsolutePath()
                                                   + "') must refer to an executable if it is not blank");
        }
        final boolean useProxy = Boolean.valueOf(configFile.getProperty("USE_PROXY", "true"));
        final NeuroJetGeneticAlgorithm nga = new NeuroJetGeneticAlgorithm(seed, pop_size, useProxy);
        for (int i = 0; i < num_generations; ++i) {
            nga.reproduce();
            System.out.println("Generation #" + (i + 1));
            System.out.print("\tBest fit = [");
            final List<Double> bestFitList = nga.getReproduction().getBestFit();
            for (int j = 1; j < bestFitList.size(); ++j) {
                if (j > 1) System.out.print(", ");
                System.out.print(bestFitList.get(j));
            }
            System.out.println("] => " + bestFitList.get(0));
            System.out.println("\tMean fit = " + nga.getReproduction().getMeanFit());
        }
        // nga.doPCA();
    }
}
