/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.neurojet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import javax.vecmath.GMatrix;

import edu.tufts.cs.geometry.PCA;
import edu.tufts.cs.geometry.PCA.PrincipalComponent;
import edu.virginia.cs.geneticalgorithm.Distribution;
import edu.virginia.cs.geneticalgorithm.DistributionMember;
import edu.virginia.cs.geneticalgorithm.Fitness;
import edu.virginia.cs.geneticalgorithm.GeneticFactory;
import edu.virginia.cs.geneticalgorithm.Genotype;
import edu.virginia.cs.geneticalgorithm.IntervalGene;
import edu.virginia.cs.geneticalgorithm.IntervalGeneticFactory;
import edu.virginia.cs.geneticalgorithm.Reproduction;
import edu.virginia.cs.geneticalgorithm.StandardGenotype;

/**
 * Driver for genetic algorithm exploring NeuroJet space
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 16, 2010
 */
public final class NeuroJetGeneticAlgorithm {

    // TODO: Generalize the location of these File objects
    private final static File NJ = new File("/Users/bhocking/Documents/workspace/NeuroJet/build/NeuroJet");
    private final static File WORKINGDIR = new File("/Users/bhocking/Documents/workspace/GA/scripts");
    private final static int GENOTYPE_SIZE = 21; // 0 - 20
    private final ScriptUpdater _updater;
    private final Fitness _fitnessFn;
    private final GeneticFactory _factory;
    private final Reproduction _reproduction;

    private List<Genotype> _population;

    public NeuroJetGeneticAlgorithm(final int seed, final int popSize) {
        _updater = new ScriptUpdater();
        buildScriptUpdater();
        final List<File> scriptFiles = Collections.singletonList(new File(WORKINGDIR, "trace_quick.nj"));
        _fitnessFn = new NeuroJetQuickFitness(scriptFiles, _updater, NJ, WORKINGDIR);
        _factory = new IntervalGeneticFactory(seed);
        _population = _factory.createPopulation(popSize, GENOTYPE_SIZE);
        final boolean allowDuplicates = true;
        final boolean keepHistory = true;
        _reproduction = new Reproduction(allowDuplicates, keepHistory);
    }

    public void reproduce() {
        _population = _reproduction.reproduce(_population, _fitnessFn, _factory.getSelectFunction(),
                                              _factory.getCrossoverFunction());
    }

    /**
     * For access to historical information
     * @return
     */
    public Reproduction getReproduction() {
        return _reproduction;
    }

    public List<Genotype> getPopulation() {
        return _population;
    }

    private void buildScriptUpdater() {
        _updater.addDoubleMapping(0, "A", 2.0, 8.0); // InternrnExcDecay
        _updater.addIntegerMapping(1, "C", 1, 4); // InternrnAxonalDelay
        _updater.addIntegerMapping(2, "D", 3, 15); // dendFilterWidth
        _updater.addIntegerMapping(3, "E", 1, 5); // minAxDelay
        _updater.addIntegerMapping(4, "F", "E", 7); // maxAxDelay
        _updater.addDesiredActMapping(5, "G", 0.5, 2.5); // Desired activity (Hz)
        _updater.addDoubleMapping(6, "H", 0, 1); // Synaptic failure
        _updater.addDoubleMapping(7, "I", 90, 190); // Off-rate time constant
        _updater.addDoubleMapping(8, "J", 2, 19); // On-rate time constant
        _updater.addDoubleMapping(9, "K", 0, 0.1); // KFB
        _updater.addDoubleMapping(10, "L", 0, 0.1); // KFF
        _updater.addDoubleMapping(11, "M", 0, 0.1); // K0
        _updater.addConstantMapping(-1, "N", 500); // Trace duration (constant)
        _updater.addDoubleMapping(12, "O", 0.005, 0.1); // mu
        _updater.addDoubleMapping(13, "P", 0, 2); // lambda
        _updater.addDoubleMapping(14, "Q", 0.01, 0.03); // IzhA
        _updater.addDoubleMapping(15, "R", -0.1, -0.05); // IzhB
        _updater.addDoubleMapping(16, "S", -70, -50); // IzhC
        _updater.addDoubleMapping(17, "T", 5, 7); // IzhD
        _updater.addDoubleMapping(18, "U", -70, -50); // IzhvStart
        _updater.addDoubleMapping(19, "W", 5, 15); // IzhIMult
        _updater.addDoubleMapping(20, "X", 0.2, 0.5); // mePct
    }

    private void doPCA() {
        final List<Distribution> history = _reproduction.getHistory();
        if (history.isEmpty()) {
            System.out.println("Cannot perform PCA without history.");
            return;
        }
        final Distribution first = history.get(0);
        final int numIndividualsPerGeneration = first.size(); // Needs to be constant across generations
        final int num_rows = history.size() * numIndividualsPerGeneration; // Total number of individuals
        final DistributionMember firstMember = first.get(0);
        final int num_cols = firstMember.getFitnessValues().size() + firstMember.getGenotype().getNumGenes();
        final double[] dmatrix = new double[num_rows * num_cols];
        for (int generation = 0; generation < history.size(); ++generation) {
            final Distribution d = history.get(generation);
            for (int individual = 0; individual < numIndividualsPerGeneration; ++individual) {
                final int row = (generation * numIndividualsPerGeneration) + individual;
                final DistributionMember dm = d.get(individual);
                final List<Double> fitVals = dm.getFitnessValues();
                final StandardGenotype g = (StandardGenotype) dm.getGenotype();
                final int rowOffset = row * num_cols;
                for (int col = 0; col < fitVals.size(); ++col) {
                    dmatrix[rowOffset + col] = fitVals.get(col);
                }
                final int colOffset = fitVals.size();
                for (int col = 0; col < g.getNumGenes(); ++col) {
                    dmatrix[rowOffset + colOffset + col] = ((IntervalGene) g.getGene(col)).getValue();
                }
            }
        }
        final GMatrix gmatrix = new GMatrix(num_rows, num_cols, dmatrix);
        final PrincipalComponent[] pc = PCA.getPrincipalComponents(gmatrix);
        FileOutputStream outFile = null;
        try {
            outFile = new FileOutputStream("pca_results.txt");
            final PrintStream out = new PrintStream(outFile);
            out.println("Number of principal components: " + pc.length);
            for (int i = 0; i < pc.length; ++i) {
                out.println("Strength of pc[" + i + "]: " + pc[i].mag);
            }
            for (int row = 0; row < pc.length; ++row) {
                out.print("pc[" + row + "].vec: [");
                for (int i = 0; i < pc[row].vec.length; ++i) {
                    if (i > 0) out.print(", ");
                    out.print(pc[row].vec[i]);
                }
                out.println("]");
            }
        }
        catch (final IOException e) {
            /* do nothing, for now */
        }
        finally {
            if (outFile != null) try {
                outFile.close();
            }
            catch (final IOException e) { /* do nothing */
            }
        }
    }

    /*
     * @param args
     */
    public static void main(final String[] args) {
        final int pop_size = 100;
        final int num_generations = 50;
        final NeuroJetGeneticAlgorithm nga = new NeuroJetGeneticAlgorithm(1, pop_size);
        for (int i = 0; i < num_generations; ++i) {
            nga.reproduce();
            System.out.println("Generation #" + (i + 1));
            System.out.println("\tBest fit = " + nga.getReproduction().getBestFit());
            System.out.println("\tMean fit = " + nga.getReproduction().getMeanFit());
        }
        nga.doPCA();
    }

}
