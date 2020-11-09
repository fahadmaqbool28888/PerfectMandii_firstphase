package com.consumer.perfectmandii.Adapter.TopRelevantAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Adapter.SubCatogories.subItemAdapter;
import com.consumer.perfectmandii.Model.SubCategories.SubCategories;
import com.consumer.perfectmandii.R;

import java.util.List;

public class toprelevantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LayoutInflater layoutInflater;
    List<SubCategories> data;
    Context context;
    subItemAdapter subitem;
    String orientation;


    public toprelevantAdapter(Context context, List<SubCategories> data) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }
    public toprelevantAdapter(Context context, List<SubCategories> data,String orientation) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.orientation=orientation;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.item_undercategory, parent, false);
        MyHolder myHolder = new MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        MyHolder myHolder = (MyHolder) holder;
        SubCategories current = data.get(position);
        //CategoyByProductModel current=data.get(position);
        myHolder.categoryName_panel.setText(current.name);


        System.out.println(String.valueOf(current.jsonArray.length()));
        // System.out.println(current.data);

        subitem = new subItemAdapter(context, current.jsonArray, current.userid, current.session,current.name);

        myHolder.recyclerView.setAdapter(subitem);
        myHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView categoryName_panel;
        RecyclerView recyclerView;

        public MyHolder(@NonNull View itemView)
        {
            super(itemView);
            categoryName_panel = itemView.findViewById(R.id.categoryName_panel);
            recyclerView = itemView.findViewById(R.id.subitem);
        }
    }
}