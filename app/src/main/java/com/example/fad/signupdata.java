package com.example.fad;

public class signupdata {


    public signupdata(String centername, String agentname, String mobileno, String houseno, String village, String mandal, String dist, String pincode, String username, String roleid, String password) {
        this.centername = centername;
        this.agentname = agentname;
        this.mobileno = mobileno;
        this.houseno = houseno;
        this.village = village;
        this.mandal = mandal;
        this.dist = dist;
        this.pincode = pincode;
        this.username = username;
        this.roleid = roleid;
        this.password = password;
    }


    public String getCentername() {
        return centername;
    }

    public void setCentername(String centername) {
        this.centername = centername;
    }

    public String getAgentname() {
        return agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getHouseno() {
        return houseno;
    }

    public void setHouseno(String houseno) {
        this.houseno = houseno;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getMandal() {
        return mandal;
    }

    public void setMandal(String mandal) {
        this.mandal = mandal;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    String centername;
    String agentname;
    String mobileno;
    String houseno;
    String village;
    String mandal;
    String dist;
    String pincode;
    String username;
    String roleid;
    String password;
}
