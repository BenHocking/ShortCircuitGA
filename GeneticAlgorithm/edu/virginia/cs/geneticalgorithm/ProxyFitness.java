/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm;

/**
 * Interface for fitness functions that act as proxies for other fitness functions
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Nov 28, 2010
 */
public interface ProxyFitness extends Fitness {

    /**
     * @return Whether this fitness function is responsible for creating its own copy of the post fitness function
     */
    public boolean generatesPostFitness();

    /**
     * @return Fitness function if this proxy generates one, or null otherwise
     */
    public Fitness getPostFitness();
}
