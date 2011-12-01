/*
 * Copyright (c) 2010-2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.neurojet.geneticalgorithm;

import static edu.virginia.cs.common.utils.ArrayNumberUtils.*;
import static edu.virginia.cs.geneticalgorithm.fitness.AbstractFitness.*;
import static edu.virginia.cs.neurojet.geneticalgorithm.ActivitySummaryFitnessGenerator.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import edu.virginia.cs.common.utils.ArrayNumberUtils;
import edu.virginia.cs.common.utils.Condition;
import edu.virginia.cs.common.utils.IntegerRange;
import edu.virginia.cs.common.utils.Pause;
import edu.virginia.cs.common.utils.ProcessBuilderUtils;
import edu.virginia.cs.common.utils.ShapeMatcher;
import edu.virginia.cs.geneticalgorithm.fitness.FitnessGenerator;
import edu.virginia.cs.geneticalgorithm.fitness.HaltableFitness;
import edu.virginia.cs.neurojet.model.FileData;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Standard trace fitness function
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Apr 27, 2010
 */
public class NeuroJetTraceFitness implements HaltableFitness {

    /**
     * Run time + 2 activity measures + trend measure + shape measure + performance measure + target measure
     */
    public static final int NUM_FIT_VALS = 7;
    private static final int WAIT_TIME = 120 /* minutes */ * 60 /* seconds per minute */ * 1000 /* ms per sec */;
    private static final double ACTIVITY_MULTIPLIER = 1E-5;
    private static int NUM_TRIALS = 150;
    private final NeuroJetTraceFitnessIntermediary _parent;
    private final File _tempDir;
    private final int _dirID;
    private Process _process = null;
    private boolean _halted = false;
    private long _end = -1; // FIXME
    // private NeuroJetNeuronBuffer _tstBuff = null;
    private final Object _lock = new Object();
    private final Object _lock2 = new Object();
    private final StringBuffer _out = new StringBuffer();
    private final StringBuffer _err = new StringBuffer();
    private final List<Double> _fitnessValues = new ArrayList<Double>();
    private final static List<Double> _desiredShape = new ArrayList<Double>();
    private FitnessGenerator _tstGenerator = null;
    private boolean _isPrepared = false;
    private File _scriptFile = null;

    NeuroJetTraceFitness(final NeuroJetTraceFitnessIntermediary parent, final int dirID) {
        _parent = parent;
        _dirID = dirID;
        _tempDir = new File(_parent.getParent().getWorkingDir(), "trace_" + String.valueOf(dirID));
        // deleteExistingFiles(); // TODO reconsider this
    }

    private FitnessGenerator getQuickGenerator() {
        if (_tstGenerator == null) {
            final File signalFile = new File(_tempDir, SIGNAL_FILE);
            final FileData activityFile = new FileData(_tempDir, MEAN_TEST_ACTIVITY_DATA_FILE, WAIT_TIME, signalFile);
            final FileData ssdFile = new FileData(_tempDir, SSD_TEST_ACTIVITY_DATA_FILE, WAIT_TIME, signalFile);
            _tstGenerator = new ActivitySummaryFitnessGenerator(activityFile, ssdFile, getDesiredAct());
        }
        return _tstGenerator;
    }

    NeuroJetTraceFitnessFactory getGrandparent() {
        return _parent.getParent();
    }

    /**
     * @return Desired activity
     */
    public double getDesiredAct() {
        return _parent.getDesiredAct();
    }

    /**
     * @return Percentage of activity due to external activation
     */
    public double getMePct() {
        return _parent.getMePct();
    }

    /**
     * @return Number of neurons in simulation
     */
    public static int getNumNeurons() {
        return 2048;
    }

    /**
     * @return Pattern size
     */
    public int getMe() {
        return (int) Math.round(getNumNeurons() * getMePct() / 10);
    }

    double getTimeStep() {
        return _parent.getTimeStep();
    }

    /**
     * @return Which neurons are in the puff pattern
     */
    public IntegerRange getPuffRange() {
        final int me = getMe();
        // Tone neurons are from 1 to me
        // Puff neurons are from me + 1 to 2 * me
        final int firstBlinkNeuron = me + 1;
        final int lastBlinkNeuron = 2 * me;
        return new IntegerRange(firstBlinkNeuron, lastBlinkNeuron);
    }

