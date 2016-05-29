package com.sberbank.analizator.interpreter;

import com.sberbank.analizator.ShuntingJardAlgorithm;
import com.sberbank.analizator.lexer.Lexer;
import com.sberbank.analizator.math.Effectiveness;
import com.sberbank.analizator.math.Function;

import java.util.*;

/**
 * @author Иван
 * @version $Id$
 */
public class ReversePolishNotationEvaluator {
    private LinkedList<String> stack = new LinkedList<String>();
    private String expression;
    private List<Function> functions;

    public ReversePolishNotationEvaluator(String expression) {
        this.expression = expression;
        this.functions = new ArrayList<Function>();

        functions.add(new Function("sin", new Effectiveness() {
            public Double getResult(Double firstOperand) {
                return Math.sin(firstOperand);
            }
        }));

        functions.add(new Function("cos", new Effectiveness() {
            public Double getResult(Double firstOperand) {
                return Math.cos(firstOperand);
            }
        }));

        functions.add(new Function("atan", new Effectiveness() {
            public Double getResult(Double firstOperand) {
                return Math.atan(firstOperand);
            }
        }));

        functions.add(new Function("ln", new Effectiveness() {
            public Double getResult(Double firstOperand) {
                return Math.log(firstOperand);
            }
        }));

        functions.add(new Function("exp", new Effectiveness() {
            public Double getResult(Double firstOperand) {
                return Math.exp(firstOperand);
            }
        }));
    }

    public Double evaluate() {
        System.out.println(expression);

        String code[] = expression.split(" ");
        String instruction;
        double firstOperand, secondOperand;
        int instructionPointer = 0;

        while (instructionPointer < code.length && instructionPointer > -1) {
            instruction = code[instructionPointer++];
            if (isNumber(instruction)) {
                stack.push(instruction);
            } else {
                if (instruction.equals("^")) {
                    secondOperand = parseDouble(stack.pop());
                    firstOperand = parseDouble(stack.pop());
                    stack.push("" + Math.pow(firstOperand, secondOperand));
                } else if (instruction.equals("/")) {
                    secondOperand = parseDouble(stack.pop());
                    firstOperand = parseDouble(stack.pop());
                    stack.push("" + (firstOperand / secondOperand));
                } else if (instruction.equals("*")) {
                    secondOperand = parseDouble(stack.pop());
                    firstOperand = parseDouble(stack.pop());
                    stack.push("" + (firstOperand * secondOperand));
                } else if (instruction.equals("+")) {
                    secondOperand = parseDouble(stack.pop());
                    firstOperand = parseDouble(stack.pop());
                    stack.push("" + (firstOperand + secondOperand));
                } else if (instruction.equals("-")) {
                    secondOperand = parseDouble(stack.pop());
                    if (stack.peek() != null && isNumber(stack.peek())) { // unary minus
                        firstOperand = parseDouble(stack.pop());
                        stack.push("" + (firstOperand - secondOperand));
                    } else {
                        stack.push("" + (-secondOperand));
                    }
                } else if (isFunction(instruction)) {
                    stack.push("" + evaluateFunction(parseDouble(stack.pop()), instruction));
                } else {
                    throw new RuntimeException("Unknown instruction: " + instruction);
                }
            }
        }
        return parseDouble(stack.pop());
    }

    private Double parseDouble(String token) {
        return Double.parseDouble(token);
    }

    private boolean isNumber(String str) {
        return str.matches(Lexer.CONST);
    }

    private boolean isFunction(String str) {
        for (Function function : functions) {
            if (function.getName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private Double evaluateFunction(Double firstOperand, String str) {
        for (Function function : functions) {
            if (function.getName().equals(str)) {
                return function.getResult(firstOperand);
            }
        }
        return 0d;
    }

    public static void main(String[] args) {
        System.out.println(new ReversePolishNotationEvaluator(new ShuntingJardAlgorithm("3.4+4*2/sin(1-5)^2^3").evaluate()).evaluate());
    }
}
