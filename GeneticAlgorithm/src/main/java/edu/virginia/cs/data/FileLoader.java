/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.data;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * File loader for files used in tests
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 19, 2011
 */
public class FileLoader {

    /**
     * @param configFile Properties to get file or directory name from
     * @param property Property name of file or directory name
     * @param defaultVal Value to use if property does not exist
     * @return Corresponding File object
     * @throws URISyntaxException If file name is invalid
     */
    public static File getFileFromProperty(final Properties configFile,
                                           final String property, final String defaultVal) throws URISyntaxException {
        String fileName = configFile.getProperty(property, defaultVal);
        if (fileName.contains("[data]")) {
            fileName = fileName.replace("[data]", getDataDirectory().getAbsolutePath());
        }
        final File retval = new File(fileName);
        return retval;
    }

    /**
     * @param fileName name of file name residing in the data directory
     * @return File object referencing desired file
     * @throws URISyntaxException should never happen
     */
    public static File getFile(final String fileName) throws URISyntaxException {
        return new File(getDataDirectory(), fileName);
    }

    /**
     * @return File object referencing the directory holding this class
     * @throws URISyntaxException should never happen
     */
    public static File getDataDirectory() throws URISyntaxException {
        return new File(FileLoader.class.getResource(".").toURI());
    }

}
