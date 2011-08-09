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
public final class IntervalMutator implements Mutator {

    private double _mutateRate;
    private double _mutateSigma;
    private final Random _rng;

    /**
     * Constructor that fully specifies a typical {@link Mutator} object for an interval gene
     * @param mutateRate Probability (0 to 1) that a {@link Gene} will mutate
     * @param mutateSigma standard deviation to use when mutating interval genes
     * @param rng Random number generator used with probabilities
     */
    public IntervalMutator(final double mutateRate, final double mutateSigma, final Random rng) {
        _mutateRate = mutateRate;
        _mutateSigma = mutateSigma;
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
     * @param mutateSigma standard deviation to use when mutating interval genes
     */
    public void setMutateSigma(final double mutateSigma) {
        _mutateSigma = mutateSigma;
    }

    /**
     * @return standard deviation to use when mutating interval genes
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
        return retval;
    }

}