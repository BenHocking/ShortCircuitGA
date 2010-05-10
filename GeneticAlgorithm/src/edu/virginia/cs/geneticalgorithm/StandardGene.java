package edu.virginia.cs.geneticalgorithm;

import java.util.Random;

/**
 * 
 * TODO Add description
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 25, 2010
 */
public final class StandardGene implements Gene {

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Integer.valueOf(_descriptor);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj.getClass().equals(getClass()) && hashCode() == obj.hashCode();
    }

    public static final StandardGene ZERO = new StandardGene("0");
    public static final StandardGene ONE = new StandardGene("1");

    private final String _descriptor;

    /**
     * @param string
     */
    private StandardGene(final String descriptor) {
        _descriptor = descriptor;
    }

    @Override
    public Gene generate(final Random rng) {
        return rng.nextBoolean() ? ONE : ZERO;
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.Gene#mutate()
     */
    @Override
    public Gene mutate(final Random rng) {
        return (this == ZERO) ? ONE : ZERO;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return _descriptor;
    }
}