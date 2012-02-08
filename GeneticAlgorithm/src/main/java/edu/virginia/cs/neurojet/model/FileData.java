/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.virginia.cs.common.utils.Pause;

/**
 * Standard class for reading a list of numbers (with a signal file indicating readiness)
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Mar 20, 2011
 */
public class FileData extends File {

    private final int _waitTime;
    private final File _signal;
    private List<Double> _data = null;
    private final Object _lock = new Object();

    /**
     * @param pathname Path to data file
     * @param waitTime How long to wait for file to be ready
     * @param signal File used to signal that data is ready
     */
    public FileData(final String pathname, final int waitTime, final File signal) {
        super(pathname);
        _waitTime = waitTime;
        _signal = signal != null ? signal : new File(pathname + ".ready");
    }

    /**
     * @param parent Directory where data file resides
     * @param child data file data
     * @param waitTime How long to wait for file to be ready
     * @param signal File used to signal that data is ready
     */
    public FileData(final File parent, final String child, final int waitTime, final File signal) {
        super(parent, child);
        _waitTime = waitTime;
        _signal = signal != null ? signal : new File(parent, child + ".ready");
    }

    /**
     * @return List of double values contained in the specified file
     */
    public List<Double> getData() {
        synchronized (_lock) {
            if (_data == null) {
                try {
                    final boolean fileFound = Pause.untilExists(_signal, _waitTime);
                    if (!fileFound) {
                        throw new IOException("Couldn't find file '" + _signal.getPath() + "'");
                    }
                    final BufferedReader reader = new BufferedReader(new FileReader(this));
                    try {
                        _data = new ArrayList<Double>();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            _data.add(Double.valueOf(line));
                        }
                    }
                    finally {
                        reader.close();
                    }
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return _data;
    }

}
