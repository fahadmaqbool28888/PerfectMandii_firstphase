package com.vendor.perfectmandii.Model;

public class userVendor
{

    public String userid;
    public String userpass;
    public String usersess;
    public String username;
    public String storename;
    public String storeid;
    public String storepath;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String status;

    public userVendor() {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public String getUsersess() {
        return usersess;
    }

    public void setUsersess(String usersess) {
        this.usersess = usersess;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getStorepath() {
        return storepath;
    }

    public void setStorepath(String storepath) {
        this.storepath = storepath;
    }

    public userVendor(String userid, String userpass, String usersess, String username, String storename, String storeid, String storepath,String status) {
        this.userid = userid;
        this.userpass = userpass;
        this.usersess = usersess;
        this.username = username;
        this.storename = storename;
        this.storeid = storeid;
        this.storepath = storepath;
        this.status=status;
    }




}

