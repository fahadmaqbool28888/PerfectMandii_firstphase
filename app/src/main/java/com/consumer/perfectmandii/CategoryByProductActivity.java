package com.consumer.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.consumer.perfectmandii.Adapter.AdapterCategoryByProduct;
import com.consumer.perfectmandii.Model.CategoyByProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryByProductActivity extends AppCompatActivity
{

    AdapterCategoryByProduct mAdapter;
    String result,id,name;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_by_product);
        recyclerView=findViewById(R.id.ctpro);
        Intent intent = getIntent();
        result = intent.getStringExtra("value");
        id=intent.getStringExtra("va");
        name=intent.getStringExtra("name");
        popdata(result);


        Toast.makeText(CategoryByProductActivity.this,result,Toast.LENGTH_LONG).show();
    }
    void popdata(String result) {
        //pdLoading.dismiss();
        List<CategoyByProductModel> data = new ArrayList<>();

        // pdLoading.dismiss();
        try {
            //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();


            JSONArray jArray = new JSONArray(result);

            // Extract data from json and store into ArrayList as class objects
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                CategoyByProductModel fishData = new CategoyByProductModel();
                fishData.image_url = json_data.getString("image_url");
                fishData.name = json_data.getString("name");
                fishData.parent_Category = json_data.getString("l2_product_name");


                // fishData.usid=username;

                data.add(fishData);
            }

            // Setup and Handover data to recyclerview

            mAdapter = new AdapterCategoryByProduct(CategoryByProductActivity.this, data);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}