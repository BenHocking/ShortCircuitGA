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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.virginia.cs.common.DoubleValueGenerator;
import edu.virginia.cs.common.IntegerValueGenerator;
import edu.virginia.cs.geneticalgorithm.GeneInterpreterMap;
import edu.virginia.cs.geneticalgorithm.IntervalGene;
import edu.virginia.cs.geneticalgorithm.StandardGenotype;

/**
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 2, 2010
 */
public class ScriptUpdater {

    private final GeneInterpreterMap<Pattern> _mapping = new GeneInterpreterMap<Pattern>();
    private DoubleValueGenerator _desiredActGenerator = null; // So we'll know if this somehow never got set
    private int _desiredActPos = 0;
    private DoubleValueGenerator _timeStepGenerator = null; // So we'll know if this somehow never got set
    private int _timeStepPos = 0;

    private Pattern generatePattern(final String varName) {
        return Pattern.compile("insert" + varName + "here", Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);
    }

    public void addMapping(final int genePosition, final String varName, final double lowerBound, final double upperBound) {
        _mapping.put(generatePattern(varName), new DoubleValueGenerator(lowerBound, upperBound), genePosition);
    }

    public void addDesiredActMapping(final int genePosition, final String varName, final double lowerBound, final double upperBound) {
        _desiredActGenerator = new DoubleValueGenerator(lowerBound, upperBound);
        _desiredActPos = genePosition;
        _mapping.put(generatePattern(varName), _desiredActGenerator, genePosition);
    }

    public void addTimeStepMapping(final int genePosition, final String varName, final double lowerBound, final double upperBound) {
        _timeStepGenerator = new DoubleValueGenerator(lowerBound, upperBound);
        _timeStepPos = genePosition;
        _mapping.put(generatePattern(varName), _timeStepGenerator, genePosition);
    }

    public void addMapping(final int genePosition, final String varName, final int lowerBound, final int upperBound) {
        _mapping.put(generatePattern(varName), new IntegerValueGenerator(lowerBound, upperBound), genePosition);
    }

    public double getDesiredAct(final StandardGenotype genotype) {
        if (_desiredActGenerator == null) return 2; // Assume 2 Hz as the desired activity rate
        final String retvalStr = _desiredActGenerator.generate(((IntervalGene) genotype.get(_desiredActPos)).getValue());
        return Double.parseDouble(retvalStr);
    }

    public double getTimeStep(final StandardGenotype genotype) {
        if (_desiredActGenerator == null) return 1; // Assume 1 ms as the time step size
        final String retvalStr = _timeStepGenerator.generate(((IntervalGene) genotype.get(_timeStepPos)).getValue());
        return Double.parseDouble(retvalStr);
    }

    public void createScriptFromTemplate(final File script, final File template, final StandardGenotype genotype)
            throws IOException {
        final BufferedReader in = new BufferedReader(new FileReader(template));
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
