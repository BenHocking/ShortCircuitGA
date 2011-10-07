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
