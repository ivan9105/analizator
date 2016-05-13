package com.sberbank.analizator;

import com.sun.istack.internal.Nullable;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {
    public static final String A = "A";
    private Map<String, String> expressionMap = new HashMap<String, String>();
    private String expression;
    private int count = 0;

    public App(String expression) {
        this.expression = expression.replaceAll(" ", "").toLowerCase();
    }

    public void evaluate() {
        //squrt first priority with same logic
        //( second priority
        int openBracket = expression.indexOf("(");
        if (openBracket >= 0) {
            String arg = "A" + count;

            List<Integer> openBracketsPos = new ArrayList<Integer>();
            List<Integer> closedBracketsPos = new ArrayList<Integer>();
            char[] chars = expression.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    openBracketsPos.add(i);
                } else if (chars[i] == ')') {
                    closedBracketsPos.add(i);
                }
            }
            int closeBracket = expression.indexOf(")");
            int shift = 0;
            for (Integer bracketsPos : openBracketsPos) {
                if (bracketsPos != openBracket && bracketsPos < closeBracket) {
                    shift++;
                }
                if (bracketsPos > closeBracket) {
                    break;
                }
            }

            openBracket = openBracketsPos.get(shift);

            if (closeBracket >= 0) {
                String expression_ = expression.substring(openBracket, closeBracket + 1);
                expression = expression.replace(expression_, arg);
                System.out.println(expression);
                expressionMap.put(arg, expression_);
                count++;

                if (!isCompleteExpression(expression)) {
                    evaluate();
                }
            } else {
                throw new RuntimeException("WTF");
            }
            return;
        }

        int multiple = expression.indexOf("*");
        if (multiple >= 0) {
            String arg = A + count;
            String leftPart = expression.substring(0, multiple);
            String rightPart = expression.substring(multiple + 1, expression.length());
            String leftArg = getLeftArg(leftPart);
            String rightArg = getRightArg(rightPart);
            String expression_ = leftArg + "*" + rightArg;

            expression = expression.replace(expression_, arg);
            System.out.println(expression);
            expressionMap.put(arg, expression_);
            count++;

            if (!isCompleteExpression(expression)) {
                evaluate();
            }
        }

        int division = expression.indexOf("/");
        if (division >= 0) {
            String arg = A + count;
            String leftPart = expression.substring(0, division);
            String rightPart = expression.substring(division + 1, expression.length());
            String leftArg = getLeftArg(leftPart);
            String rightArg = getRightArg(rightPart);
            String expression_ = leftArg + "/" + rightArg;

            expression = expression.replace(expression_, arg);
            System.out.println(expression);
            expressionMap.put(arg, expression_);
            count++;

            if (!isCompleteExpression(expression)) {
                evaluate();
            }
        }

        int addition = expression.indexOf("+");
        if (addition >= 0) {
            String arg = A + count;
            String leftPart = expression.substring(0, addition);
            String rightPart = expression.substring(addition + 1, expression.length());
            String leftArg = getLeftArg(leftPart);
            String rightArg = getRightArg(rightPart);
            String expression_ = leftArg + "+" + rightArg;

            expression = expression.replace(expression_, arg);
            System.out.println(expression);
            expressionMap.put(arg, expression_);
            count++;

            if (!isCompleteExpression(expression)) {
                evaluate();
            }
        }

        int subtraction = expression.indexOf("-");
        if (subtraction >= 0) {
            String arg = A + count;
            String leftPart = expression.substring(0, subtraction);
            String rightPart = expression.substring(subtraction + 1, expression.length());
            String leftArg = getLeftArg(leftPart);
            String rightArg = getRightArg(rightPart);
            String expression_ = leftArg + "-" + rightArg;

            expression = expression.replace(expression_, arg);
            System.out.println(expression);
            expressionMap.put(arg, expression_);
            count++;

            if (!isCompleteExpression(expression)) {
                evaluate();
            }
        }
    }

    public void evaluate(String arg, String expression) {
        //squrt first priority with same logic
        //( second priority
        int openBracket = expression.indexOf("(");
        if (openBracket >= 0) {
            List<Integer> openBracketsPos = new ArrayList<Integer>();
            List<Integer> closedBracketsPos = new ArrayList<Integer>();
            char[] chars = expression.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    openBracketsPos.add(i);
                } else if (chars[i] == ')') {
                    closedBracketsPos.add(i);
                }
            }
            int closeBracket = expression.indexOf(")");
            int shift = 0;
            for (Integer bracketsPos : openBracketsPos) {
                if (bracketsPos != openBracket && bracketsPos < closeBracket) {
                    shift++;
                }
                if (bracketsPos > closeBracket) {
                    break;
                }
            }

            openBracket = openBracketsPos.get(shift);

            if (closeBracket >= 0) {
                String expression_ = expression.substring(openBracket, closeBracket + 1);
                expression = expression.replace(expression_, arg);
                System.out.println(expression);
                expressionMap.put(arg, expression_);

                count++;

                if (!isCompleteExpression(expression_)) {

                }
            } else {
                throw new RuntimeException("WTF");
            }
            return;
        }

        int multiple = expression.indexOf("*");
        if (multiple >= 0) {
            String leftPart = expression.substring(0, multiple);
            String rightPart = expression.substring(multiple + 1, expression.length());
            String leftArg = getLeftArg(leftPart);
            String rightArg = getRightArg(rightPart);
            String expression_ = leftArg + "*" + rightArg;

            expression = expression.replace(expression_, arg);
            System.out.println(expression);
            expressionMap.put(arg, expression_);

            count++;

            if (!isCompleteExpression(expression_)) {

            }
        }

        int division = expression.indexOf("/");
        if (division >= 0) {
            String leftPart = expression.substring(0, division);
            String rightPart = expression.substring(division + 1, expression.length());
            String leftArg = getLeftArg(leftPart);
            String rightArg = getRightArg(rightPart);
            String expression_ = leftArg + "/" + rightArg;

            expression = expression.replace(expression_, arg);
            System.out.println(expression);
            expressionMap.put(arg, expression_);

            count++;

            if (!isCompleteExpression(expression_)) {

            }
        }

        int addition = expression.indexOf("+");
        if (addition >= 0) {
            String leftPart = expression.substring(0, addition);
            String rightPart = expression.substring(addition + 1, expression.length());
            String leftArg = getLeftArg(leftPart);
            String rightArg = getRightArg(rightPart);
            String expression_ = leftArg + "+" + rightArg;

            expression = expression.replace(expression_, arg);
            System.out.println(expression);
            expressionMap.put(arg, expression_);

            count++;

            if (!isCompleteExpression(expression_)) {

            }
        }

        int subtraction = expression.indexOf("-");
        if (subtraction >= 0) {
            String leftPart = expression.substring(0, subtraction);
            String rightPart = expression.substring(subtraction + 1, expression.length());
            String leftArg = getLeftArg(leftPart);
            String rightArg = getRightArg(rightPart);
            String expression_ = leftArg + "-" + rightArg;

            expression = expression.replace(expression_, arg);
            System.out.println(expression);
            expressionMap.put(arg, expression_);
            count++;
        }
    }

    private String getRightArg(String rightPart) {
        String rightArg = "";
        for (int i = 0; i < rightPart.length(); i++) {
            if (isDigits(rightPart.toCharArray()[i])) {
                rightArg = rightArg + rightPart.toCharArray()[i];
            } else {
                break;
            }
        }
        return rightArg;
    }

    private String getLeftArg(String leftPart) {
        String leftArg = "";
        for (int i = leftPart.length() - 1; i >= 0; i--) {
            if (isDigits(leftPart.toCharArray()[i])) {
                leftArg = leftArg + leftPart.toCharArray()[i];
            } else {
                break;
            }
        }
        return reverseStr(leftArg);
    }

    private String reverseStr(String string) {
        return new StringBuffer(string).reverse().toString();
    }

    private boolean isCompleteExpression(String expression) {
        return !expression.matches(".*[+-/()*]+.*");
    }

    private boolean isDigits(char symbol) {
        return String.valueOf(symbol).matches("[1234567890" + A + "]");
    }

    public static void main(String[] args) {
        String expression = "(5 + 4 + (5 * 2 + (4 + 1))) + (5 + 2) + 5 * 2 + 4 + (4 / 2 + 3) / 3";
        App app = new App(expression);
        app.evaluate();

        //Todo use unit tests for evaulater
    }
}
