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
 * @author <a href="mailto:ben.hocking@mseedsoft.com">A. Ben Hocking</a>
 * @since Nov 26, 2011
 */
public class ProcessBuilderUtils {

    public static void invoke(File workingDir, File executable, String... arguments) throws IOException {
        invoke(null, null, workingDir, executable, arguments);
    }

    public static void invoke(StringBuffer outBuff, StringBuffer errBuff, File workingDir, File executable, String... arguments) throws IOException {
        final List<String> command = new ArrayList<String>();
        command.add(executable.getCanonicalPath());
        command.addAll(Arrays.asList(arguments));
        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);
        Process process = builder.start();
        processStream(outBuff, process.getInputStream());
        processStream(errBuff, process.getErrorStream());
    }

    static void processStream(StringBuffer buff, InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (buff != null) {
                    buff.append(line);
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
