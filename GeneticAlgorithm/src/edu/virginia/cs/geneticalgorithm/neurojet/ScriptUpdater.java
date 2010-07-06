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
     * 
     * @param genePosition Not used
     * @param varName
     * @param value
     */
    public void addConstantMapping(final int genePosition, final String varName, final String value) {
        _mapping.put(generatePattern(varName), new ConstantGeneInterpreter(value));
    }

    /**
     * 
     * @param genePosition Not used
     * @param varName
     * @param value
     */
    public void addConstantMapping(final int genePosition, final String varName, final Number value) {
        _mapping.put(generatePattern(varName), new ConstantGeneInterpreter(value));
    }

    /**
     * 
     * @param genePosition
     * @param varName
     * @param lowerBound
     * @param upperBound
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

    public void addDesiredActMapping(final int genePosition, final String varName, final double lowerBound, final double upperBound) {
        _desiredActPos = genePosition;
        addDoubleMapping(genePosition, varName, lowerBound, upperBound);
    }

    public void addMePctMapping(final int genePosition, final String varName, final double lowerBound, final double upperBound) {
        _mePctPos = genePosition;
        addDoubleMapping(genePosition, varName, lowerBound, upperBound);
    }

    public void addTimeStepMapping(final int genePosition, final String varName, final double lowerBound, final double upperBound) {
        _timeStepPos = genePosition;
        addDoubleMapping(genePosition, varName, lowerBound, upperBound);
    }

    public void addIntegerMapping(final int genePosition, final String varName, final int lowerBound, final int upperBound) {
        final Pattern p = generatePattern(varName);
        _supportingMap.put(varName, p);
        _mapping.put(p, new SimpleGeneInterpreter(genePosition, new IntegerValueGenerator(lowerBound, upperBound)));
    }

    /**
     * Creates an integer mapping that uses another integer mapping as a lower bound
     * @param genePosition
     * @param varName
     * @param lowerBound
     * @param upperBound
     */
    public void addIntegerMapping(final int genePosition, final String varName, final String lowerBound, final int upperBound) {
        final Pattern lP = _supportingMap.get(lowerBound);
        final GeneInterpreter lower = _mapping.get(lP);
        final GeneInterpreter upper = new ConstantGeneInterpreter(Integer.toString(upperBound));
        _mapping.put(generatePattern(varName), new CompositeGeneInterpreter(genePosition, lower, upper, true));
    }

    public double getDesiredAct(final StandardGenotype genotype) {
        if (_desiredActGenerator == null) return 2; // Assume 2 Hz as the desired activity rate
        final String retvalStr = _desiredActGenerator.generate(((IntervalGene) genotype.get(_desiredActPos)).getValue());
        return Double.parseDouble(retvalStr);
    }

    public double getMePct(final StandardGenotype genotype) {
        if (_mePctGenerator == null) return 0.3; // Assume 0.3 as the fractional percentage
        final String retvalStr = _mePctGenerator.generate(((IntervalGene) genotype.get(_mePctPos)).getValue());
        return Double.parseDouble(retvalStr);
    }

    public double getTimeStep(final StandardGenotype genotype) {
        if (_timeStepGenerator == null) return 1; // Assume 1 ms as the time step size
        final String retvalStr = _timeStepGenerator.generate(((IntervalGene) genotype.get(_timeStepPos)).getValue());
        return Double.parseDouble(retvalStr);
    }

    public void createScriptFromTemplate(final File script, final File template, final StandardGenotype genotype)
            throws IOException {
        final BufferedReader in = new BufferedReader(new FileReader(template));
        final File parentDir = script.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        final PrintStream out = new PrintStream(new FileOutputStream(script));
        String line;
        while ((line = in.readLine()) != null) {
            for (final Pattern p : _mapping.keySet()) {
                final Matcher m = p.matcher(line);
                line = m.replaceAll(_mapping.generateValue(p, genotype));
            }
            out.println(line);
        }
        out.close();
        in.close();
    }

}
