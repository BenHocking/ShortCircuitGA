/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.concurrent;

import edu.virginia.cs.common.utils.ProcessBuilderUtils;
import java.io.File;
import java.io.IOException;
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
    private final String[] _arguments;
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
    public InvokerThread(final Runnable preprocessor, final File workingDir, final File executable, final String... arguments) {
        _preprocessor = preprocessor;
        _workingDir = workingDir;
        _executable = executable;
        _arguments = arguments;
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
                        try {
                            if (_preprocessor != null) {
                                _preprocessor.run();
                            }
                            ProcessBuilderUtils.invoke(_out, _err, _workingDir, _executable, _arguments);
                        }
                        catch (final IOException ex) {
                            if (!_halted) {
                                Logger.getLogger(InvokerThread.class.getName()).log(Level.SEVERE, null, ex);
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    finally {
                        _end = System.currentTimeMillis();
                    }
                }
            }
        };
        t.start();
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
