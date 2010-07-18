/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

import java.io.File;

/**
 * Utility class for various pause methods
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 17, 2010
 */
public class Pause {

    private static final int SLEEP_INTERVAL = 50;

    /**
     * Pauses until a file exists or maxWait milliseconds has ellapsed.
     * @param f File whose existence is being waited for
     * @param maxWait Maximum number of milliseconds to wait for
     * @return Whether the file exists
     */
    public static boolean untilExists(final File f, final int maxWait) {
        boolean retval = f.exists();
        int timeElapsed = 0;
        while ((!retval) && timeElapsed < maxWait) {
            try {
                Thread.sleep(SLEEP_INTERVAL);
            }
            catch (final InterruptedException e) { /* do nothing */
            }
            retval = f.exists();
            timeElapsed += SLEEP_INTERVAL;
        }
        return retval;
    }
}
