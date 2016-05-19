package com.sberbank.analizator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.regex.Pattern;

/**
 * @author Иван
 * @version $Id$
 */
public class RegexpReceiptTest extends TestCase {
    public RegexpReceiptTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(EvaluaterTest.class);
    }

    public void testApp() {
        //word must be begining on 'a' or nothing and ending on 'b'
        Pattern p = Pattern.compile("a*b");
        assertTrue(p.matcher("aaaaab").matches());
        assertFalse(p.matcher("aaaaa").matches());
        assertTrue(p.matcher("b").matches());

        p = Pattern.compile("x");
        assertTrue(p.matcher("x").matches());

        p = Pattern.compile("\n");
        assertTrue(p.matcher("\n").matches());

        p = Pattern.compile("[^abc]");
        assertTrue(p.matcher("x").matches());

        p = Pattern.compile("[a-d[m-p]]");
        assertTrue(p.matcher("b").matches());
        assertFalse(p.matcher("x").matches());

        //a through z, except for b and c: [ad-z] (subtraction)
        p = Pattern.compile("[a-z&&[^bc]]");
        assertTrue(p.matcher("d").matches());
        assertFalse(p.matcher("b").matches());

        //A digit: [0-9]
        p = Pattern.compile("\\d");
        assertTrue(p.matcher("1").matches());
        assertFalse(p.matcher("s").matches());

        //A non-whitespace character
        p = Pattern.compile("\\S");
        assertTrue(p.matcher("1").matches());
        assertFalse(p.matcher(" ").matches());
    }
}
