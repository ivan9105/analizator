package com.sberbank.analizator;

import com.sun.istack.internal.Nullable;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {
    private Map<String, String> expressionMap = new HashMap<String, String>();
    private String expression;
    private int count = 0;

    public App(String expression) {
        this.expression = expression.replaceAll(" ", "").toLowerCase();
    }

    public void evaluate() {
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
            closeBracket = closedBracketsPos.get(shift);

            if (closeBracket >= 0) {
                String expression_ = expression.substring(openBracket, shift == 0 ? closeBracket + 1 : closeBracket);
                expression = expression.replace(expression_, arg);
                System.out.println(expression);
                expressionMap.put(arg, expression_);

                //Todo same method with input expression and arg name A0 = A1 + A2
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

        }
    }

    private boolean isCompleteExpression(String expression) {
        return !expression.matches(".*[+-/()*]+.*");
    }

    public static void main(String[] args) {
        String expression = "(5 + 4 + (5 * 2 + (4 + 1))) + (5 + 2) + 5 * 2";
        App app = new App(expression);
        app.evaluate();
    }
}
