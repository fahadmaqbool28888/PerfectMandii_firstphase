package com.vendor.perfectmandii.Model;

public class StockProductModel
{
    /*   "id": "1",
        "name": "Bath Mug",
        "MOQ": "5",
        "stock": "0",
        "path": "https://sellerportal.perfectmandi.com/bulkfolder/img/BathMug.jpg"*/

    public String id,name,MOQ,stock,path,price;

    public StockProductModel() {
    }

    public StockProductModel(String id, String name, String path, String price) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.price = price;
    }



    private boolean checked;

    public boolean isChecked(){
        return checked;
    }
    public void setChecked(boolean checked){
        this.checked = checked;

    }
}
