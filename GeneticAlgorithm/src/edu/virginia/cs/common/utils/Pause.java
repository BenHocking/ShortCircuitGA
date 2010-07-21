/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.io.File;

import javax.swing.JFrame;

/**
 * Utility class for various pause methods
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 17, 2010
 */
public class Pause {

    private static final int SLEEP_INTERVAL = 50;

    /**
     * Pauses until a condition is met, or a certain time has elapsed
     * @param c Condition that needs to be met
     * @param maxWait Maximum number of milliseconds to wait for
     * @return Whether the condition was met
     */
    public static boolean untilConditionMet(final Condition c, final int maxWait) {
        boolean retval = c.met();
        int timeElapsed = 0;
        while ((!retval) && timeElapsed < maxWait) {
            try {
                Thread.sleep(SLEEP_INTERVAL);
            }
            catch (final InterruptedException e) { /* do nothing */
            }
            retval = c.met();
            timeElapsed += SLEEP_INTERVAL;
        }
        return retval;
    }

    /**
     * Pauses until a file exists or maxWait milliseconds has elapsed.
     * @param f File whose existence is being waited for
     * @param maxWait Maximum number of milliseconds to wait for
     * @return Whether the file exists
     */
    public static boolean untilExists(final File f, final int maxWait) {
        final Condition c = new Condition() {

            @Override
            public boolean met() {
                return f.exists();
            }
        };
        return untilConditionMet(c, maxWait);
    }

    /**
     * Pauses until a frame no longer is visible or maxWait milliseconds has elapsed
     * @param frame Frame to wait while it's still visible
     * @param maxWait Maximum number of milliseconds to wait for
     * @return Whether the frame is still visible
     */
    public static boolean whileVisible(final JFrame frame, final int maxWait) {
        final Condition c = new Condition() {

            @Override
            public boolean met() {
                return !frame.isVisible();
            }
        };
        return !untilConditionMet(c, maxWait);
    }
}
