/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.concurrent;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;

import edu.virginia.cs.data.FileLoader;
import org.junit.Ignore;

/**
 * Test harness for InvokerThread
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 21, 2011
 */
public class InvokerThreadTest {

    private static class RunnableTester implements Runnable {

        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        @SuppressWarnings("SleepWhileHoldingLock")
        public void run() {
            for (int i = 0; i < 100; ++i) {
                try {
                    Thread.sleep(10);
                }
                catch (final InterruptedException e) { // do nothing
                }
            }
        }

    }

    /**
     * @param exeName Name of executable to run (must be in test/data directory)
     * @param pre Preprocessor runnable
     * @return Instance of InvokerThread suitable for testing with
     * @throws URISyntaxException Shouldn't happen
     */
    public static InvokerThread createThread(final String exeName, final Runnable pre) throws URISyntaxException {
        final File echo = new File(FileLoader.getDataDirectory(), exeName);
        if (!echo.canExecute()) {
            echo.setExecutable(true);
        }
        assertTrue(echo.canExecute());
        return new InvokerThread(pre, FileLoader.getDataDirectory(), echo, "Hello world!");
    }

    /**
     * Test method for {@link edu.virginia.cs.common.concurrent.InvokerThread#run()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testRun() throws URISyntaxException {
        final InvokerThread t = createThread("echo", null);
        t.start();
    }

    /**
     * Test method for {@link edu.virginia.cs.common.concurrent.InvokerThread#getDuration()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    @Ignore
    public final void testGetDuration() throws URISyntaxException {
        InvokerThread t = createThread("cp", new RunnableTester());
        t.start();
        final long d1 = t.getDuration();
        t = createThread("echo", null);
        t.start();
        final long d2 = t.getDuration();
        assertTrue(d2 < d1);
    }

    /**
     * Test method for {@link edu.virginia.cs.common.concurrent.InvokerThread#halt()}.
     * @throws Exception Shouldn't happen
     */
    @Test
    public final void testHalt() throws Exception {
        final InvokerThread t = createThread("cp", new RunnableTester());
        t.start();
        for (int i = 0; i < 1000; ++i) {
            /* spin wheels */
        }
        t.halt();
        final long duration = t.getDuration();
        assertEquals(1L, duration);
    }

}
