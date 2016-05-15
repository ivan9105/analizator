package com.sberbank.analizator;

/**
 * Created by HAULMONT on 15.05.2016.
 */
public class SqrtFunction implements Function {
    private String name;

    public SqrtFunction() {
        this.name = "sqrt";
    }

    public String getValue(String value) {
        Double value_ = Double.valueOf(value);
        value_ = Math.sqrt(value_);
        value = String.valueOf(value_);

        return value;
    }

    public String getName() {
        return name;
    }
}
