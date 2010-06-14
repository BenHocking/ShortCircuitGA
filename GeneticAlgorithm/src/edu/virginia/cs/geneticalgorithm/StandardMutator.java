package edu.virginia.cs.geneticalgorithm;

import java.util.Random;

/**
 * 
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public final class StandardMutator implements Mutator {

    private final Random _rng;
    private double _mutateRate;

    /**
     * Constructor that fully specifies a typical {@link Mutator} object
     * @param mutateRate
     * @param rng
     */
    public StandardMutator(final double mutateRate, final Random rng) {
        _rng = rng;
        _mutateRate = mutateRate;
    }

    /**
     * 
     * @param mutateRate
     */
    public void setMutateRate(final double mutateRate) {
        _mutateRate = mutateRate;
    }

    /**
     * 
     * @return
     */
    public double getMutateRate() {
        return _mutateRate;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Mutator#mutate(edu.virginia.cs.geneticalgorithm.Genotype)
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