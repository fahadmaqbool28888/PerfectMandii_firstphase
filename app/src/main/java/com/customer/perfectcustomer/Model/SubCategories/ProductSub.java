package com.customer.perfectcustomer.Model.SubCategories;

public class ProductSub
{


    public static boolean ischecked;

    public String product_measure_in,measure_category;
    public String product_id;
    public String product_name;
    public String product_description;
    public String product_price;
    public String product_image_path;
    public String product_provider;
    public  String product_moq,product_quan,provider,headingname,refrence;

    public static boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        ProductSub.ischecked = ischecked;
    }
    public String product_promotionPrice,pstatus,Prodcut_Sub_Category,Principle_Category;

    public String getProduct_measure_in() {
        return product_measure_in;
    }

    public void setProduct_measure_in(String product_measure_in) {
        this.product_measure_in = product_measure_in;
    }

    public String getMeasure_category() {
        return measure_category;
    }

    public void setMeasure_category(String measure_category) {
        this.measure_category = measure_category;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }


    public String getProduct_provider() {
        return product_provider;
    }

    public void setProduct_provider(String product_provider) {
        this.product_provider = product_provider;
    }



}
