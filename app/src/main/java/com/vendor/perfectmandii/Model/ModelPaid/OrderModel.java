package com.vendor.perfectmandii.Model.ModelPaid;

public class OrderModel
{
    /*  "orderby": "fahad",
        "orderto": "Perfect Mandi",
        "Destination": "perfect Barton store near shalimar hotel Rawalpindi ",
        "Date": "2021-06-06",
        "customer_order": "58581346"*/

    public String orderby;
    public String orderto;
    public String Destination;
    public String date;
    public String customer_order;

    /* "orderid": "43",
        "userid": "03245994806",
        "image_path": "https://staginigserver.perfectmandi.com/images/bankslips/53.png",
        "deposit_amount": "10100",
        "depositdate": "2021-06-09"*/


    public  String orderid;
    public String userid;
    public String image_path;
    public String deposit_amount;
    public String depositdate;
    /*
    *    "grandtotal": "600",
        "dicount": "0",
        "packaging": "100",
        "subtotal": "500"*/
    public String grandtotal;
    public String discount;
    public String packaging;
    public String subtotal;
}
