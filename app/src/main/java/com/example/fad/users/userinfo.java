package com.example.fad.users;

public class userinfo {
    public static int getUserid() {
        return userid;
    }

    public static void setUserid(int userid) {
        userinfo.userid = userid;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        userinfo.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        userinfo.password = password;
    }

    public static int getRoleid() {
        return roleid;
    }

    public static void setRoleid(int roleid) {
        userinfo.roleid = roleid;
    }

    public static int getIsactive() {
        return isactive;
    }

    public static void setIsactive(int isactive) {
        userinfo.isactive = isactive;
    }

    public static int getBcode() {
        return bcode;
    }

    public static void setBcode(int bcode) {
        userinfo.bcode = bcode;
    }

    public static int getOperation() {
        return operation;
    }

    public static void setOperation(int operation) {
        userinfo.operation = operation;
    }

    public static int getGraceperiod() {
        return graceperiod;
    }

    public static void setGraceperiod(int graceperiod) {
        userinfo.graceperiod = graceperiod;
    }

    public static String getDeviceid() {
        return deviceid;
    }

    public static void setDeviceid(String deviceid) {
        userinfo.deviceid = deviceid;
    }

    public static String getCentername() { return centername; }

    public static void setCentername(String centername) {
        userinfo.centername = centername;
    }

    public static String getAgentname() {
        return agentname;
    }

    public static void setAgentname(String agentname) {
        userinfo.agentname = agentname;
    }

    public static String getMobileno() {
        return mobileno;
    }

    public static void setMobileno(String mobileno) {
        userinfo.mobileno = mobileno;
    }

    public static String getHouseno() {
        return houseno;
    }

    public static void setHouseno(String houseno) {
        userinfo.houseno = houseno;
    }

    public static String getVillage() {
        return village;
    }

    public static void setVillage(String village) {
        userinfo.village = village;
    }

    public static String getMandal() {
        return mandal;
    }

    public static void setMandal(String mandal) {
        userinfo.mandal = mandal;
    }

    public static String getDist() {
        return dist;
    }

    public static void setDist(String dist) {
        userinfo.dist = dist;
    }

    public static String getPincode() {
        return pincode;
    }

    public static void setPincode(String pincode) {
        userinfo.pincode = pincode;
    }

    public userinfo(String username,String password,int roleid,int isactive,int bcode,int operation,int graceperiod,String deviceid,String centername,String mobileno){
        userinfo.username =username;
        userinfo.password=password;
        userinfo.roleid=roleid;
        userinfo.isactive=isactive;
        userinfo.bcode=bcode;
        userinfo.operation=operation;
        userinfo.graceperiod=graceperiod;
        userinfo.deviceid=deviceid;
        userinfo.centername=centername;
        userinfo.mobileno=mobileno;
    }

    public static int userid;
    public static String username;
    public static String password;
    public static int roleid;
    public static int isactive;
    public static  int bcode;
    public static int operation;
    public static int graceperiod;
    public static String deviceid;
    public static String centername;
    public static String agentname;
    public static String mobileno;
    public static String houseno;
    public static String village;
    public static String mandal;
    public static String dist;
    public static String pincode;
}
