package edu.virginia.cs.geneticalgorithm;

import java.util.Random;

/**
 * {@link Gene} representing the standard 0/1 bit values.
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

    /**
     * One version of the Gene
     */
    public static final StandardGene ZERO = new StandardGene("0");
    /**
     * The other version of the Gene
     */
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

    @Override
    public Gene mutate(final Random rng) {
        return (this == ZERO) ? ONE : ZERO;
    }

    /**
     * Translates this into an integer value
     * @return
     */
    private int getValue() {
        return (this == ZERO) ? 0 : 1;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return _descriptor;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Gene g) {
        if (!(g instanceof StandardGene)) throw new RuntimeException("Genes must be of the same type");
        final StandardGene sg = (StandardGene) g;
        return getValue() - sg.getValue();
    }
}