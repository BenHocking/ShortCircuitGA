/*
 * Copyright (c) 2012 Ashlie B. Hocking All Rights reserved.
 */
package edu.virginia.cs.common.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Trivial Adapter for using a StringBuffer as an OutputStream
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie B. Hocking</a>
 * @since Mar 9, 2012
 */
public class StringBufferOutputStream extends OutputStream {
    private final StringBuffer _buff;

    /**
     * @param buff StringBuffer that the output stream will write to
     */
    public StringBufferOutputStream(final StringBuffer buff) {
        _buff = buff;
    }

    /*
     * (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(final int arg0) throws IOException {
        _buff.append((char) arg0);
    }

}
