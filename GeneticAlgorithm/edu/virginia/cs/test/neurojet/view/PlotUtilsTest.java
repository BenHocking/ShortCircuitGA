/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test.neurojet.view;

import static org.junit.Assert.*;

import javax.swing.JPanel;

import org.junit.Test;

import edu.virginia.cs.neurojet.view.PlotUtils;

/**
 * Test harness for edu.virginia.cs.neurojet.view.PlotUtils;
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 21, 2011
 */
public class PlotUtilsTest {

    /**
     * Test method for {@link edu.virginia.cs.neurojet.view.PlotUtils#plotActivity()}.
     */
    @Test
    public final void testPlotActivity() {
        new PlotUtils(); // Coverage
        final JPanel p = PlotUtils.plotActivity();
        assertEquals(null, p);
    }

}
