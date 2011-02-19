/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static edu.virginia.cs.common.utils.ArrayNumberUtils.*;
import edu.virginia.cs.common.utils.Pause;

/**
 * Class for reading and interpreting NeuroJet activity files
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 25, 2010
 */
public class NeuroJetActivity extends File {

    private List<List<Double>> _activityList = null;
    private final double _timeStep;
    private final File _signal;
    private int _waitTime = 2000;

    /**
     * @param pathname Path to Activity file
     * @param timeStep Size of simulated time step in milliseconds
     */
    public NeuroJetActivity(final String pathname, final double timeStep) {
        super(pathname);
        _signal = new File(pathname + ".ready");
        _timeStep = timeStep;
    }

    /**
     * @param parent Directory where Activity file resides
     * @param child Activity file
     * @param timeStep Size of simulated time step in milliseconds
     */
    public NeuroJetActivity(final File parent, final String child, final double timeStep) {
        super(parent, child);
        _signal = new File(parent, child + ".ready");
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
            retval.add(mean(dblList));
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
     * Finds the average within-trial activity for all trials
     * @return {@link java.util.List List} of activity for the requested trial
     */
    public List<Double> withinTrialActivity() {
        final List<List<Double>> activity = getActivity();
        if (activity.isEmpty()) return new ArrayList<Double>();
        final List<Double> retval = new ArrayList<Double>(activity.get(0).size());
        for (final List<Double> trialActivity : activity) {
            accum(retval, trialActivity);
        }
        divide(retval, activity.size());
        return retval;
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
                final boolean fileFound = Pause.untilExists(_signal, _waitTime);
                if (!fileFound) {
                    throw new IOException("Couldn't find file '" + _signal.getPath() + "'");
                }
                final BufferedReader actReader = new BufferedReader(new FileReader(this));
                _activityList = new ArrayList<List<Double>>();
                String line;
                while ((line = actReader.readLine()) != null) {
                    final String[] activities = line.split("\\s");
                    _activityList.add(multiply(toDoubleList(activities), hzConvFactor));
                }
                actReader.close();
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        return _activityList;
    }

    /**
     * Calculates the average activity over a specific interval
     * @param intervalBegin First time step to include in interval
     * @param intervalEnd Last time step to include in interval
     * @return Activity averaged over a specific interval
     */
    public double averageActivity(final int intervalBegin, final int intervalEnd) {
        final List<List<Double>> activity = getActivity();
        double retval = 0.0;
        for (final List<Double> withinTrial : activity) {
            final int lastElement = withinTrial.size(); // 1-based
            // Array is 0-based, but time steps are 1-based
            final int curLast = Math.min(intervalEnd /* + 1 - 1 */, lastElement); // Not inclusive
            retval += mean(withinTrial, intervalBegin - 1, curLast);
        }
        return retval / activity.size();
    }

    /**
     * Calculates the average activity
     * @return Averaged activity
     */
    public double averageActivity() {
        return averageActivity(1, Integer.MAX_VALUE);
    }

    /**
     * Calculates the squared deviation from desired activity over a specific interval
     * @param intervalBegin First time step to include in interval
     * @param intervalEnd Last time step to include in interval
     * @param desiredAct Desired activity
     * @return Squared deviation of activity over a specific interval
     */
    public double squaredDeviation(final int intervalBegin, final int intervalEnd, final double desiredAct) {
        final double deviation = (averageActivity(intervalBegin, intervalEnd) - desiredAct) / desiredAct;
        return deviation * deviation;
    }

    /**
     * Calculates the squared deviation from desired activity
     * @param desiredAct Desired activity
     * @return Squared deviation of activity
     */
    public double squaredDeviation(final double desiredAct) {
        return squaredDeviation(1, Integer.MAX_VALUE, desiredAct);
    }

    /**
     * @return Sample standard deviation of the activity
     */
    public double sampleStandardDeviation() {
        return sampleStdDev(getActivity());
    }

    /**
     * Sets the amount of time to wait in milliseconds
     * @param waitTime Amount of time to wait for activity file to be read
     */
    public void setWaitTime(final int waitTime) {
        _waitTime = waitTime;
    }
}
