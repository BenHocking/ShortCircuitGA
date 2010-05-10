/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.common;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Adapter class between a single instance of E and a List of that single instance
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 5, 2010
 */
public final class SingleItemList<E> implements List<E> {

    final E _val;

    /**
     * Constructor: takes single item which makes up this "list"
     */
    public SingleItemList(final E singleItem) {
        _val = singleItem;
    }

    /**
     * @see java.util.List#add(java.lang.Object)
     */
    @Override
    public boolean add(final E e) {
        throw new RuntimeException("Cannot add items to a SingleItemList");
    }

    /**
     * @see java.util.List#add(int, java.lang.Object)
     */
    @Override
    public void add(final int index, final E element) {
        throw new RuntimeException("Cannot add items to a SingleItemList");
    }

    /**
     * @see java.util.List#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        throw new RuntimeException("Cannot add items to a SingleItemList");
    }

    /**
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        throw new RuntimeException("Cannot add items to a SingleItemList");
    }

    /**
     * @see java.util.List#clear()
     */
    @Override
    public void clear() {
        throw new RuntimeException("Cannot clear a SingleItemList");
    }

    /**
     * @see java.util.List#contains(java.lang.Object)
     */
    @Override
    public boolean contains(final Object o) {
        return EqualUtils.eq(o, _val);
    }

    /**
     * @see java.util.List#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(final Collection<?> c) {
        return c.size() == 0 || (c.size() == 1 && EqualUtils.eq(c.toArray()[0], _val));
    }

    /**
     * @see java.util.List#get(int)
     */
    @Override
    public E get(final int index) {
        return _val;
    }

    /**
     * @see java.util.List#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(final Object o) {
        return contains(o) ? 0 : -1;
    }

    /**
     * @see java.util.List#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * @see java.util.List#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        return new NonIteratingIterator();
    }

    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    @Override
    public int lastIndexOf(final Object o) {
        return indexOf(o);
    }

    /**
     * @see java.util.List#listIterator()
     */
    @Override
    public ListIterator<E> listIterator() {
        return new NonIteratingIterator();
    }

    /**
     * @see java.util.List#listIterator(int)
     */
    @Override
    public ListIterator<E> listIterator(final int index) {
        return new NonIteratingIterator();
    }

    /**
     * @see java.util.List#remove(java.lang.Object)
     */
    @Override
    public boolean remove(final Object o) {
        throw new RuntimeException("Cannot remove items from a SingleItemList");
    }

    /**
     * @see java.util.List#remove(int)
     */
    @Override
    public E remove(final int index) {
        throw new RuntimeException("Cannot remove items from a SingleItemList");
    }

    /**
     * @see java.util.List#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new RuntimeException("Cannot remove items from a SingleItemList");
    }

    /**
     * @see java.util.List#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new RuntimeException("Cannot remove items from a SingleItemList");
    }

    /**
     * @see java.util.List#set(int, java.lang.Object)
     */
    @Override
    public E set(final int index, final E element) {
        throw new RuntimeException("Cannot change items in a SingleItemList");
    }

    /**
     * @see java.util.List#size()
     */
    @Override
    public int size() {
        return 1;
    }

    /**
     * @see java.util.List#subList(int, int)
     */
    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        if (fromIndex < 0 || toIndex < fromIndex || 0 < toIndex)
            throw new IndexOutOfBoundsException("SingleItemList only has itself as a subList");
        return this;
    }

    /**
     * @see java.util.List#toArray()
     */
    @Override
    public Object[] toArray() {
        final Object[] retval = {
            _val
        };
        return retval;
    }

    /**
     * @see java.util.List#toArray(T[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(final T[] a) {
        if (a.length > 0) {
            a[0] = (T) _val;
            return a;
        }
        final Class<? extends T[]> newType = (Class<? extends T[]>) a.getClass();
        final T[] copy = ((Object) newType == (Object) Object[].class) ? (T[]) new Object[1]
                                                                      : (T[]) Array.newInstance(newType.getComponentType(), 1);
        copy[0] = (T) _val;
        return copy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object o) {
        return o != null && getClass().equals(o.getClass()) && EqualUtils.eq(_val, ((SingleItemList<E>) o)._val);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return _val.hashCode();
    }

    private class NonIteratingIterator implements ListIterator<E>, Iterator<E> {

        private boolean _hasIterated = false;

        /**
         * @see java.util.ListIterator#add(java.lang.Object)
         */
        @Override
        public void add(final E e) {
            throw new RuntimeException("Cannot add items to a NonIteratingIterator");
        }

        /**
         * @see java.util.ListIterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return !_hasIterated;
        }

        /**
         * @see java.util.ListIterator#hasPrevious()
         */
        @Override
        public boolean hasPrevious() {
            return !_hasIterated;
        }

        /**
         * @see java.util.ListIterator#next()
         */
        @Override
        public E next() {
            _hasIterated = true;
            return _val;
        }

        /**
         * @see java.util.ListIterator#nextIndex()
         */
        @Override
        public int nextIndex() {
            return 0;
        }

        /**
         * @see java.util.ListIterator#previous()
         */
        @Override
        public E previous() {
            _hasIterated = true;
            return _val;
        }

        /**
         * @see java.util.ListIterator#previousIndex()
         */
        @Override
        public int previousIndex() {
            return 0;
        }

        /**
         * @see java.util.ListIterator#remove()
         */
        @Override
        public void remove() {
            throw new RuntimeException("Cannot remove items from a NonIteratingIterator");
        }

        /**
         * @see java.util.ListIterator#set(java.lang.Object)
         */
        @Override
        public void set(final E e) {
            throw new RuntimeException("Cannot change items in a NonIteratingIterator");
        }

    }
}
