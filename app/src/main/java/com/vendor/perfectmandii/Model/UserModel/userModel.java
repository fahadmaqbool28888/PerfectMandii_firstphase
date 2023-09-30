package com.vendor.perfectmandii.Model.UserModel;

public class userModel
{
    public int id;
    public  String name;
    public String password;

    public userModel(int id, String name, String password, String pin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.pin = pin;
    }


    @Override
    public String toString() {
        return "userModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String pin;
}
