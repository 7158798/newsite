package com.ruizton.main.helper;

/**
 * Created by sunpeng on 2016/5/31 0031.
 */
public class TurnOverHelp {

    private String name;
    private double amount;

    public TurnOverHelp(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
