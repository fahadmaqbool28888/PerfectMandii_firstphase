package com.customer.perfectcustomer.Model.Customer;

public class UserModel
{

public UserModel()
{

}

    public String accountid;
    public String Name;
    public String contact;
    public String profilepic;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String city;



    public String shop;
    public String session;


    int id;
    //String accountid,String name,String contact,String session,String shop,String profilepic,String city
    public UserModel(int id,String accountid, String name, String contact, String session,String shop, String profilepic,String city) {
        this.id=id;
        this.accountid = accountid;
        Name = name;
        this.contact = contact;
        this.profilepic = profilepic;
        this.session = session;
        this.shop=shop;
        this.city=city;
    }
    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }
    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
