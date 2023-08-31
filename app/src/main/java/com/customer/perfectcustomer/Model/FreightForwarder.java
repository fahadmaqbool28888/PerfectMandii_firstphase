package com.customer.perfectcustomer.Model;

public class FreightForwarder
{
    private static final String FREIGHT_ID="fr_id";
    private static final String FREIGHT_NAME="fr_name";
    private static final String FREIGHT_ADDRESS="fr_address";
    private static final String FREIGHT_CONTACT="fr_contact";

    private static final String FREIGHT_BY="fr_by";

    public int id;
    public String name,address,contact,fby;

    public FreightForwarder() {
    }

    public FreightForwarder(int id, String name, String address, String contact, String fby) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.fby = fby;
    }
    public FreightForwarder(String name, String address, String contact, String fby) {
     //   this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.fby = fby;
    }
}
