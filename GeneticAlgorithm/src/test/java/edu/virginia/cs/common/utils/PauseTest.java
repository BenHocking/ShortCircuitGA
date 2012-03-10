/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import static org.junit.Assert.*;

import static edu.virginia.cs.common.utils.Pause.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.junit.Test;

/**
 * Test harness for Pause
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 19, 2011
 */
public class PauseTest {

    /**
     * Test method for {@link edu.virginia.cs.common.utils.Pause#untilConditionMet(Condition, int)}.
     */
    @Test
    public final void testUntilConditionMet() {
        // Test that Pause.untilConditionMet handles interrupts gracefully
        final Thread interruptibleThread = new Thread() {
            @Override
            public void run() {
                Pause.untilConditionMet(new Condition() {
                    @Override
                    public boolean met() {
                        return false; // Never met
                    }

                }, 5000);
            }
        };
        long begin = System.currentTimeMillis();
        interruptibleThread.start();
        interruptibleThread.interrupt();
        Pause.untilConditionMet(new Condition() {
            @Override
            public boolean met() {
                return !interruptibleThread.isAlive();
            }
        }, 5000);
        assertTrue(System.currentTimeMillis() - begin < 1000);
        // Test that Pause.untilConditionMet times out correctly
        begin = System.currentTimeMillis();
        // Also tests the null condition (which is never met)
        Pause.untilConditionMet(null, 500);
        assertTrue(System.currentTimeMillis() - begin >= 500);
        // other methods test regular functionality of untilConditionMet
    }

    /**
     * Test method for {@link edu.virginia.cs.common.utils.Pause#untilExists(java.io.File, int)}.
     * @throws IOException if unable to create tmp_535231.dat
     */
    @Test
    public final void testUntilExists() throws IOException {
        new Pause(); // For coverage
        final File f = new File("tmp_535231.dat");
        if (f.exists()) {
            f.delete();
        }
        assertEquals(false, untilExists(f, 10));
        f.createNewFile();
        assertEquals(true, untilExists(f, 100));
        f.deleteOnExit();
    }

    /**
     * Test method for {@link edu.virginia.cs.common.utils.Pause#whileVisible(javax.swing.JFrame, int)}.
     */
    @Test
    public final void testWhileVisible() {
        final JFrame frame = new JFrame("Pause test");
        assertEquals(false, whileVisible(frame, 10));
        frame.setVisible(true);
        assertEquals(true, whileVisible(frame, 100));
    }

}