    /**
     * @return Directory where this fitness performs its calculations
     */
    public File getWorkingDir() {
        return _tempDir;
    }

    boolean isFinished() {
        return _end > 0;
    }

    private void addActivityMeasures(final List<Double> fitnessValues) {
        fitnessValues.addAll(ArrayNumberUtils.multiply(getQuickGenerator().fitnessValues(), ACTIVITY_MULTIPLIER));
    }

    private List<Double> getFracFired() {
        return getFracFired(150);
    }

    private List<Double> getFracFired(final int whichTrial) {
        // _tstBuff = new NeuroJetNeuronBuffer(_tempDir, "tstBuff.dat", WAIT_TIME);
        final FileData summaryBuffData = new FileData(_tempDir, "fit2_" + whichTrial + ".dat", WAIT_TIME,
            new File(_tempDir, "fit2_150.dat.ready"));
        return summaryBuffData.getData();
    }

    private void addTrendMeasure(final List<Double> fitnessValues) {
        double bestValue = 0.0;
        for (int trialNum = 50; trialNum <= NUM_TRIALS; trialNum += 50) {
            final double curValue = slopeContribution(slope(getFracFired(trialNum)));
            if (curValue > bestValue) {
                bestValue = curValue;
            }
        }
        fitnessValues.add(bestValue);
    }

    private double slopeContribution(final double slope) {
        final double minSlope = -0.05;
        if (slope < minSlope) {
            return 0;
        }
        return 1000 * Math.pow(slope - minSlope, 2);
    }

    private List<Double> getDesiredShape() {
        if (_desiredShape.isEmpty()) {
            for (int i = 0; i < 11; ++i) { // Flat for the first 550 ms
                _desiredShape.add(Double.valueOf(0.0));
            }
            _desiredShape.add(1.0); // Then blink 100 ms before puff
            _desiredShape.add(1.0);
            _desiredShape.add(0.5); // Reduced activity during puff
            _desiredShape.add(0.5);
        }
        return _desiredShape;
    }

    private void addShapeMeasure(final List<Double> fitnessValues) {
        final List<Double> desiredShape = getDesiredShape();
        double bestValue = 0.0;
        for (int trialNum = 50; trialNum <= NUM_TRIALS; trialNum += 50) {
            final List<Double> shape = getFracFired(trialNum);
            final double curValue = 100 / ShapeMatcher.squareDeviation(desiredShape, shape);
            if (curValue > bestValue) {
                bestValue = curValue;
            }
        }
        fitnessValues.add(bestValue);
    }

