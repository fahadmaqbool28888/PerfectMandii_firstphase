package com.vendor.perfectmandii.CategoryDetail.Model;

public class ProductModel
{
   public String id,price,path,name,product_measure_in,measure_category;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public ProductModel() {
    }

    public ProductModel(String id, String price, String path, String name, String product_measure_in, String measure_category, boolean isSelected) {
        this.id = id;
        this.price = price;
        this.path = path;
        this.name = name;
        this.product_measure_in = product_measure_in;
        this.measure_category = measure_category;
        this.isSelected = isSelected;
    }
}
