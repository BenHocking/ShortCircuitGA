/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.gene;

import edu.virginia.cs.geneticalgorithm.fitness.AbstractFitness;
import edu.virginia.cs.geneticalgorithm.fitness.Fitness;
import edu.virginia.cs.geneticalgorithm.fitness.FitnessFactory;
import edu.virginia.cs.geneticalgorithm.fitness.HaltableFitness;
import edu.virginia.cs.geneticalgorithm.fitness.ProxyFitness;
import edu.virginia.cs.geneticalgorithm.fitness.ProxyFitnessFactory;
import edu.virginia.cs.geneticalgorithm.fitness.ShortCircuitFitness;
import edu.virginia.cs.geneticalgorithm.fitness.ShortCircuitFitnessFactory;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Test harness for ShortCircuitFitnessFactory and ShortCircuitFitness
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 20, 2011
 */
public class ShortCircuitFitnessFactoryTest {

    private static class TrivialStandardHaltableFitness extends StandardGeneticFactoryTest.TrivialStandardFitness implements
            HaltableFitness {

        /**
         * @param genotype
         */
        TrivialStandardHaltableFitness(final Genotype genotype) {
            super(genotype);
        }

        @Override
        public void prepare() {
            // No preparation required
        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.fitness.HaltableFitness#halt()
         */
        @Override
        public void halt() { /* does nothing */
        }

    }

    private static class ProxyFitnessTester extends AbstractFitness implements ProxyFitness {

        protected final Genotype _genotype;
        protected final int _numThresholds;
        private final boolean _isErrorful;

        private ProxyFitnessTester(final Genotype individual, final int numThresholds) {
            _genotype = individual;
            _isErrorful = numThresholds < 0;
            _numThresholds = Math.abs(numThresholds);
        }

        @Override
        public void prepare() {
            // No preparation required
        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.fitness.ProxyFitness#generatesPostFitness()
         */
        @Override
        public boolean generatesPostFitness() {
            return false;
        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.fitness.ProxyFitness#getPostFitness()
         */
        @Override
        public Fitness getPostFitness() {
            return null;
        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.fitness.Fitness#fitnessValues()
         */
        @Override
        public List<Double> fitnessValues() {
            double val = 0;
            for (final Gene g : _genotype) {
                if (g == StandardGene.ONE) val += 1.0;
            }
            final List<Double> retval = new ArrayList<Double>();
            for (int i = 0; i < _numThresholds; ++i) {
                retval.add(val * (2 * i + 1) / (i + 1));
            }
            if (_isErrorful) {
                retval.add(-1.0);
            }
            AbstractFitness.checkFitnessSize(this, retval);
            return retval;
        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.fitness.Fitness#numFitnessValues()
         */
        @Override
        public int numFitnessValues() {
            return _isErrorful ? _numThresholds + 1 : _numThresholds;
        }
    }

    private static class ProxyFitnessTester2 extends ProxyFitnessTester {

        private ProxyFitnessTester2(final Genotype individual, final int numThresholds) {
            super(individual, numThresholds);
        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.fitness.ProxyFitness#generatesPostFitness()
         */
        @Override
        public boolean generatesPostFitness() {
            return true;
        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.fitness.ProxyFitness#getPostFitness()
         */
        @Override
        public Fitness getPostFitness() {
            return _numThresholds < 2 ? new TrivialStandardHaltableFitness(_genotype)
                                     : new StandardGeneticFactoryTest.TrivialStandardFitness(_genotype);
        }
    }

    private static class ProxyFitnessFactoryTester implements ProxyFitnessFactory {

        private final boolean _hasPost;
        private final int _numThresholds;

        private ProxyFitnessFactoryTester(final boolean hasPost, final int numThresholds) {
            _hasPost = hasPost;
            _numThresholds = numThresholds;
        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.fitness.ProxyFitnessFactory#createFitness(edu.virginia.cs.geneticalgorithm.gene.Genotype)
         */
        @Override
        public ProxyFitness createFitness(final Genotype individual) {
            return _hasPost ? new ProxyFitnessTester2(individual, _numThresholds) : new ProxyFitnessTester(individual,
                                                                                                           _numThresholds);
        }

        /**
         * @see edu.virginia.cs.geneticalgorithm.fitness.ProxyFitnessFactory#generatesPostFitness()
         */
        @Override
        public boolean generatesPostFitness() {
            return _hasPost;
        }

    }

    /**
     * Tests constructors for ShortCircuitFitnessFactory and ShortCircuitFitness
     */
    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public final void testConstructors() {
        final FitnessFactory stdFactory = new StandardGeneticFactoryTest.TrivialStandardFitnessFactory();
        new ShortCircuitFitnessFactory(new ProxyFitnessFactoryTester(false, 1), null, stdFactory);
        new ShortCircuitFitnessFactory(new ProxyFitnessFactoryTester(true, 1), null);
        new ShortCircuitFitness(null, Collections.singletonList(Double.valueOf(0)), null);
        try {
            new ShortCircuitFitnessFactory(new ProxyFitnessFactoryTester(false, 1), null, null);
            fail("Did not provide a fitness function");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("If the proxy fitness factory cannot generate a post fitness function, one must be provided!",
                         e.getMessage());
        }
    }

    private ShortCircuitFitness createShortCircuitFitness(final boolean withPost, final int numThresholds) {
        final FitnessFactory stdFactory = new StandardGeneticFactoryTest.TrivialStandardFitnessFactory();
        final List<Double> t = Collections.nCopies(Math.abs(numThresholds), 500.0);
        final ShortCircuitFitnessFactory f = new ShortCircuitFitnessFactory(new ProxyFitnessFactoryTester(withPost, numThresholds),
                                                                            t, stdFactory);
        f.setPostScale(1);
        final Genotype g = StandardGenotypeTest.createStandardGenotype(1000);
        return (ShortCircuitFitness) f.createFitness(g);
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.geneticalgorithm.fitness.ShortCircuitFitnessFactory#createFitness(edu.virginia.cs.geneticalgorithm.gene.Genotype)},
     * {@link edu.virginia.cs.geneticalgorithm.fitness.ShortCircuitFitnessFactory#setPostScale(double)},
     * {@link edu.virginia.cs.geneticalgorithm.fitness.ShortCircuitFitness#setPostScale(double)}, and
     * {@link edu.virginia.cs.geneticalgorithm.fitness.ShortCircuitFitness#getPostScale()}
     */
    @Test
    public final void testCreateFitness() {
        ShortCircuitFitness f = createShortCircuitFitness(true, 1);
        f.toString(); // Coverage
        assertEquals(1, f.getPostScale(), 0);
        f = createShortCircuitFitness(false, 1);
        f.setPostScale(10);
        f.toString(); // Coverage
        assertEquals(10, f.getPostScale(), 0);
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.fitness.ShortCircuitFitness#totalFitness()}.
     */
    @Test
    public final void testTotalFitness() {
        ShortCircuitFitness f;
        for (int i = 0; i < 100; ++i) {
            f = createShortCircuitFitness(true, 1);
            double fit = f.totalFitness();
            if (fit <= 500) {
                assertEquals(460, fit, 40);
            }
            else {
                assertEquals(1050, fit, 50);
            }
            f = createShortCircuitFitness(true, 3);
            fit = f.totalFitness();
            if (fit <= 500) {
                assertEquals(475, fit, 25);
            }
            else {
                if (Math.abs(1000 - fit) < 200) {
                    assertEquals(1000, fit, 100);
                }
                else {
                    assertEquals(2000, fit, 200);
                }
            }
        }
    }

    /**
     * Test method for {@link edu.virginia.cs.geneticalgorithm.fitness.ShortCircuitFitness#fitnessValues()}.
     */
    @Test
    public final void testFitnessValues() {
        ShortCircuitFitness f;
        for (int i = 0; i < 100; ++i) {
            f = createShortCircuitFitness(true, 1);
            List<Double> fitList = f.fitnessValues();
            for (final Double fit : fitList) {
                if (fit != 0) {
                    assertEquals(500, fit, 80);
                }
            }
            f = createShortCircuitFitness(true, 3);
            fitList = f.fitnessValues();
            for (int j = 0; j < fitList.size(); ++j) {
                final double fit = fitList.get(j);
                if (j < fitList.size() - 1) {
                    final double multiplier = (2.0 * j + 1) / (j + 1);
                    assertEquals(500 * multiplier, fit, 80 * multiplier);
                }
                else if (fit != 0) {
                    assertEquals(500, fit, 80);
                }
            }
        }
        f = createShortCircuitFitness(true, -2);
        try {
            f.totalFitness();
            fail("Too many fitness values");
        }
        catch (final IllegalArgumentException e) {
            assertEquals("# of fitness values from prefitness function exceeds size of threshold test.", e.getMessage());
        }
    }
}
