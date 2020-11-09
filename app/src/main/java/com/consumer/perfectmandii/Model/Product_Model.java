package com.consumer.perfectmandii.Model;

public class Product_Model
{

    public String image_path,ProductPrice,moq,quan,productname,productDescription,product_id,provider;
    public Product_Model(String  productname,String productDescription, String image_path, String productPrice, String moq, String quan,String product_id, String provider) {
        this.productDescription = productDescription;
        this.image_path = image_path;
        ProductPrice = productPrice;
        this.moq = moq;
        this.quan = quan;
        this.productname = productname;
        this.productDescription = productDescription;
        this.product_id = product_id;
        this.provider = provider;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getMoq() {
        return moq;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }


}
