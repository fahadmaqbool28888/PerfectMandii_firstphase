package com.vendor.perfectmandii.Dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Activity.DailyOrder.DailyOrderActivity;
import com.vendor.perfectmandii.Activity.Dispatch.vendorDispatchActivity;
import com.vendor.perfectmandii.Activity.ManageFlashSale.FlashSaLEActivity;
import com.vendor.perfectmandii.Activity.PriceUpdateActivity;
import com.vendor.perfectmandii.Activity.PromotionProductActivity;
import com.vendor.perfectmandii.Activity.PromotionWork.ManagePromotionActivity;
import com.vendor.perfectmandii.Activity.RFQ.ViewRFQActivity;
import com.vendor.perfectmandii.Activity.StockUpdateActivity;
import com.vendor.perfectmandii.Activity.vendor.AddProductActivity;
import com.vendor.perfectmandii.Activity.vendor.VendorProfileUpdateActivity;
import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.Adapter.DashboardAdapter.ProductShelfAdapter;
import com.vendor.perfectmandii.Adapter.DashboardAdapter.dashboardAdapter;
import com.vendor.perfectmandii.CustomeGlideModule;
import com.vendor.perfectmandii.DBHelper;
import com.vendor.perfectmandii.GlideApp;
import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.MainActivity;
import com.vendor.perfectmandii.ManageShelf_Activity;
import com.vendor.perfectmandii.Model.dashboardModel.modelDashboard;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.OrderDashboardActivity;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.profile_Updates.ProductInformationActivity;
import com.vendor.perfectmandii.shoppingBasket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OPActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{BottomNavigationView navigation;
    DBHelper dbHelper;
    String userid,name,session, path,store,storeid;
    TextView textname,textstore;
    NavigationView navigationView;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    dashboardAdapter adapterdashboard;
    BarChart barChart;

    TextView footer_item_2;
    RecyclerView vendor_BI_data;
    CircleImageView profile_image;


    String vendor;
    ArrayList<userVendor> pointModelsArrayList;

    ImageView bax;
    void init()
    {
        pointModelsArrayList = dbHelper.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);
            vendor=pointModel.userid;




        }
        else
        {


        }




    }
    String status;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opactivity);

        Intent intent=getIntent();
        dbHelper=new DBHelper(OPActivity.this);
        pointModelsArrayList=dbHelper.readVendor();
        userVendor vendor1=pointModelsArrayList.get(0);


        status=vendor1.status;
        userid=intent.getStringExtra("userid");
        //Toast.makeText(OPActivity.this,userid,Toast.LENGTH_LONG).show();
        name=intent.getStringExtra("username");
        session=intent.getStringExtra("session");
        path=intent.getStringExtra("path");
        store=intent.getStringExtra("store");
        storeid=intent.getStringExtra("storeid");

        intializeWidget();

        if (status.equalsIgnoreCase("active"))
        {
            storedown.setChecked(true);

        }
        else {
            storedown.setChecked(false);

        }
       // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        System.out.println(name);
        if (session==null)
        {
            Intent intent1=new Intent(OPActivity.this, LoginActivity.class);
            startActivity(intent1);
        }
        else
        {

            //intializeWidget();
        /*    Picasso.get().load(path).into(circleImageView);
            textname_store.setText(name);*/
         //   new MainActivity.AsyncFetch().execute();
            setNavigationViewListener();
            // drawer layout instance to toggle the menu icon to open
            // drawer and back button to close drawer


            loadWidget();
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

            // pass the Open and Close toggle for the drawer layout listener
            // to toggle the button
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            navigationView.getMenu().addSubMenu("");

            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            new AsyncFetch().execute();
            BarInit(barChart);


        }
        footer_item_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                dbHelper.deleteRow(userid);
                 Intent intent=new Intent(OPActivity.this,LoginActivity.class);


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

            }
        });

    }





    Switch storedown;
    void intializeWidget()
    {

        barChart=findViewById(R.id.chart_main);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView=findViewById(R.id.main_screen_menu);
        vendor_BI_data=findViewById(R.id.vendor_BI_data);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        profile_image = (CircleImageView) hView.findViewById(R.id.detail_iamge);
        textname=hView.findViewById(R.id.headname_storename);
        textstore=hView.findViewById(R.id.tilename_storename);
        footer_item_2=findViewById(R.id.footer_item_2);
        storedown=findViewById(R.id.storedown);

       /* navigation = findViewById(R.id.bottom_nav_view);*/





    }
    void loadWidget()
    {
        textname.setText(name);
        textstore.setText(store);
        if (path!=null)
        {
            GlideApp.with(profile_image.getContext()).load("https://sellerportal.perfectmandi.com/"+path).into(profile_image);


        }
        else
        {
            System.out.println(path);

 }


        storedown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean state=storedown.isChecked();

                if (!state)
                {

                    try {

                        storedown.setChecked(state);
                        AlertDialog.Builder builder = new AlertDialog.Builder(OPActivity.this);

                        // Set the message show for the Alert time
                        builder.setMessage("Are you sure you want to shut your store?");

                        // Set Alert Title
                        builder.setTitle("Alert !");

                        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                        builder.setCancelable(false);

                        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) ->
                        {

                            dbHelper.UpdateRow(storeid,"inactive");

                            new CloseFetch(storeid,"inactive").execute();
                            // When the user click yes button then app will close
                            //finish();
                        });

                        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                            // If user click no then dialog box is canceled.
                            storedown.setChecked(true);
                            dialog.cancel();
                        });

                        // Create the Alert dialog
                        AlertDialog alertDialog = builder.create();
                        // Show the Alert Dialog box
                        alertDialog.show();
                    }catch (Exception e)
                    {

                    }

                }
                else
                {
                    try {
                        storedown.setChecked(state);
                        AlertDialog.Builder builder = new AlertDialog.Builder(OPActivity.this);

                        // Set the message show for the Alert time
                        builder.setMessage("Are you sure you want to open your store?");

                        // Set Alert Title
                        builder.setTitle("Alert !");

                        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                        builder.setCancelable(false);

                        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) ->
                        {

                            dbHelper.UpdateRow(storeid,"active");

                            new CloseFetch(storeid,"active").execute();
                            // When the user click yes button then app will close
                           // finish();
                        });

                        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                            // If user click no then dialog box is canceled.
                            storedown.setChecked(false);

                            dialog.cancel();
                        });

                        // Create the Alert dialog
                        AlertDialog alertDialog = builder.create();
                        // Show the Alert Dialog box
                        alertDialog.show();
                    }
                    catch (Exception exception)
                    {

                    }


                }

            }
        });



    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void BarInit(BarChart barChart) {
       ArrayList<Integer> colors = new  ArrayList<>();

        colors.add(Color.rgb(201, 123, 28));
        colors.add(Color.rgb(201, 123, 28));
        colors.add(Color.rgb(201, 123, 28));
        colors.add(Color.rgb(201, 123, 28));
        colors.add(Color.rgb(201, 123, 28));
        colors.add(Color.rgb(201, 123, 28));
        colors.add(Color.rgb(201, 123, 28));
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, 44f));
        barEntries.add(new BarEntry(1f, 88f));
        barEntries.add(new BarEntry(2f, 41f));
        barEntries.add(new BarEntry(3f, 85f));
        barEntries.add(new BarEntry(4f, 96f));
        barEntries.add(new BarEntry(5f, 25f));
        barEntries.add(new BarEntry(6f, 10f));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        barDataSet.setValueTextColor(Color.rgb(201, 123, 28));


        barDataSet.setColors(colors);
        ArrayList<String> theDates = new ArrayList<>();
        //barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(theDates));
        BarData theData = new BarData(barDataSet);//----Line of error
        barChart.setData(theData);
        barChart.setBackgroundColor(Color.argb(0,201,123,28));
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);

 /*       barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.setDrawBorders(false);*/
        barChart.setTouchEnabled(true);
        barChart.setClickable(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);


        barChart.setDrawBorders(false);
        barChart.setDrawGridBackground(false);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawAxisLine(false);

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawLabels(false);
        barChart.getXAxis().setDrawAxisLine(false);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setDrawAxisLine(false);
    }
    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_screen_menu);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
      //  DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_addproduct: {
                //do somthing
                Intent addproductIntent = new Intent(OPActivity.this, ProductInformationActivity.class);
