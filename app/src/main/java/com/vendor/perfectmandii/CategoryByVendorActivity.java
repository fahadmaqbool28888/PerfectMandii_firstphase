package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vendor.perfectmandii.Adapter.AdapterCategoryByVendor;

import com.vendor.perfectmandii.Model.CategoyByvendorModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CategoryByVendorActivity extends AppCompatActivity
{

    RecyclerView recyclerView;
    String result;
    ProgressDialog progressDialog;
    AdapterCategoryByVendor mAdapter;
    String id,name;

    TextView textView;
    ImageView backbutton;
    String usersessionid,session,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_by_vendor);



        backbutton=findViewById(R.id.backbutton1);
        recyclerView = findViewById(R.id.rec_cat_ven);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                finish();
            }
        });


        textView=findViewById(R.id.vendor_prov);
        Intent intent = getIntent();
        result = intent.getStringExtra("value");

        id=intent.getStringExtra("va");
        name=intent.getStringExtra("name");
        usersessionid=intent.getStringExtra("userid");
        session=intent.getStringExtra("session");
        category=intent.getStringExtra("categoy");

        textView.setText(name);
        System.out.println(result);

//        progressDialog.show();

        popdata(result);


    }


    void popdata(String result) {
        //pdLoading.dismiss();
        List<CategoyByvendorModel> data = new ArrayList<>();

        // pdLoading.dismiss();
        try {
            //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();


            JSONArray jArray = new JSONArray(result);

            // Extract data from json and store into ArrayList as class objects
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                CategoyByvendorModel fishData = new CategoyByvendorModel();
                fishData.id=json_data.getString("id");
                fishData.image_url = json_data.getString("image_path");
                fishData.name = json_data.getString("image_name");
                fishData.parent_Category = json_data.getString("image_scategory");
                fishData.price=json_data.getString("image_price");
                fishData.productDescription=json_data.getString("Product_Description");
                fishData.vendorid=id;
                fishData.purchasedid=usersessionid;
                fishData.usersessionid=usersessionid;
                fishData.usersession=session;
                fishData.parent_Category=category;
                // fishData.usid=username;

                data.add(fishData);
            }

            // Setup and Handover data to recyclerview

            mAdapter = new AdapterCategoryByVendor(CategoryByVendorActivity.this, data);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}