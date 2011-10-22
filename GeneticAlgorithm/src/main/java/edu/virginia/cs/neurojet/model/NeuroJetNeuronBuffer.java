/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.virginia.cs.common.utils.IntegerRange;
import edu.virginia.cs.common.utils.Pause;
import edu.virginia.cs.common.utils.SetUtils;

/**
 * File containing NeuroJet Buffer data
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Dec 5, 2010
 */
public class NeuroJetNeuronBuffer extends File {

    private final List<Set<Integer>> _firingBuffer = new ArrayList<Set<Integer>>();
    private final File _signal;
    private final int _waitTime;

    /**
     * @param pathname Path to Activity file
     * @param waitTime How long to wait for the buffer file to be written (depends tremendously on the script)
     */
    public NeuroJetNeuronBuffer(final String pathname, final int waitTime) {
        super(pathname);
        _signal = new File(pathname + ".ready");
        _waitTime = waitTime;
    }

    /**
     * @param parent Directory where Activity file resides
     * @param child Activity file
     * @param waitTime How long to wait for the buffer file to be written (depends tremendously on the script)
     */
    public NeuroJetNeuronBuffer(final File parent, final String child, final int waitTime) {
        super(parent, child);
        _signal = new File(parent, child + ".ready");
        _waitTime = waitTime;
    }

    /**
     * @param parent Directory where Activity file resides
     * @param child Activity file
     */
    public NeuroJetNeuronBuffer(final File parent, final String child) {
        this(parent, child, 0);
    }

    /**
     * @return Firing buffer for a particular trial (training or testing) in the simulation
     */
    public List<Set<Integer>> getFiringBuffer() {
        return getFiringBuffer(false);
    }

    /**
     * @param reRead Whether to read the activity file again, even if it's already been read
     * @return Firing buffer for a particular trial (training or testing) in the simulation
     */
    public List<Set<Integer>> getFiringBuffer(final boolean reRead) {
        if (reRead) {
            _firingBuffer.clear();
        }
        if (_firingBuffer.isEmpty()) {
            try {
                final boolean fileFound = Pause.untilExists(_signal, _waitTime);
                if (!fileFound) {
                    throw new IOException("Couldn't find file '" + _signal.getPath() + "'");
                }
                final BufferedReader reader = new BufferedReader(new FileReader(this));
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String[] lineData = line.split("\\s");
                        final boolean isOn = (Integer.parseInt(lineData[2]) != 0);
                        if (isOn) {
                            final Integer neuron = Integer.parseInt(lineData[1]);
                            final int timePos = Integer.parseInt(lineData[0]);
                            while (_firingBuffer.size() < timePos) {
                                _firingBuffer.add(new HashSet<Integer>());
                            }
                            final Set<Integer> firingSet = _firingBuffer.get(timePos - 1);
                            firingSet.add(neuron);
                        }
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
        return _firingBuffer;
    }

    /**
     * @param neuronRange Range of neurons to get the fraction of firing for
     * @param timeRange Time range to sample for
     * @return Fraction of the desired neurons firing out of the total number of firings for the range
     */
    public double fractionFired(final IntegerRange neuronRange, final IntegerRange timeRange) {
        final List<Set<Integer>> neuronBuff = getFiringBuffer();
        int totalNumFired = 0;
        double totalPuffFired = 0;
        for (final Integer i : timeRange) {
            if (i < neuronBuff.size()) {
                final Set<Integer> firingNeurons = neuronBuff.get(i);
                if (!firingNeurons.isEmpty()) {
                    totalNumFired += firingNeurons.size();
                    final Set<Integer> firingPuff = SetUtils.intersection(firingNeurons, neuronRange.asSet());
                    totalPuffFired += firingPuff.size();
                }
            }
        }
        return totalNumFired > 0 ? totalPuffFired / totalNumFired : 0.0;
    }

    /**
     * @return Number of time steps in the firing buffer
     */
    public int numTimeSteps() {
        return getFiringBuffer().size();
    }
}
