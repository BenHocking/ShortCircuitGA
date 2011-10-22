/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.concurrent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread that invokes another runnable and tracks its output, errors, and time running
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Dec 6, 2010
 */
public class InvokerThread extends Thread {

    private final Runnable _preprocessor;
    private final File _executable;
    private final List<String> _arguments;
    private final File _workingDir;
    private Process _process;
    private long _start = -1;
    private long _end = -1;
    private boolean _halted;
    private final Object _lock = new Object();
    private final StringBuffer _out = new StringBuffer();
    private final StringBuffer _err = new StringBuffer();

    /**
     * Constructor
     * @param preprocessor Runnable to run prior to running the executable
     * @param executable Executable to run in the thread
     * @param arguments Arguments to pass to the executable
     * @param workingDir Directory to run the executable from
     */
    public InvokerThread(final Runnable preprocessor, final File executable, final List<String> arguments, final File workingDir) {
        _preprocessor = preprocessor;
        _executable = executable;
        _arguments = arguments;
        _workingDir = workingDir;
        _halted = false;
    }

    @Override
    public void run() {
        final Thread t = new Thread() {

            @Override
            public void run() {
                synchronized (_lock) {
                    try {
                        _halted = false;
                        _start = System.currentTimeMillis();
                        _end = -1;
                        BufferedReader err = null;
                        BufferedReader out = null;
                        try {
                            if (_preprocessor != null) {
                                _preprocessor.run();
                            }
                            final List<String> command = new ArrayList<String>();
                            command.add(_executable.getCanonicalPath());
                            command.addAll(_arguments);
                            final ProcessBuilder builder = new ProcessBuilder(command);
                            builder.directory(_workingDir);
                            _process = builder.start();
                            out = new BufferedReader(new InputStreamReader(_process.getInputStream()));
                            String line;
                            while ((line = out.readLine()) != null) {
                                _out.append(line);
                            }
                            try {
                                err = new BufferedReader(new InputStreamReader(_process.getErrorStream()));
                                while ((line = err.readLine()) != null) {
                                    _err.append(line);
                                }
                            }
                            catch (final IOException e) {
                                err.close();
                                _err.append(e.getStackTrace().toString());
                            }
                        }
                        catch (final IOException e) {
                            if (out != null) try {
                                out.close();
                            }
                            catch (IOException ex) {
                                Logger.getLogger(InvokerThread.class.getName()).log(Level.SEVERE, null, ex);
                                throw new RuntimeException(ex);
                            }
                            if (!_halted) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    finally {
                        _end = System.currentTimeMillis();
                    }
                }
            }
        };
        t.run();
    }

    /**
     * Calculates how long the process ran
     * @return How long the process ran
     */
    public long getDuration() {
        synchronized (_lock) {
            final long e = ((_end > 0) ? _end : System.currentTimeMillis());
            final long s = (_start > 0) ? _start : e;
            return e - s + 1;
        }
    }

    /**
     * Stops the process
     */
    public void halt() {
        synchronized (_lock) {
            if (!_halted) {
                if (_process != null) {
                    _process.destroy();
                }
                _end = System.currentTimeMillis();
                _halted = true;
            }
        }
    }
}
