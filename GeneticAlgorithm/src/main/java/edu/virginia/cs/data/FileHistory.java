/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.data;

import java.io.File;
import java.io.Serializable;

/**
 * History implementer that makes history available via a file
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @param <T> Type of item to maintain a history of
 * @since Jun 28, 2011
 */
public class FileHistory<T extends Serializable> implements History<T> {

    private final File _file;
    private int _numItems = 0;

    /**
     * @param file Where the history should be stored
     */
    public FileHistory(final File file) {
        _file = file;
        if (_file.exists()) {

        }
    }

    /**
     * @see edu.virginia.cs.data.History#add(java.lang.Object)
     */
    @Override
    public boolean add(final T item) {
        // TODO Auto-generated method stub
        ++_numItems;
        return true;
    }

    /**
     * @see edu.virginia.cs.data.History#get(int)
     */
    @Override
    public T get(final int i) {
        // TODO Auto-generated method stub
        return null;
    }

}
