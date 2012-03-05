/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility functions related to invoking commands
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie B. Hocking</a>
 * @since Nov 26, 2011
 */
public class ProcessBuilderUtils {

    /**
     * @param workingDir directory to run program from
     * @param executable program to run
     * @param interruptListener interrupt listener that provides a means for interrupting a running process
     * @param arguments arguments to pass to the program
     * @throws IOException if program does not exist and possibly for other reasons
     */
    public static void
            invoke(final File workingDir, final File executable, final InterruptListener interruptListener,
                   final String... arguments) throws IOException {
        invoke(null, null, workingDir, executable, interruptListener, arguments);
    }

    /**
     * @param outBuff buffer to write output to
     * @param errBuff buffer to write errors to
     * @param workingDir directory to run program from
     * @param executable program to run
     * @param interruptListener interrupt listener that provides a means for interrupting a running process
     * @param arguments arguments to pass to the program
     * @throws IOException if program does not exist and possibly for other reasons
     */
    public static void invoke(final StringBuffer outBuff, final StringBuffer errBuff, final File workingDir,
                              final File executable, final InterruptListener interruptListener,
                              final String... arguments) throws IOException {
        final List<String> command = new ArrayList<String>();
        command.add(executable.getCanonicalPath());
        command.addAll(Arrays.asList(arguments));
        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);
        if (!wantsInterrupt(interruptListener)) {
            final Process process = builder.start();
            processStream(outBuff, process.getInputStream(), interruptListener);
            if (!wantsInterrupt(interruptListener)) {
                process.destroy();
            }
            else {
                processStream(errBuff, process.getErrorStream(), interruptListener);
                if (!wantsInterrupt(interruptListener)) {
                    process.destroy();
                }
            }
        }
    }

    /**
     * Null-safe check for whether interruptListener has requested an interrupt
     * @param interruptListener Listener to check
     * @return Whether interruptListener has requested an interrupted if it is non-null
     */
    static boolean wantsInterrupt(final InterruptListener interruptListener) {
        return interruptListener != null && interruptListener.wantsInterrupt();
    }

    static void processStream(final StringBuffer buff, final InputStream stream,
                              final InterruptListener interruptListener) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (buff != null) {
                    buff.append(line);
                }
                if (wantsInterrupt(interruptListener)) {
                    break;
                }
            }
        }
        catch (final IOException e) {
            if (buff != null) {
                buff.append(e.getStackTrace().toString());
            }
            throw e;
        }
        finally {
            reader.close();
        }
    }
}
