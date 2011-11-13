/*
 * Copyright (c) 2011 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs;

import static org.junit.Assert.*;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * For testing random stuff
 * @author <a href="mailto:benjaminhocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since Feb 26, 2011
 */
public class SandboxTest {
    private interface Fred {
        
    }

    private class Flintstone implements Fred {
        
    }

    private static class GrowingList extends ArrayList<Integer> {

        GrowingList(final int n) {
            for (int i = 0; i < n; ++i) {
                add(i + 1);
            }
        }

        @Override
        public String toString() {
            final StringBuilder retval = new StringBuilder("[");
            for (int i = 0; i < size(); ++i) {
                if (i > 0) retval.append(',');
                retval.append(get(i));
            }
            retval.append(']');
            return retval.toString();
        }
    }

    /**
     * For testing random stuff
     * @throws Exception It's a sandbox!
     */
    @Test
    public void sandbox() throws Exception {
        // final TestEqualityTester tet1 = new TestEqualityTester();
        // final TestEqualityTester tet2 = new TestEqualityTester();
        // final Integer i1 = Integer.valueOf(1);
        // final Integer i2 = Integer.valueOf(2);
        // TestUtils.testEqualityProperty(tet1, tet2, "a", i1, i2, true);
        // TestUtils.testEqualityProperty(tet1, tet2, "b", i1, i2, false);
        System.out.println(Fred.class.toString());
        String s = "&amp; & &nsbp; &tc., &tc. &tc";
        final String regex = "&([^;\\W]*([^;\\w]|$))";
        final String replacement = "&amp;$1";
        final String t = s.replaceAll(regex, replacement).replaceAll(regex, replacement);
        System.out.println(t);
        final String s2 = "&& &&rarr;&";
        System.out.println(s2.replaceAll(regex, replacement).replaceAll(regex, replacement));
        System.out.println("&foo&bar;".replaceAll(regex, replacement).replaceAll(regex, replacement));
        System.out.println("&&".replaceAll(regex, replacement));
        System.out.println("&&&".replaceAll(regex, replacement));
        final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(s);
        if (m.matches()) {
            s = m.replaceAll(replacement);
        }
        System.out.println(s);
        /*
         * final String t = s.replaceAll("&([^;])", "&amp;"); System.out.println(t); final File f = new
         * File("/Users/bhocking/test.dat"); // assertTrue(f.createNewFile()); final ObjectOutputStream writer = new
         * ObjectOutputStream(new FileOutputStream(f)); for (int i = 0; i < 5; ++i) { final Object o = new GrowingList(i);
         * System.out.println(o); writer.writeObject(o); } writer.close(); final ObjectInputStream reader = new
         * ObjectInputStream(new FileInputStream(f)); try { while (true) { // for (int i = 0; i < 5; ++i) { // while
         * (reader.available() > 0) { final GrowingList gl = (GrowingList) reader.readObject(); System.out.println(">" + gl); } }
         * catch (final EOFException e) { } reader.close();
         */
    }
}
