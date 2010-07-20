/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2i;

import org.junit.Test;

import edu.virginia.cs.common.gui.ScatterPlot;
import edu.virginia.cs.common.utils.Pause;

/**
 * Tests and stores JFreeChart examples
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 18, 2010
 */
public class JFreeChartTest {

    /**
     * Test to make sure that ScatterPlot can show up. TODO Needs to be improved.
     */
    @Test
    public void mainTest() {
        spyDemo();
        Pause.untilExists(new File("dne"), 1000);
    }

    private static void spyDemo() {
        final List<Point2i> data = new ArrayList<Point2i>();
        data.add(new Point2i(1, 1000));
        data.add(new Point2i(2, 1500));
        data.add(new Point2i(2, 1250));
        data.add(new Point2i(3, 900));
        data.add(new Point2i(3, 1000));
        final ScatterPlot scatterPlot = new ScatterPlot("Firing diagram", data, "Time", "Neuron");
        scatterPlot.setVisible(true);
    }
}
