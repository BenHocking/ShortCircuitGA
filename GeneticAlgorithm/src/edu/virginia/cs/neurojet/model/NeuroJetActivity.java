/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.virginia.cs.common.utils.ArrayNumberUtils;
import edu.virginia.cs.common.utils.Pause;

/**
 * Class for reading and interpreting NeuroJet activity files
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 25, 2010
 */
public class NeuroJetActivity extends File {

    private List<List<Double>> _activityList = null;
    private final double _timeStep;

    /**
     * @param pathname Path to Activity file
     * @param timeStep Size of simulated time step in milliseconds
     */
    public NeuroJetActivity(final String pathname, final double timeStep) {
        super(pathname);
        _timeStep = timeStep;
    }

    /**
     * @param parent Directory where Activity file resides
     * @param child Activity file
     * @param timeStep Size of simulated time step in milliseconds
     */
    public NeuroJetActivity(final File parent, final String child, final double timeStep) {
        super(parent, child);
        _timeStep = timeStep;
    }

    /**
     * Calculates the mean activity for each trial in the simulation
     * @return A {@link java.util.List List} of {@link java.lang.Double Doubles} of the mean activity for each trial in the
     * simulation
     */
    public List<Double> acrossTrialActivity() {
        final List<List<Double>> activity = getActivity();
        final List<Double> retval = new ArrayList<Double>();
        for (final List<Double> dblList : activity) {
            retval.add(ArrayNumberUtils.mean(dblList));
        }
        return retval;
    }

    /**
     * Finds the activity for a given trial in the simulation
     * @param trial Trial to find the activity of
     * @return {@link java.util.List List} of activity for the requested trial
     */
    public List<Double> withinTrialActivity(final int trial) {
        final List<List<Double>> activity = getActivity();
        return (trial < activity.size()) ? activity.get(trial) : null;
    }

    /**
     * @return Activity for every trial in the simulation
     */
    public List<List<Double>> getActivity() {
        return getActivity(false);
    }

    /**
     * @param reRead Whether to read the activity file again, even if it's already been read
     * @return Activity for every trial in the simulation
     */
    public List<List<Double>> getActivity(final boolean reRead) {
        if (reRead) {
            _activityList = null;
        }
        if (_activityList == null) {
            // timeStep is in ms. This should be equivalent to 1 / (_timestep measured in seconds), or 1 / (_timestep / 1000)
            final double hzConvFactor = 1000.0 / _timeStep;
            try {
                final boolean fileFound = Pause.untilExists(this, 2000);
                if (!fileFound) {
                    throw new IOException("Couldn't find file '" + this.getPath() + "'");
                }
                final BufferedReader actReader = new BufferedReader(new FileReader(this));
                _activityList = new ArrayList<List<Double>>();
                String line;
                while ((line = actReader.readLine()) != null) {
                    final String[] activities = line.split("\\s");
                    _activityList.add(ArrayNumberUtils.multiply(ArrayNumberUtils.toDoubleList(activities), hzConvFactor));
                }
                actReader.close();
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        return _activityList;
    }
}
