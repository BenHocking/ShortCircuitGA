package edu.virginia.cs.geneticalgorithm.mutator;

import java.util.Random;

import edu.virginia.cs.geneticalgorithm.gene.Gene;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;
import edu.virginia.cs.geneticalgorithm.gene.IntervalGene;

/**
 * Standard {@link Mutator} class.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public final class DecayingIntervalMutator implements Mutator {

    private double _mutateRate;
    private final double _rateDecay;
    private double _mutateSigma;
    private final double _sigmaDecay;
    private final Random _rng;

    /**
     * Constructor that fully specifies a typical {@link Mutator} object for an interval gene
     * @param mutateRate Probability (0 to 1) that a {@link Gene} will mutate
     * @param rateDecay Rate at which the mutate rate decays. A negative value will result in the mutation rate increasing.
     * @param mutateSigma Standard deviation used when mutating {@link IntervalGene IntervalGenes}.
     * @param sigmaDecay Rate at which the standard deviation decays.
     * @param rng Random number generator used with probabilities
     */
    public DecayingIntervalMutator(final double mutateRate, final double rateDecay, final double mutateSigma,
                                   final double sigmaDecay, final Random rng) {
        _mutateRate = mutateRate;
        _rateDecay = rateDecay;
        _mutateSigma = mutateSigma;
        _sigmaDecay = sigmaDecay;
        _rng = rng;
    }

    /**
     * Sets the mutation probability for this functor
     * @param mutateRate Probability (0 to 1) that a {@link Gene} will mutate
     */
    public void setMutateRate(final double mutateRate) {
        _mutateRate = mutateRate;
    }

    /**
     * Gets the mutation probability for this functor
     * @return Mutation probability (0 to 1)
     */
    public double getMutateRate() {
        return _mutateRate;
    }

    /**
     * @param mutateSigma Standard deviation used when mutating {@link IntervalGene IntervalGenes}.
     */
    public void setMutateSigma(final double mutateSigma) {
        _mutateSigma = mutateSigma;
    }

    /**
     * @return Standard deviation used when mutating {@link IntervalGene IntervalGenes}.
     */
    public double getMutateSigma() {
        return _mutateSigma;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.mutator.Mutator#mutate(edu.virginia.cs.geneticalgorithm.gene.Genotype)
     */
    @Override
    public Genotype mutate(final Genotype toMutate) {
        final Genotype retval = toMutate.clone();
        for (int i = 0; i < toMutate.getNumGenes(); ++i) {
            if (_rng.nextDouble() < _mutateRate) {
                final Gene g = retval.getGene(i);
                if (g instanceof IntervalGene) {
                    final IntervalGene ig = (IntervalGene) retval.getGene(i);
                    retval.setGene(i, ig.mutate(_rng, _mutateSigma));
                }
                else {
                    retval.setGene(i, g.mutate(_rng));
                }
            }
        }
        _mutateRate = _mutateRate * (1 - _rateDecay);
        _mutateSigma = _mutateSigma * (1 - _sigmaDecay);
        return retval;
    }

}