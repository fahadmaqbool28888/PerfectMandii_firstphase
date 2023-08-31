package com.customer.perfectcustomer.Model.RFQMODEL;

public class rfq_model
{

    /* private static final String RFQ_ID="rfq_id";
    private static final String RFQ_OCID="rfq_ocid";
    private static final String RFQ_I_D="rfq_i_d";
    private static final String RFQ_r_f_q="rfq_r_f_q";*/

    public int id;
    public String ocid,i_d,r_f_q;

    public rfq_model() {
    }

    public rfq_model(int id, String ocid, String i_d, String r_f_q) {
        this.id = id;
        this.ocid = ocid;
        this.i_d = i_d;
        this.r_f_q = r_f_q;
    }
    public rfq_model(String ocid, String i_d, String r_f_q) {

        this.ocid = ocid;
        this.i_d = i_d;
        this.r_f_q = r_f_q;
    }
}
