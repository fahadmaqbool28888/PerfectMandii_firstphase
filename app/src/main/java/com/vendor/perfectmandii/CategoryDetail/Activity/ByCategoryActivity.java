package com.vendor.perfectmandii.CategoryDetail.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.DashboardAdapter.ProductShelfAdapter;
import com.vendor.perfectmandii.CategoryDetail.Adapter.binstate;
import com.vendor.perfectmandii.CategoryDetail.Controller.bulkupdate;
import com.vendor.perfectmandii.CategoryDetail.Controller.bundleUpload;
import com.vendor.perfectmandii.CategoryDetail.Controller.demandData;
import com.vendor.perfectmandii.CategoryDetail.Controller.demand_categorizeP;
import com.vendor.perfectmandii.CategoryDetail.Model.ProductModel;
import com.vendor.perfectmandii.DBHelper;
import com.vendor.perfectmandii.Dashboard.OPActivity;
import com.vendor.perfectmandii.ManageShelf_Activity;
import com.vendor.perfectmandii.Model.ProductShelf;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.datapass;
import com.vendor.perfectmandii.shoppingBasket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ByCategoryActivity extends AppCompatActivity
{ int i,i1,i2;

    String flag="specific";
    ArrayList<ProductModel> bulkque;
    binstate bin;
    ArrayList<ProductModel> productModels;

    String[] numarray = {"Search","Apply"};
    Spinner level_1,level_2,level_3;
    ArrayList<String> level0_list,level1_list,level2_list;

    HashMap<String,JSONArray> level0,level1,level2;
    HashMap<String, JSONArray> hashMap;


    ProgressBar progressBar;
    Context context;
    DBHelper dbHelper;
    ArrayList<userVendor> pointModelsArrayList;

    String store_id,s_1,s_2,s_3=null;

    String json_string;
    ImageButton unstable_state;

    EditText enter_price;
    Button submit_price;
    RecyclerView productContainer;
    CheckBox select_All;

    boolean flagSelectAll;
    void connectLocalDb(Context context)
    {
        dbHelper=new DBHelper(context);
        pointModelsArrayList=dbHelper.readVendor();
        userVendor vendor1=pointModelsArrayList.get(0);
        store_id=vendor1.storeid;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_category);
        context=ByCategoryActivity.this;
        connectLocalDb(context);




//

        //

        init_widget();


        select_All.setVisibility(View.GONE);

        progressBar.setVisibility(View.GONE);
        if (level0_list.size()>0)
        {

            level0_list.clear();
            level0_list.add("Select Category");

        }
        else {
            level0_list.add("Select Category");


            new demandData(ByCategoryActivity.this, level_1, level_2, level_3) {
                @Override
                protected void passmethod(String key, JSONArray jsonArray)
                {

                    level0_list.add(key);
                    level0.put(key,jsonArray);
                }
            }.execute();
        }


        level_2.setVisibility(View.INVISIBLE);
        level_3.setVisibility(View.INVISIBLE);


       populate_level0(ByCategoryActivity.this);



        level_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i>0)
                {
                    s_1 = level_1.getSelectedItem().toString();


                    try {
                        get_level_2(s_1);
                        level_2.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {


            }
        });

        level_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i>0)
                {
                   s_2 = level_2.getSelectedItem().toString();


                    try {
                        get_level_3(s_2);
                        level_3.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        level_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i>0)
                {
                    s_3 = level_3.getSelectedItem().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submit_price.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {

        i=level_1.getSelectedItemPosition();
        i1=level_2.getSelectedItemPosition();
        i2=level_3.getSelectedItemPosition();


        String val=enter_price.getText().toString();
        if (i>0&i1>0&i2>-1&!val.equalsIgnoreCase("")) {
            s_1 = level_1.getSelectedItem().toString();
            s_2 = level_2.getSelectedItem().toString();
            s_3 = level_3.getSelectedItem().toString();


            purchase(context,s_1,s_2,s_3,val);





        }
        else {
            Toast.makeText(context,"Please enter price and submit categories",Toast.LENGTH_LONG).show();
        }

        }
});





        unstable_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                productModels.clear();

              i=level_1.getSelectedItemPosition();
              i1=level_2.getSelectedItemPosition();
                i2=level_3.getSelectedItemPosition();


                if (i>0&i1>0&i2>-1)
                {
                    s_1=level_1.getSelectedItem().toString();
                    s_2=level_2.getSelectedItem().toString();
                    s_3=level_3.getSelectedItem().toString();



                    new demand_categorizeP(context, progressBar,store_id,s_1,s_2,s_3) {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected void passdata(String str)
                        {



                            try
                            {
                                JSONArray jArray = new JSONArray(str);

                                for(int i=0;i<jArray.length();i++){
                                    JSONObject json_data = jArray.getJSONObject(i);


                                    ProductModel fishData = new ProductModel();
                                    fishData.id=json_data.getString("id");
                                    fishData.path= json_data.getString("path");
                                    fishData.name= json_data.getString("name");
                                    fishData.price=json_data.getString("price");
                                    fishData.product_measure_in=json_data.getString("measure_in");
                                    fishData.measure_category=json_data.getString("measure_category");


                                    fishData.setSelected(false);


                                    productModels.add(fishData);
                                  //  bulkque.add(fishData);
                                }



                                bin=new binstate(context,productModels,flagSelectAll) {
                                    @Override
                                    public void add(String action,ProductModel productModel)
                                    {
                                            bulkque.add(productModel);
                                    }

                                    @Override
                                    public void minus(ProductModel productModel)
                                    {
                                        bulkque.remove(productModel);
                                    }

                                    @Override
                                    public void unselectAll(String action, ProductModel productModel)
                                    {
                                        flag="specific";
                                        select_All.setChecked(false);
                                        flagSelectAll=false;
                                        bulkque.remove(productModel);
                                    }
                                };

                                select_All.setVisibility(View.VISIBLE);
                                productContainer.setAdapter(bin);
                                progressBar.setVisibility(View.GONE);
                                productContainer.setLayoutManager( new LinearLayoutManager(context));


                            } catch (Exception e) {
                            }
                        }
                    }.execute();

                }
                else {
                    Toast.makeText(context,"Please Select categories properly", Toast.LENGTH_LONG).show();

                }





            }
        });



        select_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_All.isChecked())
                {


                    flagSelectAll=true;

                    if (bulkque.size()>0)
                    {
                        bulkque.clear();
                    }
                    bin.selectAllItems();
                    flag="bulk";


                    int length=productModels.size();
                    for (int i=0;i<length;i++)
                    {
                        ProductModel productModel=productModels.get(i);
                        bulkque.add(productModel);
                    }

         /*   for (int i=0;i<data.size();i++)
            {
                ProductModel order=data.get(i);

                orderque("add",order);
            }*/




                    //  calculateorder("add","yes",null);


                }
                else
                {
                    bulkque.clear();
                    flagSelectAll=false;
                    flag="specific";
                    bin.unselectAllItems();

                    //calculateorder("minus","yes",null);


                }
            }
        });



    }


    void purchase(Context context,String s_1,String s_2,String s_3,String val)
    {
        if (flag.equalsIgnoreCase("bulk"))
        {

         //   Toast.makeText(context,flag,Toast.LENGTH_LONG).show();

            json_string=JsonString(context);
            if ("{\"upload_fishes\":]}".equalsIgnoreCase(json_string))
            {

            }
            else
            {
                String times=String.valueOf(bulkque.size());
                progressBar.setVisibility(View.VISIBLE);
                new bundleUpload(context, progressBar, json_string, store_id, val, s_1, s_2, s_3, times) {
                    @Override
                    public void data_fetch(String str)
                    {
                        bulkque.clear();
                        productModels.clear();
                        flag="specific";
                        progressBar.setVisibility(View.INVISIBLE);
                        productModels.clear();
                        try
                        {
                            JSONArray jArray = new JSONArray(str);

                            for(int i=0;i<jArray.length();i++){
                                JSONObject json_data = jArray.getJSONObject(i);


                                ProductModel fishData = new ProductModel();
                                fishData.id=json_data.getString("id");
                                fishData.path= json_data.getString("path");
                                fishData.name= json_data.getString("name");
                                fishData.price=json_data.getString("price");
                                fishData.product_measure_in=json_data.getString("measure_in");
                                fishData.measure_category=json_data.getString("measure_category");


                                fishData.setSelected(false);


                                productModels.add(fishData);
                            }



                            bin=new binstate(context,productModels,flagSelectAll) {
                                @Override
                                public void add(String action,ProductModel productModel)
                                {
                                    bulkque.add(productModel);
                                }

                                @Override
                                public void minus(ProductModel productModel)
                                {
                                    bulkque.remove(productModel);
                                }

                                @Override
                                public void unselectAll(String action, ProductModel productModel)
                                {
                                    flag="specific";
                                    select_All.setChecked(false);
                                    flagSelectAll=false;
                                    bulkque.remove(productModel);
                                }


                            };

                            productContainer.setAdapter(bin);
                            progressBar.setVisibility(View.GONE);
                            productContainer.setLayoutManager( new LinearLayoutManager(context));


                        } catch (Exception e) {
                        }
                    }
                }.execute();

                select_All.setChecked(false);

            }
        }
        else
        {
            if (bulkque.size()>productModels.size())
            {

                bulkque.clear();
            }
            else if (bulkque.size()<1)
            {
                Toast.makeText(context,"Please Add Item to proceed",Toast.LENGTH_LONG).show();

            }
            else
            {
             json_string=JsonString(context);
                if ("{\"upload_fishes\":]}".equalsIgnoreCase(json_string))
                {

                }
                else
                {
                    String times=String.valueOf(bulkque.size());
                    progressBar.setVisibility(View.VISIBLE);
                    new bundleUpload(context, progressBar, json_string, store_id, val, s_1, s_2, s_3, times) {
                        @Override
                        public void data_fetch(String str)
                        {
                            bulkque.clear();
                            productModels.clear();
                            flag="specific";
                            progressBar.setVisibility(View.INVISIBLE);
                            productModels.clear();
                            try
                            {
                                JSONArray jArray = new JSONArray(str);

                                for(int i=0;i<jArray.length();i++){
                                    JSONObject json_data = jArray.getJSONObject(i);


                                    ProductModel fishData = new ProductModel();
                                    fishData.id=json_data.getString("id");
                                    fishData.path= json_data.getString("path");
                                    fishData.name= json_data.getString("name");
                                    fishData.price=json_data.getString("price");
                                    fishData.product_measure_in=json_data.getString("measure_in");
                                    fishData.measure_category=json_data.getString("measure_category");


                                    fishData.setSelected(false);


                                    productModels.add(fishData);
                                }



                                bin=new binstate(context,productModels,flagSelectAll) {
                                    @Override
                                    public void add(String action,ProductModel productModel)
                                    {
                                        bulkque.add(productModel);
                                    }

                                    @Override
                                    public void minus(ProductModel productModel)
                                    {
                                        bulkque.remove(productModel);
                                    }

                                    @Override
                                    public void unselectAll(String action, ProductModel productModel)
                                    {
                                        flag="specific";
                                        select_All.setChecked(false);
                                        flagSelectAll=false;
                                        bulkque.remove(productModel);
                                    }


                                };

                                productContainer.setAdapter(bin);
                                progressBar.setVisibility(View.GONE);
                                productContainer.setLayoutManager( new LinearLayoutManager(context));


                            } catch (Exception e) {
                            }
                        }
                    }.execute();


                }
            }

        }
    }





    String JsonString(Context context) {

        int i;


        json_string=null;




        try {
            //Repeat and loop this until all objects are added (and add try+catch)
            json_string ="{\"upload_fishes\":[";
            for (i=0;i<bulkque.size();i++)
            {

                JSONObject obj_new = new JSONObject();

                ProductModel productModel=bulkque.get(i);
                obj_new.put("id",productModel.id);

                json_string = json_string + obj_new.toString() + ",";

//Close JSON string

            }
            json_string = json_string.substring(0, json_string.length()-1);
            json_string += "]}";



        }
        catch (JSONException jsox)
        {
            Toast.makeText(context,jsox.toString(),Toast.LENGTH_LONG).show();
        }

        return  json_string;
    }


    void init_widget()
    {
        productModels=new ArrayList<>();
        submit_price=findViewById(R.id.submit_price);
        enter_price=findViewById(R.id.enter_price);
        productContainer=findViewById(R.id.productCon);
        select_All=findViewById(R.id.editva);
        productModels=new ArrayList<>();
        bulkque=new ArrayList<>();
        progressBar=findViewById(R.id.progressbar);
        level0=new HashMap<>();
        level2=new HashMap<>();
        level0_list=new ArrayList<>();
        level1_list=new ArrayList<>();
        level2_list=new ArrayList<>();
        level_1=findViewById(R.id.level1);
        level_2=findViewById(R.id.level2);
        level_3=findViewById(R.id.level3);

        unstable_state=findViewById(R.id.action_unstable);

    }

    // Spinner Management
    private void populate_level0(Context cc)
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, level0_list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        level_1.setAdapter(dataAdapter);
    }
    private void populate_level1(Context cc)
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, level1_list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        level_2.setAdapter(dataAdapter);
    }

    private void populate_level3(Context cc)
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, level2_list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        level_3.setAdapter(dataAdapter);
    }

    void get_level_2(String array) throws JSONException
    {
        level1=new HashMap<String, JSONArray>();
        JSONArray jArray=level0.get(array);

        if (level1_list.size()>0)
        {
            level1_list.clear();
            level1_list.add("Select Sub categoy");
        }
        else {
            level1_list.add("Select Sub categoy");

        }

        // System.out.println(array+" "+jArray.toString());
        //JSONArray jArray = new JSONArray(data.get(array));
        for (int i=0;i<jArray.length();i++)
        {

            JSONObject json_data = jArray.getJSONObject(i);

            String d=json_data.getString("Category");
            JSONArray jsonArray=json_data.getJSONArray("arr");

            level1_list.add(d);
            level1.put(d,jsonArray);
            //   System.out.println(d +" "+jsonArray);







        }
        populate_level1(ByCategoryActivity.this);


    }

    void get_level_3(String array) throws JSONException
    {
        JSONArray jArray=level1.get(array);


        if (level2_list.size()>1)
        {
            level2_list.clear();
            level2_list.add("Select category");
        }
        // System.out.println(array+" "+jArray.toString());
        //JSONArray jArray = new JSONArray(data.get(array));
        for (int i=0;i<jArray.length();i++)
        {

            JSONObject json_data = jArray.getJSONObject(i);

            String d=json_data.getString("Category");
           // JSONArray jsonArray=json_data.getJSONArray("arr");

            level2_list.add(d);

            //   System.out.println(d +" "+jsonArray);







        }

        populate_level3(ByCategoryActivity.this);

    }
}