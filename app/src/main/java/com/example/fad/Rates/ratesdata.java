package com.example.fad.Rates;

public class ratesdata {

    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getBcode() {
        return bcode;
    }

    public void setBcode(String bcode) {
        this.bcode = bcode;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getFdate() {
        return fdate;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public String getFatmin() {
        return fatmin;
    }

    public void setFatmin(String fatmin) {
        this.fatmin = fatmin;
    }

    public String getFatmax() {
        return fatmax;
    }

    public void setFatmax(String fatmax) {
        this.fatmax = fatmax;
    }

    public String getSnfmin() {
        return snfmin;
    }

    public void setSnfmin(String snfmin) {
        this.snfmin = snfmin;
    }

    public String getSnfmax() {
        return snfmax;
    }

    public void setSnfmax(String snfmax) {
        this.snfmax = snfmax;
    }

    public String getTsrate() {
        return tsrate;
    }

    public void setTsrate(String tsrate) {
        this.tsrate = tsrate;
    }

    private String slno;
    private String bcode;
    private String mtype;
    private String fdate;
    private String tdate;
    private String fatmin;
    private String fatmax;
    private String snfmin;
    private String snfmax;
    private String tsrate;
    public ratesdata(String slno,String bcode,String mtype,String fdate,String tdate,String fatmin,String fatmax,String snfmin,String snfmax,String tsrate){
        this.bcode=bcode;
        this.mtype=mtype;
        this.fdate=fdate;
        this.tdate=tdate;
        this.fatmin=fatmin;
        this.fatmax=fatmax;
        this.snfmin=snfmin;
        this.snfmax=snfmax;
        this.tsrate=tsrate;
        this.slno=slno;
    }
}
