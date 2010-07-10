/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.neurojet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.virginia.cs.common.DoubleValueGenerator;
import edu.virginia.cs.common.IntegerValueGenerator;
import edu.virginia.cs.common.ValueGenerator;
import edu.virginia.cs.geneticalgorithm.CompositeGeneInterpreter;
import edu.virginia.cs.geneticalgorithm.ConstantGeneInterpreter;
import edu.virginia.cs.geneticalgorithm.GeneInterpreter;
import edu.virginia.cs.geneticalgorithm.GeneInterpreterMap;
import edu.virginia.cs.geneticalgorithm.IntervalGene;
import edu.virginia.cs.geneticalgorithm.SimpleGeneInterpreter;
import edu.virginia.cs.geneticalgorithm.StandardGenotype;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 2, 2010
 */
public final class ScriptUpdater {

    private final GeneInterpreterMap<Pattern> _mapping = new GeneInterpreterMap<Pattern>();
    private final Map<String, Pattern> _supportingMap = new HashMap<String, Pattern>();
    private ValueGenerator _desiredActGenerator = null; // So we'll know if this somehow never got set
    private int _desiredActPos = -1;
    private ValueGenerator _mePctGenerator = null; // So we'll know if this somehow never got set
    private int _mePctPos = -1;
    private ValueGenerator _timeStepGenerator = null; // So we'll know if this somehow never got set
    private int _timeStepPos = -1;

    private Pattern generatePattern(final String varName) {
        return Pattern.compile("insert" + varName + "here", Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);
    }

    /**
     * Maps a variable to a constant {@link java.lang.String String} value
     * @param genePosition Not used
     * @param varName Variable that will be replaced in the NeuroJet script(s).
     * @param value {@link java.lang.String String} to replace the variable with
     */
    public void addConstantMapping(final int genePosition, final String varName, final String value) {
        _mapping.put(generatePattern(varName), new ConstantGeneInterpreter(value));
    }

    /**
     * Maps a variable to a constant {@link java.lang.Number Number} value
     * @param genePosition Not used
     * @param varName Variable that will be replaced in the NeuroJet script(s).
     * @param value {@link java.lang.Number Number} to replace the variable with
     */
    public void addConstantMapping(final int genePosition, final String varName, final Number value) {
        _mapping.put(generatePattern(varName), new ConstantGeneInterpreter(value));
    }

    /**
     * Maps a variable to a double value between a lower and upper bound, depending on a
     * {@link edu.virginia.cs.geneticalgorithm.Gene Gene} in a {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param genePosition Where the {@link edu.virginia.cs.geneticalgorithm.Gene Gene} is located in its
     * {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param varName Variable that will be replaced in the NeuroJet script(s).
     * @param lowerBound Lowest possible value the variable can attain
     * @param upperBound Greatest possible value the variable can attain
     */
    public void addDoubleMapping(final int genePosition, final String varName, final double lowerBound, final double upperBound) {
        final ValueGenerator generator = new DoubleValueGenerator(lowerBound, upperBound);
        final Pattern p = generatePattern(varName);
        _supportingMap.put(varName, p);
        _mapping.put(p, new SimpleGeneInterpreter(genePosition, generator));
        if (genePosition == _desiredActPos) {
            _desiredActGenerator = generator;
        }
        else if (genePosition == _timeStepPos) {
            _timeStepGenerator = generator;
        }
        else if (genePosition == _mePctPos) {
            _mePctGenerator = generator;
        }
    }

    /**
     * Maps the desired activity variable to a double value between a lower and upper bound, depending on a
     * {@link edu.virginia.cs.geneticalgorithm.Gene Gene} in a {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param genePosition Where the {@link edu.virginia.cs.geneticalgorithm.Gene Gene} is located in its
     * {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param varName Variable that will be replaced in the NeuroJet script(s).
     * @param lowerBound Lowest possible value the variable can attain
     * @param upperBound Greatest possible value the variable can attain
     * @see #getDesiredAct(StandardGenotype)
     */
    public void addDesiredActMapping(final int genePosition, final String varName, final double lowerBound, final double upperBound) {
        _desiredActPos = genePosition;
        addDoubleMapping(genePosition, varName, lowerBound, upperBound);
    }

    /**
     * Maps the fractional activity of driven input to a double value between a lower and upper bound, depending on a
     * {@link edu.virginia.cs.geneticalgorithm.Gene Gene} in a {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param genePosition Where the {@link edu.virginia.cs.geneticalgorithm.Gene Gene} is located in its
     * {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param varName Variable that will be replaced in the NeuroJet script(s).
     * @param lowerBound Lowest possible value the variable can attain
     * @param upperBound Greatest possible value the variable can attain
     * @see #getMePct(StandardGenotype)
     */
    public void addMePctMapping(final int genePosition, final String varName, final double lowerBound, final double upperBound) {
        _mePctPos = genePosition;
        addDoubleMapping(genePosition, varName, lowerBound, upperBound);
    }

    /**
     * Maps the timestep size variable to a double value between a lower and upper bound, depending on a
     * {@link edu.virginia.cs.geneticalgorithm.Gene Gene} in a {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param genePosition Where the {@link edu.virginia.cs.geneticalgorithm.Gene Gene} is located in its
     * {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param varName Variable that will be replaced in the NeuroJet script(s).
     * @param lowerBound Lowest possible value the variable can attain
     * @param upperBound Greatest possible value the variable can attain
     * @see #getTimeStep(StandardGenotype)
     */
    public void addTimeStepMapping(final int genePosition, final String varName, final double lowerBound, final double upperBound) {
        _timeStepPos = genePosition;
        addDoubleMapping(genePosition, varName, lowerBound, upperBound);
    }

