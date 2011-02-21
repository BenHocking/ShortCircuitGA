/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

import java.util.Collections;
import java.util.List;

/**
 * Composite {@link Fitness} class that only evaluates the second fitness function if the first fitness function returns result(s)
 * exceeding a specified threshold. This is useful for cases where the second fitness function is expensive to evaluate. Note that
 * {@link Genotype} objects that use this fitness function must have clone methods that work.
 * 
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jun 12, 2010
 */
public class ShortCircuitFitness extends AbstractFitness {

    private final ProxyFitness _preFit;
    private final List<Double> _preThreshold;
    private final Fitness _postFit;
    private final int _postFitLen;
    private double _postScale = 1.0; // Amount to scale post fitness by
    final boolean _useThresholdAsLimit = true;

    /**
     * Constructor
     * @param preFit {@link Fitness} function that requires less time to run than postFit and can often act as a reasonable proxy
     * @param preThreshold {@link java.util.List List} of threshold values for multi-objective fitness which must all be passed for
     * the postFit {@link Fitness} function to be evaluated
     * @param postFit {@link Fitness} function that is run if the threshold is met
     */
    public ShortCircuitFitness(final ProxyFitness preFit, final List<Double> preThreshold, final Fitness postFit) {
        this(preFit, preThreshold, postFit, 1);
    }

    /**
     * Constructor
     * @param preFit {@link Fitness} function that requires less time to run than postFit and can often act as a reasonable proxy
     * @param preThreshold {@link java.util.List List} of threshold values for multi-objective fitness which must all be passed for
     * the postFit {@link Fitness} function to be evaluated
     * @param postFit {@link Fitness} function that is run if the threshold is met
     * @param postFitLen Number of fitness values returned by the postFit {@link Fitness} function
     */
    public ShortCircuitFitness(final ProxyFitness preFit, final List<Double> preThreshold, final Fitness postFit,
                               final int postFitLen) {
        _preFit = preFit;
        _preThreshold = preThreshold;
        _postFit = postFit;
        _postFitLen = postFitLen;
    }

    private boolean passedThreshold() {
        boolean passedThreshold = true;
        if (_preThreshold.size() == 1) { // Only comparing against total fitness
            passedThreshold = (_preFit.totalFitness() > _preThreshold.get(0));
        }
        else {
            final List<Double> fitVals = _preFit.fitnessValues();
            if (fitVals.size() > _preThreshold.size())
                throw new IllegalArgumentException("# of fitness values from prefitness function exceeds size of threshold test.");
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
     * @see edu.virginia.cs.geneticalgorithm.Fitness#fitnessValues()
     */
    @Override
    public List<Double> fitnessValues() {
        final List<Double> retval = _preFit.fitnessValues();
        if (passedThreshold()) {
            // Evaluate the post-fitness function
            retval.addAll(_postFit.fitnessValues());
        }
        else {
            if (_postFit instanceof HaltableFitness) {
                ((HaltableFitness) _postFit).halt();
            }
            // Assign zero for the post-fitness function values
            retval.addAll(Collections.nCopies(_postFitLen, 0.0));
        }
        return retval;
    }

    /**
     * Sets the amount to scale the postFit {@link Fitness} function by. This can be used to ensure that the results of the fitness
     * function are more important than its proxy.
     * @param postScale Amount to scale the postFit {@link Fitness} function by
     */
    public void setPostScale(final double postScale) {
        _postScale = postScale;
    }

    /**
     * Returns the amount to scale the postFit {@link Fitness} function by. This can be used to ensure that the results of the
     * fitness function are more important than its proxy.
     * @return Amount to scale the postFit {@link Fitness} function by
     */
    public double getPostScale() {
        return _postScale;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Fitness#totalFitness()
     */
    @Override
    public double totalFitness() {
        // Makes sure fitness is calculated (and possibly gets the values required for checking threshold)
        double retval = _preFit.totalFitness();
        if (passedThreshold()) {
            // In many cases, we want the threshold of the proxy to also be its maximum attainable value. That way, when we run the
            // post-fitness function (because the threshold has been reached), we don't end up giving the proxy value (which is
            // meant to substitute for the post-fitness function after all) too much weight.
            if (_useThresholdAsLimit) {
                retval = _preThreshold.get(0);
            }
            retval += _postScale * _postFit.totalFitness();
        }
        return retval;
    }

    @Override
    public String toString() {
        return "{hash = " + hashCode() + "\n\tpre = " + _preFit + "\n\tthreshold = " + _preThreshold + "\n\tpost = " + _postFit
               + "}";
    }
}
