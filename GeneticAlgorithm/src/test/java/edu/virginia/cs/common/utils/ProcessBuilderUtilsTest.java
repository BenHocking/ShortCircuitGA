/*
 * Copyright (c) 2012 Ashlie B. Hocking All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import edu.virginia.cs.data.FileLoader;

/**
 * Test harness for ProcessBuilderUtils
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie B. Hocking</a>
 * @since Mar 5, 2012
 */
public class ProcessBuilderUtilsTest {

    /**
     * Test method for
     * {@link edu.virginia.cs.common.utils.ProcessBuilderUtils#invoke(java.io.File, java.io.File, edu.virginia.cs.common.utils.InterruptListener, java.lang.String[])}
     * and
     * {@link edu.virginia.cs.common.utils.ProcessBuilderUtils#invoke(java.lang.StringBuffer, java.lang.StringBuffer, java.io.File, java.io.File, edu.virginia.cs.common.utils.InterruptListener, java.lang.String[])}
     * .
     * @throws Exception if one occurs
     */
    @Test
    public final void testInvoke() throws Exception {
        try {
            // Just to prevent coverage complaining
            new ProcessBuilderUtils();
        }
        catch (final Exception e) {
            // do nothing
        }
        final File echo = new File(FileLoader.getDataDirectory(), "echo");
        ProcessBuilderUtils.invoke(FileLoader.getDataDirectory(), echo, null, "Hello!");
        StringBuffer outBuff = new StringBuffer();
        StringBuffer errBuff = new StringBuffer();
        ProcessBuilderUtils.invoke(outBuff, errBuff, FileLoader.getDataDirectory(), echo, null, "Hello!");
        assertTrue(outBuff.toString().contains("Hello!"));
        final InterruptListener listener = new InterruptListener();
        listener.askForInterrupt();
        outBuff = new StringBuffer();
        errBuff = new StringBuffer();
        ProcessBuilderUtils.invoke(outBuff, errBuff, FileLoader.getDataDirectory(), echo, listener, "Hello!");
        assertFalse(outBuff.toString().contains("Hello!"));
        final InterruptListener interruptOnSecondAsk = new MockInterruptListener(2);
        ProcessBuilderUtils.invoke(outBuff,
                                   errBuff,
                                   FileLoader.getDataDirectory(),
                                   echo,
                                   interruptOnSecondAsk,
                                   "Hello!");
        assertFalse(outBuff.toString().contains("Hello!"));
        final InterruptListener interruptOnFourthAsk = new MockInterruptListener(4);
        ProcessBuilderUtils.invoke(outBuff,
                                   errBuff,
                                   FileLoader.getDataDirectory(),
                                   echo,
                                   interruptOnFourthAsk,
                                   "Hello!");
        assertTrue(outBuff.toString().contains("Hello!"));
    }

    /**
     * Test method for
     * {@link edu.virginia.cs.common.utils.ProcessBuilderUtils#wantsInterrupt(edu.virginia.cs.common.utils.InterruptListener)}
     * .
     */
    @Test
    public final void testWantsInterrupt() {
        assertFalse(ProcessBuilderUtils.wantsInterrupt(null));
        final InterruptListener listener = new InterruptListener();
        assertFalse(ProcessBuilderUtils.wantsInterrupt(listener));
        listener.askForInterrupt();
        assertTrue(ProcessBuilderUtils.wantsInterrupt(listener));
    }

    // Hack for getting an interrupt on the nth time it's asked about
    private static class MockInterruptListener extends InterruptListener {
        // How may times can it be asked before automatically requesting an interrupt itself
        private final int _maxCount;
        private int _count = 0;

        MockInterruptListener(final int maxCount) {
            _maxCount = maxCount;
        }

        @Override
        public boolean wantsInterrupt() {
            _count++;
            return (_count >= _maxCount);
        }
    }

    private static class MockInputStream extends InputStream {

        /*
         * (non-Javadoc)
         * @see java.io.InputStream#read()
         */
        @Override
        public int read() throws IOException {
            throw new IOException("Mock exception");
        }

    }

    /**
     * Test method for
     * {@link edu.virginia.cs.common.utils.ProcessBuilderUtils#processStream(java.lang.StringBuffer, java.io.InputStream, edu.virginia.cs.common.utils.InterruptListener)}
     * .
     * @throws Exception if it occurs
     */
    @Test
    public final void testProcessStream() throws Exception {
        StringBuffer buff = new StringBuffer();
        InputStream stream = new FileInputStream(FileLoader.getFile("fit1_a.dat"));
        final InterruptListener interruptListener = new InterruptListener();
        ProcessBuilderUtils.processStream(buff, stream, interruptListener);
        assertTrue(buff.toString().startsWith("0.302734375\n"));
        buff = new StringBuffer();
        stream = new FileInputStream(FileLoader.getFile("fit1_a.dat"));
        interruptListener.askForInterrupt();
        ProcessBuilderUtils.processStream(buff, stream, interruptListener);
        assertEquals(0, buff.length());
        try {
            ProcessBuilderUtils.processStream(buff, new MockInputStream(), interruptListener);
            fail("Expected IOException from MockInputStream");
        }
        catch (final IOException ex) {
            assertEquals("Mock exception", ex.getMessage());
            assertTrue(buff.toString().contains("Mock exception"));
            assertTrue(buff.toString().contains("ProcessBuilderUtilsTest"));
        }
        try {
            // Repeat but without buffer to ensure no NPEs are encountered
            ProcessBuilderUtils.processStream(null, new MockInputStream(), interruptListener);
            fail("Expected IOException from MockInputStream");
        }
        catch (final IOException ex) {
            assertEquals("Mock exception", ex.getMessage());
        }
    }

}
