package com.sberbank.analizator;

import java.util.*;

/**
 * Hello world!
 */
public class Evaluater {
    public static final String A = "ARG";
    public static final String M = "MNS";
    private Map<String, String> expressionMap = new HashMap<String, String>();
    private Map<String, String> argumentsMap = new HashMap<String, String>();
    private Map<String, Function> modifierMap = new HashMap<String, Function>();
    private String expression;
    private int count = 0;
    private Map<String, String> replacementMap = new HashMap<String, String>();
    private List<Function> functions = new ArrayList<Function>();
    private boolean modified;

    public Evaluater(String expression, boolean modified) {
        this.expression = expression;
        this.modified = modified;
        if (modified) {
            this.expression = expression.replaceAll(" ", "").toLowerCase();
        }
        this.functions.add(new SqrtFunction());
    }

    public void evaluate() {
        if (modified) {
            int count = 0;
            String word = "Z";

            for (Function function : getFunctions()) {
                String name = function.getName().toLowerCase();
                String replacement = word + count++;
                this.expression = expression.replace(name, replacement);
                replacementMap.put(replacement, name);
            }

            for (String argument : getArgumentsMap().keySet()) {
                String replacement = word + count++;
                this.expression = expression.replace(argument.toLowerCase(), replacement);
                replacementMap.put(replacement, argument.toLowerCase());
            }

            this.expression = expression.replaceAll("[^0123456789Z+-/*()]", "");

            for (Map.Entry<String, String> entry : replacementMap.entrySet()) {
                this.expression = expression.replace(entry.getKey(), entry.getValue());
            }

            modified = false;
        }

        if (argumentsMap.size() > 0) {
            for (Map.Entry<String, String> entry : argumentsMap.entrySet()) {
                if (expression.contains(entry.getKey().toLowerCase())) {
                    this.expression = expression.replace(entry.getKey().toLowerCase(), entry.getValue());
                }
            }
        }
        argumentsMap.clear();

        int openBracket = expression.indexOf("(");
        if (openBracket >= 0) {
            String arg = A + count;

            List<Integer> openBracketsPos = new ArrayList<Integer>();
            char[] chars = expression.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    openBracketsPos.add(i);
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
                //check functions
                for (Function function : getFunctions()) {
                    String name = function.getName().toLowerCase();
                    if (openBracket - name.length() >= 0) {
                        String preBracket = expression.substring(openBracket - name.length(), openBracket);
                        if (name.equals(preBracket)) {
                            modifierMap.put(arg, function);
                            break;
                        }
                    }
                }

                String expression_ = expression.substring(openBracket, closeBracket + 1);

                if (modifierMap.get(arg) != null) {
                    expression = expression.substring(0, openBracket -
                            modifierMap.get(arg).getName().length()) + arg + expression.substring(closeBracket + 1, expression.length());
                } else {
                    expression = expression.substring(0, openBracket) + arg + expression.substring(closeBracket + 1, expression.length());
                }

                expression_ = expression_.substring(1, expression_.length() - 1);

                //for single negative numbers
                if (expression_.contains("-") && expression_.split("-")[0].equals("")) {
                    expression_ = M + expression_.substring(1, expression_.length());
                }

                expressionMap.put(arg, expression_);
                count++;

                if (!isCompleteExpression(expression)) {
                    evaluate();
                }
            } else {
                throw new EvaluaterException("Check out the brackets");
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
            expressionMap.put(arg, expression_);
            count++;

            if (!isCompleteExpression(expression)) {
                evaluate();
            }
        }
    }

    public Double execute() {
        Map<String, String> nextArgs = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : expressionMap.entrySet()) {
            String expression = entry.getValue();

            if (isComplexOperation(expression)) {
                Evaluater evaluater = new Evaluater(expression, false);
                evaluater.setCount(count++);
                evaluater.setFunctions(getFunctions());
                evaluater.evaluate();
                getModifierMap().putAll(evaluater.getModifierMap());

                Map<String, String> expressionMap = evaluater.getExpressionMap();

                List<String> list = new ArrayList<String>(expressionMap.keySet());
                Collections.sort(list);

                String key = list.get(list.size() - 1);
                String expression_ = expressionMap.get(key);
                expressionMap.remove(key);
                nextArgs.putAll(expressionMap);
                entry.setValue(expression_);

                count = evaluater.getCount();
            }
        }

        expressionMap.putAll(nextArgs);

        calculate();
        execute_();

        Object[] objects = expressionMap.values().toArray();
        if (objects.length == 0) {
            throw new EvaluaterException("The expression is not valid");
        }

