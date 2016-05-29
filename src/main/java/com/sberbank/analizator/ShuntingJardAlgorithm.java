package com.sberbank.analizator;

import com.sberbank.analizator.lexer.Lexer;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

/**
 * @author Иван
 * @version $Id$
 */
public class ShuntingJardAlgorithm {
    private StringBuilder output;
    private Stack<String> stack;
    private String expression;
    private Lexer lexer;

    private HashSet<String> functions = new HashSet<String>();

    private enum Operator {
        ADD(3, Operator.LEFT),
        SUBTRACT(3, Operator.LEFT),
        MULTIPLY(4, Operator.LEFT),
        DIVIDE(4, Operator.LEFT),
        POWER(5, Operator.RIGHT);

        static final int LEFT = 0;
        static final int RIGHT = 1;

        final int precedence;
        final int associativity;

        Operator(int p, int a) {
            precedence = p;
            associativity = a;
        }
    }

    private Map<String, Operator> ops = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
        put("^", Operator.POWER);
    }};

    public ShuntingJardAlgorithm(String expression) {
        this.output = new StringBuilder();
        this.stack = new Stack<String>();
        this.expression = expression;
        this.lexer = new Lexer(new ByteArrayInputStream(this.expression.getBytes()));

        functions.add("sin");
        functions.add("cos");
        functions.add("atan");
        functions.add("ln");
        functions.add("exp");
        functions.add("ack");
    }

    public String evaluate() {
        int id;
        String token;
        while ((id = lexer.nextSymbol()) != Lexer.EOF) {
            token = lexer.stringfy(id);

            if (isNumber(token)) {
                output.append(token).append(' ');
            }

            if (isFunction(token)) {
                stack.push(token);
            }

            if (isFunctionSeparator(token)) {
                while (stack.peek().charAt(0) != '(') {
                    output.append(stack.pop()).append(' ');
                }
            }

            if (isOperator(token)) {
                while (!stack.isEmpty() && isOperator(stack.peek()) && isHigherPrecedence(token)) {
                    output.append(stack.pop()).append(' ');
                }
                stack.push(token);
            }

            if (token.charAt(0) == '(') {
                stack.push(token);
            }

            if (token.charAt(0) == ')') {
                try {
                    while (!stack.isEmpty() && stack.peek().charAt(0) != '(') {
                        output.append(stack.pop()).append(' ');
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Mismatched parenthesis");
                }
                if (stack.isEmpty())
                    throw new RuntimeException("Mismatched parenthesis");
                stack.pop();
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek().charAt(0) == ')' || stack.peek().charAt(0) == '(') {
                throw new RuntimeException("Mismatched parenthesis");
            }
            output.append(stack.pop()).append(' ');
        }

        return output.toString();
    }

    private boolean isNumber(String str) {
        return str.matches(Lexer.CONST);
    }

    private boolean isFunction(String token) {
        return functions.contains(token);
    }

    private boolean isFunctionSeparator(String token) {
        return token.equals(",");
    }

    private boolean isOperator(String token) {
        return ops.containsKey(token);
    }

    private boolean isHigherPrecedence(String token) {
        return isLeftAssociative(token) && precedence(token) <= precedence(stack.peek())
                || isRightAssociative(token) && precedence(token) < precedence(stack.peek());
    }

    private int precedence(String token) {
        return (ops.get(token) != null) ? ops.get(token).precedence : -1;
    }

    private boolean isLeftAssociative(String token) {
        return ops.get(token) != null && ops.get(token).associativity == Operator.LEFT;
    }

    private boolean isRightAssociative(String token) {
        return ops.get(token) != null && ops.get(token).associativity == Operator.RIGHT;
    }

    public static void main(String[] args) {
        String expression = "3.4+4*2/(1-5)^2^3";
        System.out.println(new ShuntingJardAlgorithm(expression).evaluate());
    }
}