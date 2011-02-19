/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

/**
 * Container for formulae useful for evaluating NeuroJet episodes
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Dec 5, 2010
 */
public class NeuroJetFormulae {

    /**
     * Calculates the number of neurons that fired during a specified interval.
     * @param bufferFile File to read firing data from (in NeuroJet/MATLAB format)
     * @param intervalBegin First time step to include in calculations
     * @param intervalEnd Last time step to include in calculations
     * @return Number of neurons from neuronSet that fired during the specified intervals
     * @throws IOException If the buffer file cannot be accessed or is in the wrong format
     */
    public static int numberActiveNeurons(final File bufferFile, final int intervalBegin, final int intervalEnd) throws IOException {
        return numberActiveNeurons(bufferFile, intervalBegin, intervalEnd, null);
    }

    /**
     * Calculates the number of neurons from a specified set of neurons that fired during a specified interval.
     * @param bufferFile File to read firing data from (in NeuroJet/MATLAB format)
     * @param intervalBegin First time step to include in calculations
     * @param intervalEnd Last time step to include in calculations
     * @param neuronSet Set of neurons to include in calculations (null to include all neurons)
     * @return Number of neurons from neuronSet that fired during the specified intervals
     * @throws IOException If the buffer file cannot be accessed or is in the wrong format
     */
    public static int numberActiveNeurons(final File bufferFile, final int intervalBegin, final int intervalEnd,
                                          final Set<Integer> neuronSet) throws IOException {
        int retval = 0;
        final BufferedReader actReader = new BufferedReader(new FileReader(bufferFile));
        String line;
        // Tone neurons are from 1 to me
        // Puff neurons are from me + 1 to 2 * me
        // final int firstBlinkNeuron = me + 1;
        // final int lastBlinkNeuron = 2 * me;
        while ((line = actReader.readLine()) != null) {
            final String[] lineData = line.split("\\s");
            final int timePos = Integer.parseInt(lineData[0]);
            if (timePos >= intervalBegin && timePos <= intervalEnd) {
                final boolean isOn = (Integer.parseInt(lineData[2]) != 0);
                if (isOn) {
                    final Integer nPos = Integer.parseInt(lineData[1]);
                    if (neuronSet == null || neuronSet.contains(nPos)) {
                        ++retval;
                    }
                }
            }
        }
        actReader.close();
        return retval;
    }

    /**
     * Calculates how many neurons from a specified set of neurons fired during a specified interval as a fraction of all neurons
     * that fired during that same time interval.
     * @param bufferFile File to read firing data from (in NeuroJet/MATLAB format)
     * @param intervalBegin First time step to include in calculations
     * @param intervalEnd Last time step to include in calculations
     * @param neuronSet Set of neurons to include in calculations (null to include all neurons)
     * @return Number of neurons from neuronSet that fired during the specified intervals
     * @throws IOException If the buffer file cannot be accessed or is in the wrong format
     */
    public static double fractionActiveNeurons(final File bufferFile, final int intervalBegin, final int intervalEnd,
                                               final Set<Integer> neuronSet) throws IOException {
        final double numerator = numberActiveNeurons(bufferFile, intervalBegin, intervalEnd, neuronSet);
        final double denominator = numberActiveNeurons(bufferFile, intervalBegin, intervalEnd);
        return numerator / denominator;
    }

}
