/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

/**
 * Interface for class that that has a specific Trigger
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 20, 2010
 */
public interface Trigger {

    /**
     * Waits up to maxWait milliseconds for trigger to occur
     * @param maxWait Maximum number of milliseconds to wait for
     * @return Whether the trigger occurred
     */
    public boolean waitForTrigger(int maxWait);
}
