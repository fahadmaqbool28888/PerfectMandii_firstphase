package com.vendor.perfectmandii.ExternalRequest;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.vendor.perfectmandii.Activity.EditProductActivity;
import com.vendor.perfectmandii.ImageProcessClass;
import com.vendor.perfectmandii.Model.ProductEdit.editProduct;

import java.util.HashMap;

public class ImageHttp_PRODUCTEDIT extends AsyncTask<Void,Void,String>
{
    Thread t;
    Handler handler;
    String ServerUploadPath = "https://sellerportal.perfectmandi.com/update_product_mb.php";
    editProduct EditProduct;
    String _product_name = "product_name";//9
    String product_Desc = "product_Desc";//10
    String product_unit = "product_unit";//11
    String product_price = "product_price";//12
    String product_MOQ = "product_moq";//12
    String image_path = "image_path";//13
    String accountid = "account_id";
    Context context;
    public ImageHttp_PRODUCTEDIT(ProgressDialog progressDialog,Context context,editProduct EditProduct,Handler handler)
    {
        this.context=context;
        this.EditProduct=EditProduct;
        this.progressDialog = progressDialog;
        this.handler=handler;
    }

    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        progressDialog.show();
      //  progressDialog = ProgressDialog.show(context,"Product Information is Uploading","Please Wait",false,false);
    }

    @Override
    protected void onPostExecute(String string1) {

        super.onPostExecute(string1);
        // Toast.makeText(AddStoreActivity.this,string1,Toast.LENGTH_SHORT).show();

        System.out.println("Response Get is "+string1);

        progressDialog.dismiss();
       // Toast.makeText(context, string1, Toast.LENGTH_SHORT).show();
       // progressDialog.dismiss();


       // t.suspend();



    }

    @Override
    protected String doInBackground(Void... params) {


        ImageProcessClass imageProcessClass = new ImageProcessClass();

        HashMap<String,String> HashMapParams = new HashMap<String,String>();


        HashMapParams.put("id", EditProduct.Id);
        HashMapParams.put(_product_name,EditProduct.productname); //mobile Number
        HashMapParams.put(product_Desc, EditProduct.productdescription);
        HashMapParams.put(product_unit,EditProduct.unitquantity);
        HashMapParams.put(product_price,EditProduct.unitprice); //BA
        HashMapParams.put(image_path, EditProduct.image_paths ); //SA
        HashMapParams.put(product_MOQ, EditProduct.unitmoq);//SA
        HashMapParams.put(accountid,EditProduct.providerId);
      String  FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

        return FinalData;
    }
}
