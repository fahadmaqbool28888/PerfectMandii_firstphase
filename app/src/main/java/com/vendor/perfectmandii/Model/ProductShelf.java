package com.vendor.perfectmandii.Model;

public class ProductShelf {
    public String id;
    public String Product_Name;
    public String Product_Description;
    public String Product_Unit_Quan;
    public String Product_Unit_Price;
    public String store_listing;
    public String lisiting_status;
    public String image_path;
    public String userid;
    public boolean state;
    public String MOQ,provider;

    private boolean checked;

    public boolean isChecked(){
        return checked;
    }
    public void setChecked(boolean checked){
        this.checked = checked;

    }
}
