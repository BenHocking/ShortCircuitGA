/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common.gui;

import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Point2i;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

/**
 * JFrame for displaying a scatter plot
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Jul 19, 2010
 */
public class ScatterPlot extends JFrame {

    /**
     * Constructor
     * @param title Frame and plot title
     * @param data List of Point2i used to create the scatter plot
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @exception HeadlessException if GraphicsEnvironment.isHeadless() returns true.
     */
    public ScatterPlot(final String title, final List<Point2i> data, final String xAxisLabel, final String yAxisLabel)
                                                                                                                      throws HeadlessException {
        super(title);
        setContentPane(createScatterPlotPanel(data, title, xAxisLabel, yAxisLabel));
        pack();
    }

    /**
     * Creates a JPanel containing a scatter plot of the provided data.
     * @param data List of Point2i used to create the scatter plot
     * @param title Plot title
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @return JPanel with scatter plot on it
     */
    public static JPanel createScatterPlotPanel(final List<Point2i> data, final String title, final String xAxisLabel,
                                                final String yAxisLabel) {
        final Integer key = Integer.valueOf(0);
        final XYSeries series = new XYSeries(key, true, false);
        for (final Point2i p : data) {
            series.add(p.x + Math.random() * 1e-8, p.y);
        }
        final DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        dataset.addSeries(series);
        return createSpyPanel(dataset, title, xAxisLabel, yAxisLabel);
    }

    /**
     * Creates a JPanel containing a scatter plot of the provided data.
     * @param dataset XYDataset used to create the scatter plot
     * @param title Plot title
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @return JPanel with scatter plot on it
     */
    public static JPanel createSpyPanel(final XYDataset dataset, final String title, final String xAxisLabel,
                                        final String yAxisLabel) {
        final JFreeChart chart = ChartFactory.createScatterPlot(title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL,
                                                                false, false, false);
        final ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
}