    @Override
    public void prepare() {
        if (!_isPrepared) {
            try {
                File scriptFile = getGrandparent().getMainFile();
                final File cfFile = copyPreviousScriptFile(scriptFile);
                final ScriptUpdater updater = getGrandparent().getUpdater();
                if (NeuroJetTraceFitnessIntermediary.DELETE_WORKING_FILES) {
                    _tempDir.deleteOnExit();
                }
                for (final File f : getGrandparent().getScriptFiles()) {
                    final File script = new File(_tempDir, f.getName());
                    if (f.equals(getGrandparent().getMainFile())) {
                        scriptFile = script;
                    }
                    try {
                        updater.createScriptFromTemplate(script, f, _parent.getGenotype(), _dirID);
                    }
                    catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                final File lastReadyFile = new File(_tempDir, "fit2_300.dat.ready");
                // if (!(filesMatch(scriptFile, cfFile) && lastReadyFile.exists())) {
                // System.out.println("Files match: " + filesMatch(scriptFile, cfFile));
                // System.out.println("lastReadyFile.exists(): " + lastReadyFile.exists());
                // }
                if (filesMatch(scriptFile, cfFile) && lastReadyFile.exists()) {
                    _end = 1; // Signifies completion
                    _halted = true; // So that we don't try to halt an unstarted thread
                }
                else {
                    deleteExistingFiles(getGrandparent().getScriptFiles());
                    final File prepareScript = getGrandparent().getPrepareScript();
                    if (prepareScript != null) {
                        ProcessBuilderUtils.invoke(_out, _err, _tempDir, prepareScript, scriptFile.getParentFile().getCanonicalPath(), String.valueOf(_dirID));
                    }
                }
                _scriptFile = scriptFile;
                _isPrepared = true;
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(NeuroJetTraceFitness.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(NeuroJetTraceFitness.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.Fitness#fitnessValues()
     */
    @Override
    public List<Double> fitnessValues() {
        runSimulationIfNeeded();
        return _parent.getMeanFitnessValues();
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.Fitness#totalFitness()
     */
    @Override
    public double totalFitness() {
        runSimulationIfNeeded();
        return _parent.getMeanTotalFitness();
    }

    /**
     * @see edu.virginia.cs.geneticalgorithm.fitness.Fitness#numFitnessValues()
     */
    @Override
    public int numFitnessValues() {
        return NUM_FIT_VALS;
    }

    void runSimulationIfNeeded() {
        synchronized (_lock) {
            if (_fitnessValues.isEmpty()) {
                _halted = false;
                if (!isFinished()) {
                    invoke();
                }
                double totalFitness = 0;
                if (_halted) {
                    for (int i = 0; i < NUM_FIT_VALS; ++i) {
                        _fitnessValues.add(0.0);
                    }
                }
                else {
                    Pause.untilConditionMet(new FitnessFinished(this), WAIT_TIME);
                    _fitnessValues.add(1.0); //FIXME: Remove this (need to adjust other calculations)
                    addActivityMeasures(_fitnessValues);
                    addTrendMeasure(_fitnessValues);
                    addShapeMeasure(_fitnessValues);
                    final double fitness = generateTraceFitness();
                    assert (fitness >= 0);
                    _fitnessValues.add(fitness);
                    _fitnessValues.add(hasTargetBehavior());
                    totalFitness = calcTotalFitness(_fitnessValues);
                }
                checkFitnessSize(this, _fitnessValues);
                _parent.addToSummedFitnessValues(_fitnessValues, totalFitness);
            }
        }
    }

    private double calcTotalFitness(final List<Double> fitVals) {
        // final double actFitness = 1E-5 * getQuickGenerator().overallFitness();
        // final double slopeFitness = slopeContribution(fitVals.get(3));
        // return 1 / (fitVals.get(0) + 1) + actFitness + slopeFitness + fitVals.get(5) + fitVals.get(6);
        double retval = 0;
        for (final Double d : fitVals) {
            retval += d;
        }
        return retval;
    }

    private void deleteExistingFiles(final List<File> list) {
        // Remove any existing files
        final File[] prevFiles = _tempDir.listFiles();
        if (prevFiles != null) {
            for (final File f : prevFiles) {
                boolean inList = false;
                for (final File listFile : list) {
                    if (listFile.getName().equals(f.getName())) {
                        inList = true;
                        break;
                    }
                }
                if (!inList) {
                    f.delete();
                }
            }
        }
    }

    private void runSimulation() {
        synchronized (_lock2) {
            try {
                // Launch NeuroJet
                try {
                    ProcessBuilderUtils.invoke(_out, _err, _tempDir, getGrandparent().getNeuroJet(), _scriptFile.getCanonicalPath());
                }
                catch (final IOException e) {
                    if (!_halted) {
                        Logger.getLogger(NeuroJetTraceFitness.class.getName()).log(Level.SEVERE, null, e);
                        throw new RuntimeException(e);
                    }
                }
            }
            finally {
                _end = 1;
            }
        }
    }

    /**
     * @param scriptFile
     * @param cfFile
     * @return
     * @throws IOException
     */
    private boolean filesMatch(final File scriptFile, final File cfFile) throws IOException {
        if (cfFile != null) {
            final BufferedReader in1 = new BufferedReader(new FileReader(scriptFile));
            final BufferedReader in2 = new BufferedReader(new FileReader(cfFile));
            try {
                String line1 = null;
                String line2 = null;
                while (((line1 = in1.readLine()) != null) && ((line2 = in2.readLine()) != null)) {
                    if (!line1.equals(line2)) {
                        return false;
                    }
                }
                if (line2 != null) { // Short-circuit evaluation in while loop requires this
                    line2 = in2.readLine();
                }
                // If either line is not null, then the end of that file wasn't reached
                return (line1 == null) && (line2 == null);
            }
            finally {
                in1.close();
                in2.close();
            }
        }
        return false;
    }

    /**
     * @param scriptFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    private File copyPreviousScriptFile(final File scriptFile) throws FileNotFoundException, IOException {
        final File previousScriptFile = new File(_tempDir, scriptFile.getName());
        File cfFile = null;
        if (previousScriptFile != null && previousScriptFile.exists()) {
            cfFile = new File(scriptFile.getParent(), scriptFile.getName() + ".tmp");
            copyFile(previousScriptFile, cfFile);
        }
        return cfFile;
    }

    private boolean copyFile(final File source, final File destination) {
        final File previousScriptFile = new File(_tempDir, source.getName());
        boolean retval = false;
        if (previousScriptFile != null && previousScriptFile.exists()) {
            BufferedReader in = null;
            PrintStream out = null;
            try {
                in = new BufferedReader(new FileReader(source));
                out = new PrintStream(new FileOutputStream(destination));
                String line;
                while ((line = in.readLine()) != null) {
                    out.println(line);
                }
                retval = true;
            }
            catch (final FileNotFoundException e) {
                retval = false;
            }
            catch (final IOException e) {
                retval = false;
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (final IOException e) {
                        retval = false;
                    }
                }
                if (out != null) {
                    out.close();
                }
            }
        }
        return retval;
    }

    static double generateTimeValue(final int when) {
        // Puff normally starts at time step 651 and lasts to 750
        // Responding in this time window demonstrates memory but not prediction
        if (when >= 651) {
            return 0.4;
        }
        // Ideal are responses in the 551 to 650 window
        if (when >= 551) {
            return 1.0;
        }
        // The 100 ms before that isn't so bad
        if (when >= 451) {
            return 0.5;
        }
        // The 200 ms before that isn't so good
        if (when >= 251) {
            return 0.05;
        }
        // The first few hundred ms is too early
        return 0.01;
    }

    // private NeuroJetNeuronBuffer getTestBufferFile() {
    // if (_tstBuff == null) {
    // _tstBuff = new NeuroJetNeuronBuffer(_tempDir, "tstBuff.dat", WAIT_TIME);
    // }
    // return _tstBuff;
    // }
    /**
     * @return Whether the fitness function acquired the target behavior (i.e., blinked at the right time)
     */
    public double hasTargetBehavior() {
        // 11 = (600 / 50) - 1
        final double fracPuffFired = getFracFired().get(11);
        return (fracPuffFired / (0.3 * getMePct()));
    }

    private double generateTraceFitness() {
        double maxFracPuffFired = 0.0;
        int maxEra = -1;
        final List<Double> fracFired = getFracFired();
        for (int i = 0; i < fracFired.size(); ++i) {
            if (maxFracPuffFired < fracFired.get(i)) {
                maxFracPuffFired = fracFired.get(i);
                maxEra = i * 50;
            }
        }
        final int early = 200;
        final double fracPuffEarlyFired = (maxEra > early) ? fracFired.get(1) : 0.0;
        final double retval = (maxFracPuffFired - fracPuffEarlyFired) * generateTimeValue(maxEra + 1) / getMePct();
        return retval;
    }

    @Override
    public void halt() {
        if (!_halted) {
            _halted = true;
            if (_process != null) {
                _process.destroy();
            }
        }
    }

    @Override
    public String toString() {
        return "{hash = " + hashCode() + ", directory = '" + _tempDir + "'}";
    }

    /**
     * Calculates the fitness function.
     * @param individual {@link Genotype} to return the multi-objective fitness values for.
     */
    private void invoke() {
        // No longer does threading
        runSimulation();
    }

    private static class FitnessFinished implements Condition {

        private final NeuroJetTraceFitness _fitness;

        FitnessFinished(final NeuroJetTraceFitness f) {
            _fitness = f;
        }

        /**
         * @see edu.virginia.cs.common.utils.Condition#met()
         */
        @Override
        public boolean met() {
            return _fitness.isFinished();
        }
    }
}
