/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Utilities for testing
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 8, 2011
 */
public class TestUtils {

    public static Method getGetter(final Class<?> clz, final String propertyName) throws NoSuchMethodException {
        final Class<?> getterArgs[] = new Class<?>[0];
        final String getterName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        return clz.getMethod(getterName, getterArgs);
    }

    public static Method getSetter(final Class<?> clz, final String propertyName, final Class<?> propertyClass)
            throws NoSuchMethodException {
        final Class<?> setterArgs[] = new Class[1];
        setterArgs[0] = propertyClass;
        final String setterName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        return clz.getMethod(setterName, setterArgs);
    }

    public static void testEqualityProperty(final Object obj1, final Object obj2, final String propertyToChange, final Object val1,
                                            final Object val2, final boolean affectsEquality) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        final Class<?> clz = obj1.getClass();
        final Method getter = getGetter(clz, propertyToChange);
        final Object saveVal = getter.invoke(obj1);
        final Method setter = getSetter(clz, propertyToChange, val1.getClass());
        final Object setterArgs[] = new Object[1];
        setterArgs[0] = val1;
        setter.invoke(obj1, setterArgs);
        setterArgs[0] = val2;
        setter.invoke(obj2, setterArgs);
        // If affectsEquality, then these should no longer be equal
        // If not, then they should still be equal
        assertEquals(!affectsEquality, obj1.equals(obj2));
        assertEquals(!affectsEquality, obj2.equals(obj1));
        setterArgs[0] = null;
        setter.invoke(obj2, setterArgs);
        assertEquals(!affectsEquality, obj1.equals(obj2));
        assertEquals(!affectsEquality, obj2.equals(obj1));
        setterArgs[0] = saveVal;
        setter.invoke(obj1, setterArgs);
        setter.invoke(obj2, setterArgs);
    }
}
