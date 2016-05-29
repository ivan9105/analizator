package com.sberbank.analizator.math;

/**
 * @author Иван
 * @version $Id$
 */
public class Function {
    private String name;
    private Effectiveness effectiveness;

    public Function(String name, Effectiveness effectiveness) {
        this.name = name;
        this.effectiveness = effectiveness;
    }

    public Double getResult(Double firstOperand) {
        return effectiveness.getResult(firstOperand);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
