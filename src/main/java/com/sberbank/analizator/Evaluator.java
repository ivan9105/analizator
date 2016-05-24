package com.sberbank.analizator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Иван
 * @version $Id$
 */
public class Evaluator {
    // Associativity constants for operators
    private static final int LEFT_ASSOC = 0;
    private static final int RIGHT_ASSOC = 1;

    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();

    static {
        OPERATORS.put("+", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("-", new int[]{0, LEFT_ASSOC});
        OPERATORS.put("*", new int[]{5, LEFT_ASSOC});
        OPERATORS.put("/", new int[]{5, LEFT_ASSOC});
    }

    private static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }

    // Test associativity of operator token
    private static boolean isAssociative(String token, int type) {
        if (!isOperator(token)) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }

        if (OPERATORS.get(token)[1] == type) {
            return true;
        }
        return false;
    }

    // Compare precedence of operators.
    private static final int cmpPrecedence(String token1, String token2) {
        if (!isOperator(token1) || !isOperator(token2)) {
            throw new IllegalArgumentException("Invalid tokens: " + token1
                    + " " + token2);
        }
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }

    // Convert infix expression format into reverse Polish notation
    public static String[] infixToRPN(String[] inputTokens) {
        ArrayList<String> out = new ArrayList<String>();

        Stack<String> stack = new Stack<String>();
        for (String token : inputTokens) {
            if (isOperator(token)) {
                while (!stack.empty() && isOperator(stack.peek())) {
                    if ((isAssociative(token, LEFT_ASSOC) &&
                            cmpPrecedence(token, stack.peek()) <= 0) ||
                            (isAssociative(token, RIGHT_ASSOC) &&
                                    cmpPrecedence(token, stack.peek()) < 0)) {
                        //if next priority > pop upper item in out
                        out.add(stack.pop());
                        continue;
                    }
                    break;
                }
                //push the new operator on the stack on up
                stack.push(token);
            } else if (token.equals("(")) {
                // If token is a left bracket '('
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                stack.pop();
            } else {
                out.add(token);
            }
        }

        while (!stack.empty()) {
            out.add(stack.pop());
        }
        String[] output = new String[out.size()];

        return out.toArray(output);
    }

    //The single value remaining on the stack is the evaluation. Java code for this is as follows
    public static double RPNtoDouble(String[] tokens) {
        Stack<String> stack = new Stack<String>();
        for (String token : tokens) {
            if (!isOperator(token)) {
                stack.push(token);
            } else {
                Double d2 = Double.valueOf(stack.pop());
                Double d1 = Double.valueOf(stack.pop());

                Double result = token.compareTo("+") == 0 ? d1 + d2 :
                        token.compareTo("-") == 0 ? d1 - d2 :
                                token.compareTo("*") == 0 ? d1 * d2 :
                                        d1 / d2;
                stack.push(String.valueOf(result));
            }
        }
        return Double.valueOf(stack.pop());
    }

    public static void main(String[] args) {
        String[] input = "( 1 + 2 ) * ( 3 / 4 ) - ( 5 + 6 )".split(" ");
        String[] output = infixToRPN(input);
        for (String token : output) {
            System.out.print(token + " ");
        }
        Double result = RPNtoDouble(output);
        System.out.println("= " + result);
    }
}
