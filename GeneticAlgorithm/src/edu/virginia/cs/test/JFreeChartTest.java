/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.vecmath.Point2i;

import org.junit.Test;

import edu.virginia.cs.common.gui.ScatterPlot;
import edu.virginia.cs.common.utils.Trigger;

/**
 * Tests and stores JFreeChart examples
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 18, 2010
 */
public class JFreeChartTest {

    private TriggeredScatterPlot _scatterPlot = null;

    private static class TriggeredScatterPlot extends ScatterPlot implements Trigger {

        private final CountDownLatch _latch = new CountDownLatch(1);

        private TriggeredScatterPlot(final List<Point2i> data) throws HeadlessException {
            super("Firing diagram", data, "Time", "Neuron");
        }

        @Override
        public void setVisible(final boolean v) {
            super.setVisible(v);
            if (!v) {
                _latch.countDown();
            }
        }

        @Override
        public boolean waitForTrigger(final int maxWait) {
            try {
                _latch.await(maxWait, TimeUnit.MILLISECONDS);
            }
            catch (final InterruptedException e) {
                /* do nothing */
            }
            return !isVisible();
        }

    }

    /**
     * Test to make sure that ScatterPlot can show up. TODO Needs to be improved.
     */
    @Test
    public void mainTest() {
        spyDemo();
        _scatterPlot.waitForTrigger(60000);
    }

    private void spyDemo() {
        final List<Point2i> data = new ArrayList<Point2i>();
        data.add(new Point2i(1, 1000));
        data.add(new Point2i(2, 1500));
        data.add(new Point2i(2, 1250));
        data.add(new Point2i(3, 900));
        data.add(new Point2i(3, 1000));
        _scatterPlot = new TriggeredScatterPlot(data);
        _scatterPlot.setVisible(true);
    }
}