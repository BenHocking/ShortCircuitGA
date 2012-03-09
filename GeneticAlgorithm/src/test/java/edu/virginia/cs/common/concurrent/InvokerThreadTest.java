/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.concurrent;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;

import edu.virginia.cs.common.utils.Condition;
import edu.virginia.cs.common.utils.Pause;
import edu.virginia.cs.data.FileLoader;

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

    private static class MockRunnable implements Runnable {
        private boolean _wasRun = false;

        public void run() {
            _wasRun = true;
        }

        boolean ran() {
            return _wasRun;
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
        if (echo.exists()) {
            if (!echo.canExecute()) {
                echo.setExecutable(true);
            }
            assertTrue(echo.canExecute());
        }
        return new InvokerThread(pre, FileLoader.getDataDirectory(), echo, "Hello world!");
    }

    /**
     * Test method for {@link edu.virginia.cs.common.concurrent.InvokerThread#run()}.
     * @throws URISyntaxException Shouldn't happen
     * @throws InterruptedException Shouldn't happen
     */
    @Test
    public final void testRun() throws URISyntaxException, InterruptedException {
        {
            InvokerThread t = createThread("echo", null);
            t.start();
            final MockRunnable mock = new MockRunnable();
            t = createThread("echo", mock);
            assertFalse(mock.ran());
            t.start();
            Pause.untilConditionMet(new Condition() {
                @Override
                public boolean met() {
                    return mock.ran();
                }
            }, 500);
            assertTrue(mock.ran());
            assertNull(t.getException());
        }
        {
            final MockRunnable mock2 = new MockRunnable();
            final InvokerThread t = createThread("dne", mock2);
            assertFalse(mock2.ran());
            t.start();
            Pause.untilConditionMet(new Condition() {
                @Override
                public boolean met() {
                    return mock2.ran();
                }
            }, 500);
            assertTrue(mock2.ran());
            Pause.untilConditionMet(new Condition() {
                @Override
                public boolean met() {
                    return t.getException() != null;
                }
            }, 5000);
            assertNotNull(t.getException());
        }
    }

    /**
     * Test method for {@link edu.virginia.cs.common.concurrent.InvokerThread#getDuration()}.
     * @throws URISyntaxException Shouldn't happen
     */
    @Test
    public final void testGetDuration() throws URISyntaxException {
        final InvokerThread t = createThread("cp", new RunnableTester());
        assertEquals(1L, t.getDuration());
        t.start();
        Pause.untilConditionMet(new Condition() {
            @Override
            public boolean met() {
                return t.getDuration() > 0;
            }
        }, 2000);
        final long d1 = t.getDuration();
        assertTrue(d1 > 0);
        final InvokerThread t2 = createThread("echo", null);
        t2.start();
        Pause.untilConditionMet(new Condition() {
            @Override
            public boolean met() {
                return t2.getDuration() > 0;
            }
        }, 500);
        final long d2 = t2.getDuration();
        assertTrue(d2 > 0);
    }

    /**
     * Test method for {@link edu.virginia.cs.common.concurrent.InvokerThread#halt()}.
     * @throws Exception Shouldn't happen
     */
    @Test
    public final void testHalt() throws Exception {
        {
            final InvokerThread t = createThread("cp", new RunnableTester());
            t.start();
            Pause.untilConditionMet(null, 100);
            t.halt();
            final long duration = t.getDuration();
            assertTrue(duration > 0);
            t.halt(); // Should not cause an error
        }
        final File sleep = new File(FileLoader.getDataDirectory(), "sleep");
        {
            final InvokerThread sleeper = new InvokerThread(null, FileLoader.getDataDirectory(), sleep, "10000");
            sleeper.start();
            sleeper.halt();
            final long duration = sleeper.getDuration();
            assertTrue(duration > 0);
            assertTrue(duration < 1000);
        }
        {
            final InvokerThread sleeper = new InvokerThread(null, FileLoader.getDataDirectory(), sleep, "10000");
            sleeper.halt(); // Should not cause an error
            sleeper.start();
            final long duration = sleeper.getDuration();
            assertTrue(duration <= 1L);
        }
    }

}
