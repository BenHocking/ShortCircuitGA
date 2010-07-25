/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.gui;

import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Point2d;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

/**
 * JFrame for displaying a single valued time plot
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 24, 2010
 */
public class TimePlot extends JFrame {

    /**
     * Constructor
     * @param title Frame and plot title
     * @param data List of Point2d used to create the scatter plot
     * @param timeUnit Time unit to display on the X-axis label
     * @param yAxisLabel Y-axis label
     * @exception HeadlessException if GraphicsEnvironment.isHeadless() returns true.
     */
    public TimePlot(final String title, final List<Point2d> data, final String timeUnit, final String yAxisLabel)
                                                                                                                 throws HeadlessException {
        super(title);
        setContentPane(createTimePlotPanel(data, title, timeUnit, yAxisLabel));
        pack();
    }

    /**
     * Creates a JPanel containing a single valued time plot of the provided data.
     * @param data List of Point2d used to create the time plot
     * @param title Plot title
     * @param timeUnit Time unit to display on the X-axis label
     * @param yAxisLabel Y-axis label
     * @return JPanel with time plot on it
     */
    public static JPanel createTimePlotPanel(final List<Point2d> data, final String title, final String timeUnit,
                                             final String yAxisLabel) {
        final Integer key = Integer.valueOf(0);
        final XYSeries series = new XYSeries(key, true, false);
        for (final Point2d p : data) {
            series.add(p.x, p.y);
        }
        final DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        dataset.addSeries(series);
        return createTimePlotPanel(dataset, title, timeUnit, yAxisLabel);
    }

    /**
     * Creates a JPanel containing a single valued time plot of the provided data.
     * @param dataset XYDataset used to create the time plot
     * @param title Plot title
     * @param timeUnit Time unit to display on the X-axis label
     * @param yAxisLabel Y-axis label
     * @return JPanel with time plot on it
     */
    public static JPanel createTimePlotPanel(final XYDataset dataset, final String title, final String timeUnit,
                                             final String yAxisLabel) {
        final JFreeChart chart = ChartFactory.createXYLineChart(title, "Time (" + timeUnit + ")", yAxisLabel, dataset,
                                                                PlotOrientation.VERTICAL, false, false, false);
        final ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
}
