/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

/**
 * Interface for conditions that must be met
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 20, 2010
 */
public interface Condition {

    /**
     * @return Whether the {@link Condition} has been met.
     */
    public boolean met();
}
