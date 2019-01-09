package com.example.miskra.paybook;

/**
 * Created by miskra on 10.06.2017.
 */

public class PaymentMonthly {
    private String type;
    private double amount;
    private int year;
    private int month;

    public PaymentMonthly(){

    }

    public PaymentMonthly(String type, double amount, int year, int month){
        this.type = type;
        this.amount = amount;
        this.year = year;
        this.month = month;
    }


    public String getType() {
        return type;
    }
    public double getAmount() { return amount; }
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setAmount(double amount) { this.amount = amount; }
    public void setYear(int year) { this.year = year; }
    public void setMonth(int month) { this.month = month; }
}
