/*
 * Copyright (c) 2012 Ashlie B. Hocking
 * All Rights reserved. 
 */
package edu.virginia.cs.common.utils;

/**
 * Class for interrupting running processes
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie B. Hocking</a>
 * @since Mar 3, 2012
 */
public class InterruptListener {
    private volatile boolean _wantsInterrupt = false;

    /**
     * Request that the process (or processes) listening to this interrupter interrupt its process
     */
    public void askForInterrupt() {
        _wantsInterrupt = true;
    }

    /**
     * Queries whether the process should be interrupted
     * @return Whether the process should be interrupted
     */
    public boolean wantsInterrupt() {
        return _wantsInterrupt;
    }
}