package edu.virginia.cs.geneticalgorithm.mutator;

import java.util.Random;

import edu.virginia.cs.geneticalgorithm.gene.Gene;
import edu.virginia.cs.geneticalgorithm.gene.Genotype;

/**
 * Standard {@link Mutator} class.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public final class StandardMutator implements Mutator {

    private final Random _rng;
    private double _mutateRate;

    /**
     * Constructor that fully specifies a typical {@link Mutator} object
     * @param mutateRate Probability (0 to 1) that a {@link Gene} will mutate
     * @param rng Random number generator used with probabilities
     */
    public StandardMutator(final double mutateRate, final Random rng) {
        _rng = rng;
        _mutateRate = mutateRate;
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
     * @see edu.virginia.cs.geneticalgorithm.mutator.Mutator#mutate(edu.virginia.cs.geneticalgorithm.gene.Genotype)
     */
    @Override
    public Genotype mutate(final Genotype toMutate) {
        final Genotype retval = toMutate.clone();
        for (int i = 0; i < toMutate.getNumGenes(); ++i) {
            if (_rng.nextDouble() < _mutateRate) {
                retval.setGene(i, retval.getGene(i).mutate(_rng));
            }
        }
        return retval;
    }

}