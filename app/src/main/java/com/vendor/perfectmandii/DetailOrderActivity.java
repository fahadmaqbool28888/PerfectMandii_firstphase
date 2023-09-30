package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.vendor.perfectmandii.Activity.DailyOrder.DailyOrderActivity;
import com.vendor.perfectmandii.Adapter.DailyOrderAdapter;
import com.vendor.perfectmandii.Adapter.DetailOrderAdapter;
import com.vendor.perfectmandii.Model.DailyOrder.orderDaily;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailOrderActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView detail_order_item;
    //DetailOrderAdapter mAdapter;
    OrderDetailAdapter orderDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        progressDialog=new ProgressDialog(DetailOrderActivity.this);
        Intent intent=getIntent();
        String data=intent.getStringExtra("data");
        String data1=intent.getStringExtra("data1");


        interpret_Json(data);
        interpret_Item(data1);

       // System.out.println(data2);


    }

    void init()
    {
        detail_order_item=findViewById(R.id.detail_order_item);
    }
    void interpret_Json(String jsonData)
    {
        progressDialog.show();
        List<DetailOrderModel> data=new ArrayList<>();

      /*  pdLoading.dismiss();*/
        try
        {
            JSONArray jArray = new JSONArray(jsonData);

            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);


                DetailOrderModel daily = new DetailOrderModel();
                daily.Product_Name=json_data.getString("Product_Name");
                daily.order_quantity=json_data.getString("order_quantity");
                daily.selling_price=json_data.getString("selling_price");
                daily.total_price=json_data.getString("total_price");

                System.out.println(daily.Product_Name+"\n");
                System.out.println(daily.order_quantity+"\n");
                System.out.println(daily.selling_price+"\n");
                System.out.println(daily.total_price+"\n");
                //daily.orderdetail=json_data.getJSONObject("orderdetail");



                data.add(daily);
            }

            // Setup and Handover data to recyclerview

            System.out.println(data.size());
            orderDetailAdapter = new OrderDetailAdapter(DetailOrderActivity.this, data);
            detail_order_item.setAdapter(orderDetailAdapter);
            detail_order_item.setLayoutManager( new LinearLayoutManager(DetailOrderActivity.this));
            progressDialog.dismiss();

        } catch (Exception e) {
        }
    }
    void interpret_Item(String jsonData)
    {



        /*  pdLoading.dismiss();*/
        try
        {
            JSONArray jArray = new JSONArray(jsonData);

            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);


                DetailOrderModel daily = new DetailOrderModel();
                daily.ItemCount=json_data.getString("ItemCount");
                daily.totalvalue=json_data.getString("totalvalue");



                //daily.orderdetail=json_data.getJSONObject("orderdetail");




            }



        } catch (Exception e) {
        }
    }
}