        String result = ((String) objects[0]).replaceAll(M, "-");
        return Double.valueOf(result);
    }

    private void calculate() {
        for (Map.Entry<String, String> entry : expressionMap.entrySet()) {
            String expression_ = entry.getValue();
            String key = entry.getKey();
            Function function = modifierMap.get(key);

            if (!isExistsArguments(expression_)) {
                if (expression_.contains("*")) {
                    String arg[] = expression_.split("\\*");
                    if (checkArgs(arg)) {
                        Double a1 = Double.valueOf(arg[0].replaceAll(M, "-"));
                        Double a2 = Double.valueOf(arg[1].replaceAll(M, "-"));

                        Double value = a1 * a2;

                        String valueStr = String.valueOf(value);
                        if (function != null) {
                            valueStr = function.getValue(valueStr);
                            modifierMap.remove(key);
                        }

                        if (value < 0) {
                            valueStr = valueStr.replace("-", M);
                        }

                        entry.setValue(valueStr);
                    }
                } else if (expression_.contains("+")) {
                    String arg[] = expression_.split("\\+");
                    if (checkArgs(arg)) {
                        Double a1 = Double.valueOf(arg[0].replaceAll(M, "-"));
                        Double a2 = Double.valueOf(arg[1].replaceAll(M, "-"));

                        Double value = a1 + a2;

                        String valueStr = String.valueOf(value);
                        if (function != null) {
                            valueStr = function.getValue(valueStr);
                            modifierMap.remove(key);
                        }

                        if (value < 0) {
                            valueStr = valueStr.replace("-", M);
                        }
                        entry.setValue(valueStr);
                    }
                } else if (expression_.contains("-")) {
                    String arg[] = expression_.split("\\-");
                    if (checkArgs(arg)) {
                        Double a1 = Double.valueOf(arg[0].replaceAll(M, "-"));
                        Double a2 = Double.valueOf(arg[1].replaceAll(M, "-"));

                        Double value = a1 - a2;

                        String valueStr = String.valueOf(value);
                        if (function != null) {
                            valueStr = function.getValue(valueStr);
                            modifierMap.remove(key);
                        }

                        if (value < 0) {
                            valueStr = valueStr.replace("-", M);
                        }
                        entry.setValue(valueStr);
                    }
                } else if (expression_.contains("/")) {
                    String arg[] = expression_.split("/");
                    if (checkArgs(arg)) {
                        Double a1 = Double.valueOf(arg[0].replaceAll(M, "-"));
                        Double a2 = Double.valueOf(arg[1].replaceAll(M, "-"));

                        Double value = a1 / a2;

                        String valueStr = String.valueOf(value);
                        if (function != null) {
                            valueStr = function.getValue(valueStr);
                            modifierMap.remove(key);
                        }

                        if (value < 0) {
                            valueStr = valueStr.replace("-", M);
                        }
                        entry.setValue(valueStr);
                    }
                } else if (function != null) {
                    entry.setValue(function.getValue(expression_));
                }
            }
        }
    }

    private boolean checkArgs(String[] arg) {
        return !arg[0].equals("") && !arg[1].equals("");
    }

    private void execute_() {
        Map<String, String> processMap = new HashMap<String, String>(expressionMap);

        if (processMap.size() > 1) {
            for (Map.Entry<String, String> entry : expressionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (!isExistsArguments(value)) {
                    for (Map.Entry<String, String> entry_ : processMap.entrySet()) {
                        if (!entry_.getKey().equals(key) && entry_.getValue().contains(key)) {
                            entry_.setValue(entry_.getValue().replace(key, value.replace("-", M)));
                        }
                    }
                    processMap.remove(key);
                }
            }

            expressionMap.clear();
            expressionMap.putAll(processMap);
        }

        calculate();

        if (expressionMap.size() > 1) {
            execute_();
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
        if (expression == null) {
            return false;
        }

        return !expression.matches(".*[+-/()*]+.*");
    }

    private boolean isExistsArguments(String expression) {
        return expression.matches(".*[" + A + "]+.*");
    }

    private boolean isComplexOperation(String expression) {
        String dummy = replaceAllOperation(expression);
        int count = 0;
        for (int i = 0; i < dummy.toCharArray().length; i++) {
            if (dummy.toCharArray()[i] == 'D') {
                count++;
            }
        }

        return count > 1;
    }

    private String replaceAllOperation(String expression) {
        return expression
                .replaceAll("\\+", "D")
                .replaceAll("\\-", "D")
                .replaceAll("/", "D")
                .replaceAll("\\*", "D");
    }

    private boolean isDigits(char symbol) {
        return String.valueOf(symbol).matches("[1234567890" + A + "]");
    }

    public Map<String, String> getExpressionMap() {
        return expressionMap;
    }

    public Map<String, String> getArgumentsMap() {
        return argumentsMap;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public Map<String, Function> getModifierMap() {
        return modifierMap;
    }
}
