package com.consumer.perfectmandii.Adapter.TopRelevantAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Activity.Loadmore.loadmoreActivity;
import com.consumer.perfectmandii.Adapter.SubCatogories.subItemAdapter_relevant;
import com.consumer.perfectmandii.Model.SubCategories.SubCategoriesRelevant;
import com.consumer.perfectmandii.R;

import java.util.List;

public class relevantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LayoutInflater layoutInflater;
    List<SubCategoriesRelevant> data;
    Context context;
    subItemAdapter_relevant subitem;

    String categoryName,userid,session;


    public relevantAdapter(Context context, List<SubCategoriesRelevant> data,String categoryName,String userid,String session) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.categoryName=categoryName;
        this.session=session;
        this.userid=userid;
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
        SubCategoriesRelevant current = data.get(position);

        if (current.search_type.equalsIgnoreCase("relevant"))
        {
            myHolder.categoryName_panel.setText(categoryName);
        }
        //top_selling
        else if (current.search_type.equalsIgnoreCase("top_selling"))
        {
            myHolder.categoryName_panel.setText("Top Selling");
        }
        else
        {
            myHolder.categoryName_panel.setText(current.search_type);
        }


        if (current.orientation.equalsIgnoreCase("vertical"))
        {
            subitem = new subItemAdapter_relevant(context, current.jsonArray);

            myHolder.recyclerView.setAdapter(subitem);
            myHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        }
        else
        {
            subitem = new subItemAdapter_relevant(context, current.jsonArray);

            myHolder.recyclerView.setAdapter(subitem);
            myHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        }


        myHolder.Loadmore_panel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context, loadmoreActivity.class);
                intent.putExtra("jsonArray", current.jsonArray.toString());
                intent.putExtra("userid",userid);
                intent.putExtra("session",session);
             /*   intent.putExtra("userid",current.);
                intent.putExtra("session",current.session);
                intent.putExtra("categoryname",current.name);*/
                intent.putExtra("categoryname",categoryName);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView categoryName_panel,Loadmore_panel;
        RecyclerView recyclerView;

        public MyHolder(@NonNull View itemView)
        {
            super(itemView);
            categoryName_panel = itemView.findViewById(R.id.categoryName_panel);
            recyclerView = itemView.findViewById(R.id.subitem);
            Loadmore_panel=itemView.findViewById(R.id.Loadmore_panel);
        }
    }
}