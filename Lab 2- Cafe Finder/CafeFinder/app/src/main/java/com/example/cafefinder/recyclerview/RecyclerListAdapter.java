package com.example.cafefinder.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cafefinder.R;

import java.util.List;


public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {

    List<ListItem> cafeList;

    Context context;

    public RecyclerListAdapter(List<ListItem> list)
    {
        this.cafeList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ListItem listItem = cafeList.get(position);

        holder.cafeName.setText(listItem.getCafeName());
        holder.cafeRating.setText(listItem.getRating());



    }

    @Override
    public int getItemCount() {
        return cafeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView cafeName;
        TextView cafeRating;
        CardView cv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            context = itemView.getContext();
            cafeName = itemView.findViewById(R.id.cafe_name);
            cafeRating = itemView.findViewById(R.id.cafe_rating);
            cv = itemView.findViewById(R.id.cardview);
        }

    }
}
