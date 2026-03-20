/*
 * Decompiled with CFR 0.152.
 */
package data.finance.budget;

public class Expense {
    private String name;
    private double amount;

    public Expense(String string, double d) {
        this.name = string;
        this.amount = d;
    }

    public String getName() {
        return this.name;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double d) {
        this.amount = d;
    }
}
