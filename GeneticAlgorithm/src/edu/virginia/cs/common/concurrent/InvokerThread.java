/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.concurrent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        synchronized (_lock) {
            _halted = false;
            _preprocessor.run();
            final List<String> command = new ArrayList<String>();
            try {
                command.add(_executable.getCanonicalPath());
                command.addAll(_arguments);
                final ProcessBuilder builder = new ProcessBuilder(command);
                builder.directory(_workingDir);
                _process = builder.start();
                _start = System.currentTimeMillis();
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Calculates how long the process ran
     * @return How long the process ran
     */
    public long getDuration() {
        synchronized (_lock) {
            return ((_end > 0) ? _end : System.currentTimeMillis()) - _start + 1;
        }
    }

    /**
     * Stops the process
     */
    public void halt() {
        synchronized (_lock) {
            if (!_halted) {
                _process.destroy();
                _end = System.currentTimeMillis();
                _halted = true;
            }
        }
    }

}
