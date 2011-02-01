/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

/**
 * Interface for a Fitness function that can be halted.
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Oct 25, 2010
 */
public interface HaltableFitness extends Fitness {

    /**
     * Halt the fitness function if it is still running. Does nothing if the fitness function has already completed.
     */
    public void halt();
}
