package com.sberbank.analizator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Collections;

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

        expression = "sqrt(2+2)";
        evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "2.0");

        expression = "sqrt(sqrt(4)+sqrt(4)) + sqrt(sqrt(sqrt(sqrt(sqrt(1)))))";
        evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "3.0");

        expression = "((2+2) + (2+2) + (2+2))";
        evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "12.0");

        expression = "((-2-2) + (-2-2) + (-2-2))";
        evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "-12.0");

        expression = "-2-(-2)";
        evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "0.0");

        expression = "sqrt((-2-(-2)) + 1)";
        evaluater = new Evaluater(expression, true);
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "1.0");

        //My function example
        expression = "multipleByTwo(multipleByTwo(2))";
        evaluater = new Evaluater(expression, true);
        evaluater.getFunctions().add(new Function() {
            public String getName() {
                return "multipleByTwo";
            }

            public String getValue(String value) {
                Double value_ = Double.valueOf(value);
                value_ = value_ * 2;
                value = String.valueOf(value_);
                return value;
            }
        });
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "8.0");

        boolean isException = false;

        try {
            expression = "()()";
            evaluater = new Evaluater(expression, true);
            evaluater.evaluate();
            assertEquals(String.valueOf(evaluater.execute()), "1.0");
        } catch (EvaluaterException e) {
            isException = true;
        }

        assertTrue(isException);

        expression = "G55 + G33 + sqrt(G4)";
        evaluater = new Evaluater(expression, true);
        evaluater.getArgumentsMap().put("G55", "130");
        evaluater.getArgumentsMap().put("G33", "270");
        evaluater.getArgumentsMap().put("G4", "4");
        evaluater.evaluate();
        assertEquals(String.valueOf(evaluater.execute()), "402.0");
    }
}
