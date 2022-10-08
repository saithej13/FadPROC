package com.example.fad.branch;

import java.sql.Date;

public class branch {
    int bcode;
    String bname;
    Date fdate;
    Date tdate;
    String productkey;
    String deviceid;
    int active;

    public branch(int bcode, String bname, Date fdate, Date tdate, String productkey, String deviceid, int active) {
        this.bcode = bcode;
        this.bname = bname;
        this.fdate = fdate;
        this.tdate = tdate;
        this.productkey = productkey;
        this.deviceid = deviceid;
        this.active = active;
    }

    public int getBcode() {
        return bcode;
    }

    public void setBcode(int bcode) {
        this.bcode = bcode;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public Date getFdate() {
        return fdate;
    }

    public void setFdate(Date fdate) {
        this.fdate = fdate;
    }

    public Date getTdate() {
        return tdate;
    }

    public void setTdate(Date tdate) {
        this.tdate = tdate;
    }

    public String getProductkey() {
        return productkey;
    }

    public void setProductkey(String productkey) {
        this.productkey = productkey;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public int isActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