//userid
                addproductIntent.putExtra("userid", userid);
                addproductIntent.putExtra("username", name);
                addproductIntent.putExtra("session", session);
                startActivity(addproductIntent);
                break;
            }
            case R.id.nav_add_update: {
                Intent updateprofileIntent = new Intent(OPActivity.this, VendorProfileUpdateActivity.class);
                startActivity(updateprofileIntent);
                break;
            }
            case R.id.nav_mshelf: {
                Intent addproductIntent = new Intent(OPActivity.this, ManageShelf_Activity.class);
                addproductIntent.putExtra("userid", userid);
                addproductIntent.putExtra("username", name);
                addproductIntent.putExtra("session", session);
                startActivity(addproductIntent);
                break;
            }
            case R.id.nav_morder: {
                //do somthing

                Intent addproductIntent = new Intent(OPActivity.this, OrderDashboardActivity.class);
                addproductIntent.putExtra("userid", userid);
                addproductIntent.putExtra("username", name);
                addproductIntent.putExtra("session", session);
                startActivity(addproductIntent);
                break;
            }
            case R.id.nav_mbank: {
                //do somthing
                Intent manageDispatch = new Intent(OPActivity.this, vendorDispatchActivity.class);
                startActivity(manageDispatch);
                //Toast.makeText(OPActivity.this, "Manage Bank", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_mprices: {
                //do somthing
                Intent manage = new Intent(OPActivity.this, PriceUpdateActivity.class);
                startActivity(manage);
                //Toast.makeText(OPActivity.this, "Manage Prices", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.nav_salespromotion: {
                Intent addproductIntent = new Intent(OPActivity.this, PromotionProductActivity.class);
                startActivity(addproductIntent);

                break;
            }

            case R.id.nav_RFQ_:
            {
                startActivity(new Intent(OPActivity.this, ViewRFQActivity.class));
               // Toast.makeText(getApplicationContext(),"Ye Hona hai",Toast.LENGTH_LONG).show();
                break;
            }

        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(OPActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                url = new URL("https://sellerportal.perfectmandi.com/get_BI.php");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return e.toString();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {


            List<modelDashboard> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    //JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(i);
                    modelDashboard modeldashboard = new modelDashboard();
                    modeldashboard.Display_Name= json_data.getString("bi_Display_Name");
                    modeldashboard.Display_Type= json_data.getString("bi_Display_Type");





                    data.add(modeldashboard);
                }

                // Setup and Handover data to recyclerview

                adapterdashboard = new dashboardAdapter(OPActivity.this, data);
                progressDialog.dismiss();
                vendor_BI_data.setAdapter(adapterdashboard);
                progressDialog.dismiss();
                vendor_BI_data.setLayoutManager( new GridLayoutManager(OPActivity.this,2));

            } catch (Exception e)
            {

            }


        }

    }


    private class CloseFetch extends AsyncTask<String, String, String>
    {
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        String id,status;
        CloseFetch(String id,String status)
        {
           this.id=id;
           this.status=status;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                System.out.println("https://sellerportal.perfectmandi.com/shutshop.php?id="+id+"&status="+status);
                url = new URL("https://sellerportal.perfectmandi.com/shutshop.php?id="+id+"&status="+status);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return e.toString();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {


            Toast.makeText(OPActivity.this,result,Toast.LENGTH_LONG).show();

           /* List<modelDashboard> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    //JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(i);
                    modelDashboard modeldashboard = new modelDashboard();
                    modeldashboard.Display_Name= json_data.getString("bi_Display_Name");
                    modeldashboard.Display_Type= json_data.getString("bi_Display_Type");





                    data.add(modeldashboard);
                }

                // Setup and Handover data to recyclerview

                adapterdashboard = new dashboardAdapter(OPActivity.this, data);
              //  progressDialog.dismiss();
                vendor_BI_data.setAdapter(adapterdashboard);
                //progressDialog.dismiss();
                vendor_BI_data.setLayoutManager( new GridLayoutManager(OPActivity.this,2));

            } catch (Exception e)
            {

            }
*/

        }

    }
}