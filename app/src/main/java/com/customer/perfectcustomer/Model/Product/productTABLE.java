package com.customer.perfectcustomer.Model.Product;

public class productTABLE
{
    public int id;
    public String name;

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

    public productTABLE() {
    }

    public productTABLE(String name) {
        this.name = name;
    }

    public productTABLE(int id, String name) {
        this.id = id;
        this.name = name;
    }


}
