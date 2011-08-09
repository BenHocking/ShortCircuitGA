/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.data;

import java.util.ArrayList;

/**
 * History implementer that makes history available via a list
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @param <T> Type of item to maintain a history of
 * @since Jun 28, 2011
 */
public class ListHistory<T> extends ArrayList<T> implements History<T> {

}
