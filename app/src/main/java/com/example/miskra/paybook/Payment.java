package com.example.miskra.paybook;

import java.util.Date;

/**
 * Created by miskra on 26.05.2017.
 */

public class Payment {
    private String paymentId, product, type, shop, amount, date;
    private int popId;

    public Payment() {
    }

    public Payment(String type, String shop, String amount, String date) {

        this.type = type;
        this.shop = shop;
        this.amount = amount;
        this.date = date;
        
    }

    public Payment(String product, String type, String shop, String amount, String date){
        Long timeLong = System.currentTimeMillis()/1000;
        this.paymentId = timeLong.toString();
        this.product = product;
        this.type = type;
        this.shop = shop;
        this.amount = amount;
        this.date = date;
        this.popId = 0;
    }

    public Payment(String product, String type, String shop, String amount, String date, int popId){
        Long timeLong = System.currentTimeMillis()/1000;
        this.paymentId = timeLong.toString();
        this.product = product;
        this.type = type;
        this.shop = shop;
        this.amount = amount;
        this.date = date;
        this.popId = popId;
    }

    public String getPaymentId() {
        return paymentId;
    }
    public String getProduct() {
        return product;
    }
    public String getType() {
        return type;
    }
    public String getShop() {
        return shop;
    }
    public String getAmount() { return amount; }
    public String getDate() {
        return date;
    }
    public int getPopId() {
        return popId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public void setProduct(String product) { this.product = product; }
    public void setType(String type) {
        this.type = type;
    }
    public void setShop(String shop) {
        this.shop = shop;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public void setDate(String date) { this.date = date; }
    public void setPopId(int popId) { this.popId = popId; }

}
