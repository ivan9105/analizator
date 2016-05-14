package com.sberbank.analizator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple Evaluater.
 */
public class EvaluaterTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EvaluaterTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( EvaluaterTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        String expression = "2 + 2 * 2 + (4 / 2 * 2)";
        Evaluater evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "7.0");

        expression = "(5 + 4 + (5 * 2 + (4 + 1)))";
        evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "24.0");

        expression = "8/2/2";
        evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "2.0");

        expression = "8 + 4 * 2 - (16 - (16 - (16 + 195))) * (-1)";
        evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "227.0");

        expression = "(-1)*(-1)*(-1)";
        evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "-1.0");

        assertTrue( true );
    }
}
