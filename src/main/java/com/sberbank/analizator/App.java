package com.sberbank.analizator;

import java.util.HashMap;
import java.util.Map;
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
            int closeBracket = expression.indexOf(")");
            if (closeBracket >= 0) {
                String expression_ = expression.substring(openBracket, closeBracket + 1);
                expression = expression.replace(expression_, arg);
                System.out.println(expression);
                expressionMap.put(arg, expression_);
                count++;

                if (!isCompleteExpression()) {
                    evaluate();
                }
            } else {
                throw new RuntimeException("WTF");
            }


        } else {

        }
    }

    private boolean isCompleteExpression() {
        return !expression.matches(".*[+-/()*]+.*");
    }

    public static void main(String[] args) {
        String expression = "(5 + 4) + (5 + 2) + 5 * 2";
        App app = new App(expression);
        app.evaluate();
    }
}
