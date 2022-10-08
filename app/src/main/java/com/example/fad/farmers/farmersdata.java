package com.example.fad.farmers;

public class farmersdata {
    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }
    private String slno;
    private String code;
    private String fname;
    private String mtype;
    private String mobileno;
    private String active;
    public farmersdata(String slno,String code,String fname,String mobileno,String mtype,String active){
        this.code=code;
        this.fname=fname;
        this.mobileno=mobileno;
        this.mtype=mtype;
        this.active=active;
        this.slno=slno;
    }
}
