package com.customer.perfectcustomer.Model;

import org.json.JSONArray;

public class OrderCartModel
{
    public static boolean ischecked;

    public Double  val;
    private boolean isSelected;
    public String id;
    public String name,oos,quantity;
    public JSONArray replaceData;
    public String Accountid;
    public String l2_product_name;
    public String image_urlname;
    public String category_provider;
    public String status;
    public String price;
    public String order_quantity;
    public String selling_price;
    public String st_price;
    public String session,stock,MOQ,product_measure_in,measure_category;

    public String productDescription;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String ocid;
    public String productName;
    public String refrence;

    public JSONArray jsonArray;
    public String rp;

    public static boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        OrderCartModel.ischecked = ischecked;
    }
    //
    /*  "id": "127",
        "order_id": "5",
        "ordersession": "SatFeb06182252GMT05002021",
        "userid": "03125117559",
        "customer_order": "32258079",
        "invoice_order": "#F6031c76849b45",
        "subtotal": "800",
        "grandtotal": "2000",
        "discount": "0",
        "packaging": "1200"*/

    public String order_id;
    public String ordersession;
    public String userid;
    public String customer_order;
    public String invoice_order;
    public String subtotal;
    public String grandtotal;
    public String discount;
    public String packaging;
    public String order_place;
    public String Sub_Category;
    public String paid_status,Principle_Category;

}
