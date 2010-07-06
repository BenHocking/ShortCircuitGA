/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Composite {@link Fitness} class that only evaluates the second fitness function if the first fitness function returns result(s)
 * exceeding a specified threshold. This is useful for cases where the second fitness function is expensive to evaluate. Note that
 * {@link Genotype} objects that use this fitness function must have clone methods that work.
 * 
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jun 12, 2010
 */
public final class ShortCircuitFitness extends AbstractFitness {

    private final Fitness _preFit;
    private final List<Double> _preThreshold;
    private final Fitness _postFit;
    private final int _postFitLen;
    private final boolean _useTotalFitness;
    private double _postScale = 1.0; // Amount to scale post fitness by
    private final Map<Genotype, List<Double>> _fitMap = new HashMap<Genotype, List<Double>>();
    final boolean _useThresholdAsLimit = true;

    /**
     * 
     * @param preFit
     * @param preThreshold
     * @param postFit
     */
    public ShortCircuitFitness(final Fitness preFit, final List<Double> preThreshold, final Fitness postFit) {
        this(preFit, preThreshold, postFit, 1);
    }

    /**
     * 
     * @param preFit
     * @param preThreshold
     * @param postFit
     * @param postFitLen
     */
    public ShortCircuitFitness(final Fitness preFit, final List<Double> preThreshold, final Fitness postFit, final int postFitLen) {
        _preFit = preFit;
        _preThreshold = preThreshold;
        _postFit = postFit;
        _postFitLen = postFitLen;
        _useTotalFitness = false;
    }

    /**
     * 
     * @param preFit
     * @param preThreshold
     * @param postFit
     */
    public ShortCircuitFitness(final Fitness preFit, final double preThreshold, final Fitness postFit) {
        this(preFit, preThreshold, postFit, 1);
    }

    /**
     * 
     * @param preFit
     * @param preThreshold
     * @param postFit
     * @param postFitLen
     */
    public ShortCircuitFitness(final Fitness preFit, final double preThreshold, final Fitness postFit, final int postFitLen) {
        _preFit = preFit;
        _preThreshold = Collections.singletonList(preThreshold);
        _postFit = postFit;
        _postFitLen = postFitLen;
        _useTotalFitness = true;
    }

    private boolean passedThreshold(final List<Double> fitVals, final Genotype individual) {
        boolean passedThreshold = true;
        if (_useTotalFitness) {
            passedThreshold = (_preFit.totalFitness(individual) > _preThreshold.get(0));
        }
        else {
            if (fitVals.size() > _preThreshold.size())
                throw new RuntimeException("Number of fitness values from first fitness function exceeds size of threshold test.");
            for (int i = 0; i < fitVals.size(); ++i) {
                if (fitVals.get(i) < _preThreshold.get(i)) {
                    passedThreshold = false;
                    break;
                }
            }
        }
        return passedThreshold;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Fitness#fitnessValues(edu.virginia.cs.geneticalgorithm.Genotype)
     */
    @Override
    public List<Double> fitnessValues(final Genotype individual) {
        List<Double> retval = _fitMap.get(individual);
        if (retval != null) return retval;
        retval = _preFit.fitnessValues(individual);
        if (passedThreshold(retval, individual)) {
            // Evaluate the post-fitness function
            retval.addAll(_postFit.fitnessValues(individual));
        }
        else {
            // Assign zero for the post-fitness function values
            retval.addAll(Collections.nCopies(_postFitLen, 0.0));
        }
        // Create copy in case genotype is changed after the fitness has been calculated
        _fitMap.put(individual.clone(), retval);
        return retval;
    }

    public void setPostScale(final double postScale) {
        _postScale = postScale;
    }

    public double getPostScale() {
        return _postScale;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Fitness#totalFitness(edu.virginia.cs.geneticalgorithm.Genotype)
     */
    @Override
    public double totalFitness(final Genotype individual) {
        // Makes sure fitness is calculated (and possibly gets the values required for checking threshold)
        final List<Double> fitVals = fitnessValues(individual);
        double retval = _preFit.totalFitness(individual);
        if (passedThreshold(fitVals, individual)) {
            // TODO Document
            if (_useThresholdAsLimit) {
                retval = _preThreshold.get(0);
            }
            retval += _postScale * _postFit.totalFitness(individual);
        }
        return retval;
    }

}
