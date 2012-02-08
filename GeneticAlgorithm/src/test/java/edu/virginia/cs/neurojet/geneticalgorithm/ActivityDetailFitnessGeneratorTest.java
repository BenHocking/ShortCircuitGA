/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */

package edu.virginia.cs.neurojet.geneticalgorithm;

import java.util.Arrays;
import java.util.List;
import edu.virginia.cs.neurojet.model.NeuroJetActivityTest;
import edu.virginia.cs.neurojet.model.NeuroJetActivity;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test harness for ActivityDetailFitnessGenerator
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 */
public class ActivityDetailFitnessGeneratorTest {

    public void testConstructor() throws Exception {
        NeuroJetActivity activityFile = NeuroJetActivityTest.getActivityFile();
        ActivityDetailFitnessGenerator instance = new ActivityDetailFitnessGenerator(activityFile, 2.5);
        assertEquals(activityFile, instance._activityFile);
    }

    /**
     * Test of generateQuickFitness method, of class ActivityDetailFitnessGenerator.
     */
    @Test
    public void testGenerateQuickFitness() throws Exception {
        NeuroJetActivity activityFile = NeuroJetActivityTest.getActivityFile();
        ActivityDetailFitnessGenerator instance = new ActivityDetailFitnessGenerator(activityFile, 2.5);
        instance.generateQuickFitness();
        List<Double> fitness = instance.fitnessValues();
        List<Double> expected = Arrays.asList(0.022549838479366438, 1373.1462316133766);
        assertArrayEquals(expected.toArray(), fitness.toArray());
    }

}