    /**
     * Maps a variable to a integer value between a lower and upper bound, depending on a
     * {@link edu.virginia.cs.geneticalgorithm.Gene Gene} in a {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param genePosition Where the {@link edu.virginia.cs.geneticalgorithm.Gene Gene} is located in its
     * {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param varName Variable that will be replaced in the NeuroJet script(s).
     * @param lowerBound Lowest possible value the variable can attain
     * @param upperBound Greatest possible value the variable can attain
     */
    public void addIntegerMapping(final int genePosition, final String varName, final int lowerBound, final int upperBound) {
        final Pattern p = generatePattern(varName);
        _supportingMap.put(varName, p);
        _mapping.put(p, new SimpleGeneInterpreter(genePosition, new IntegerValueGenerator(lowerBound, upperBound)));
    }

    /**
     * Creates an integer mapping that uses another integer mapping as a lower bound
     * @param genePosition Where the {@link edu.virginia.cs.geneticalgorithm.Gene Gene} is located in its
     * {@link edu.virginia.cs.geneticalgorithm.Genotype Genotype}
     * @param varName Variable that will be replaced in the NeuroJet script(s).
     * @param lowerBound Lowest possible value the variable can attain
     * @param upperBound Greatest possible value the variable can attain
     */
    public void addIntegerMapping(final int genePosition, final String varName, final String lowerBound, final int upperBound) {
        final Pattern lP = _supportingMap.get(lowerBound);
        final GeneInterpreter lower = _mapping.get(lP);
        final GeneInterpreter upper = new ConstantGeneInterpreter(Integer.toString(upperBound));
        _mapping.put(generatePattern(varName), new CompositeGeneInterpreter(genePosition, lower, upper, true));
    }

    /**
     * Calculates the desired activity based off its previously defined generation technique, and returns that value. Defaults to 2
     * Hz if no generator has been defined for it.
     * @param genotype {@link edu.virginia.cs.geneticalgorithm.StandardGenotype StandardGenotype} that contains
     * {@link edu.virginia.cs.geneticalgorithm.Gene Genes} used for generation
     * @return The desired activity based off its previously defined generation technique
     * @see #addDesiredActMapping(int, String, double, double)
     */
    public double getDesiredAct(final StandardGenotype genotype) {
        if (_desiredActGenerator == null) return 2; // Assume 2 Hz as the desired activity rate
        final String retvalStr = _desiredActGenerator.generate(((IntervalGene) genotype.get(_desiredActPos)).getValue());
        return Double.parseDouble(retvalStr);
    }

    /**
     * Calculates the fractional activity of driven input based off its previously defined generation technique, and returns that
     * value. Defaults to 0.3 (30%) if no generator has been defined for it.
     * @param genotype {@link edu.virginia.cs.geneticalgorithm.StandardGenotype StandardGenotype} that contains
     * {@link edu.virginia.cs.geneticalgorithm.Gene Genes} used for generation
     * @return The desired activity based off its previously defined generation technique
     * @see #addMePctMapping(int, String, double, double)
     */
    public double getMePct(final StandardGenotype genotype) {
        if (_mePctGenerator == null) return 0.3; // Assume 0.3 as the fractional percentage
        final String retvalStr = _mePctGenerator.generate(((IntervalGene) genotype.get(_mePctPos)).getValue());
        return Double.parseDouble(retvalStr);
    }

    /**
     * Calculates the timestep size based off its previously defined generation technique, and returns that value. Defaults to 1 if
     * no generator has been defined for it.
     * @param genotype {@link edu.virginia.cs.geneticalgorithm.StandardGenotype StandardGenotype} that contains
     * {@link edu.virginia.cs.geneticalgorithm.Gene Genes} used for generation
     * @return The desired activity based off its previously defined generation technique
     * @see #addTimeStepMapping(int, String, double, double)
     */
    public double getTimeStep(final StandardGenotype genotype) {
        if (_timeStepGenerator == null) return 1; // Assume 1 ms as the time step size
        final String retvalStr = _timeStepGenerator.generate(((IntervalGene) genotype.get(_timeStepPos)).getValue());
        return Double.parseDouble(retvalStr);
    }

    /**
     * Replaces all mapped variables with their mapped values in the specified script.
     * @param script {@link java.io.File File} to create containing the mapped values.
     * @param template {@link java.io.File File} containing the variables to be replaced with their mapped values.
     * @param genotype {@link edu.virginia.cs.geneticalgorithm.StandardGenotype StandardGenotype} that contains
     * {@link edu.virginia.cs.geneticalgorithm.Gene Genes} used for generation
     * @throws IOException If a problem is encountered in either reading the template {@link java.io.File File} or creating the
     * script {@link java.io.File File}.
     */
    public void createScriptFromTemplate(final File script, final File template, final StandardGenotype genotype)
            throws IOException {
        BufferedReader in = null;
        PrintStream out = null;
        try {
            in = new BufferedReader(new FileReader(template));

            final File parentDir = script.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            out = new PrintStream(new FileOutputStream(script));
            String line;
            while ((line = in.readLine()) != null) {
                for (final Pattern p : _mapping.keySet()) {
                    final Matcher m = p.matcher(line);
                    line = m.replaceAll(_mapping.generateValue(p, genotype));
                }
                out.println(line);
            }
        }
        finally {
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }
}
