/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.data;

import java.io.File;
import java.net.URISyntaxException;
import org.junit.Test;

/**
 * File loader for files used in tests
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 19, 2011
 */
public class TestFileLoader {

    @Test
    public void needTest() {
       // FIXME
    }

    /**
     * @param fileName name of file name residing in the data directory
     * @return File object referencing desired file
     * @throws URISyntaxException if fileName cannot be resolved as a URI
     */
    public static File getFile(final String fileName) throws URISyntaxException {
        return new File(getDataDirectory(), fileName);
    }

    /**
     * @return File object referencing the directory holding this class
     * @throws URISyntaxException if fileName cannot be resolved as a URI
     */
    public static File getDataDirectory() throws URISyntaxException {
        return new File(TestFileLoader.class.getResource(".").toURI());
    }

}
