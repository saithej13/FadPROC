package com.example.fad;

public class settingsdata {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    private String slno;
    private String bcode;
    private String name;
    private String value;
    private String active;
    public settingsdata(String slno,String bcode,String name,String value,String active){
        this.slno=slno;
        this.bcode=bcode;
        this.name=name;
        this.value=value;
        this.active=active;
    }
}
