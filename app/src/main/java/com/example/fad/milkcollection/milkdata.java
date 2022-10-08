package com.example.fad.milkcollection;

import java.util.Date;

public class milkdata {
    public  String  getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getSnf() {
        return snf;
    }

    public void setSnf(String snf) {
        this.snf = snf;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    private String  slno;
    private Date date;
    private String shift;
    private String code;
    private String name;
    private String mtype;
    private String quantity;
    private String fat;
    private String snf;
    private String rate;
    private String amount;
    public milkdata(String slno,Date date,String shift,String code,String name,String mtype,String quantity,String fat,String snf,String rate,String amount){
        this.slno=slno;
        this.date=date;
        this.shift=shift;
        this.code= code;
        this.name=name;
        this.mtype=mtype;
        this.quantity=quantity;
        this.fat=fat;
        this.snf=snf;
        this.rate=rate;
        this.amount=amount;
    }

}
