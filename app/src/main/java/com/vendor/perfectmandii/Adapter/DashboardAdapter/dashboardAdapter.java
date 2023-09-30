package com.vendor.perfectmandii.Adapter.DashboardAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile.AddStoreActivity;
import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.DashboardActivity;
import com.vendor.perfectmandii.Model.dashboardModel.modelDashboard;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.profile_Updates.ProductInformationActivity;

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
import java.util.Collections;
import java.util.List;

public class dashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<modelDashboard> data= Collections.emptyList();




    public dashboardAdapter(Context context, List<modelDashboard> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_dashboard, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        modelDashboard current=data.get(position);
        myHolder.dheading.setText(current.Display_Name);

        if (current.Display_Type.equalsIgnoreCase("text"))
        {
            myHolder.chart.setVisibility(View.GONE);

        }
        else if (current.Display_Type.equalsIgnoreCase("bar_graph"))
        {
            myHolder.chart.setVisibility(View.VISIBLE);
            BarInit(myHolder.chart);
            myHolder.dheading.setVisibility(View.GONE);
            myHolder.dvalue.setVisibility(View.GONE);


        }

    }
    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {

        TextView dheading,dvalue;
        BarChart chart;

        ImageView ivFish;
        public MyHolder(View itemView)
        {
            super(itemView);
            dheading= itemView.findViewById(R.id.dheading);
            dvalue= itemView.findViewById(R.id.dvalue);
            chart=itemView.findViewById(R.id.chart);

        }

    }
    private void BarInit(BarChart barChart) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, 44f));
        barEntries.add(new BarEntry(1f, 88f));
        barEntries.add(new BarEntry(2f, 41f));
        barEntries.add(new BarEntry(3f, 85f));
        barEntries.add(new BarEntry(4f, 96f));
        barEntries.add(new BarEntry(5f, 25f));
        barEntries.add(new BarEntry(6f, 10f));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        ArrayList<String> theDates = new ArrayList<>();
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(theDates));
        BarData theData = new BarData(barDataSet);//----Line of error
        barChart.setData(theData);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
    }

}