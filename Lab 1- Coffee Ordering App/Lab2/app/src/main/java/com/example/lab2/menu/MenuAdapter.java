package com.example.lab2.menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.R;
import com.example.lab2.payment.PaymentActivity;

import java.util.List;




public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    List<MenuItem> menuItemList;
    String username;
    String emailId;
    Context context;

    public MenuAdapter(List<MenuItem> list, String username, String emailId)
    {
        this.menuItemList = list;
        this.username = username;
        this.emailId = emailId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MenuItem menuItem = menuItemList.get(position);

        holder.menuTitle.setText(menuItem.getMenuTitle());
        holder.menuImage.setImageResource(menuItem.getMenuImage());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent;
                intent = new Intent(context, PaymentActivity.class);
                intent.putExtra("selectedCoffee", menuItem.getMenuTitle());
                intent.putExtra("username", username);
                intent.putExtra("emailId", emailId);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView menuImage;
        TextView menuTitle;
        CardView cv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            context = itemView.getContext();
            menuImage = itemView.findViewById(R.id.menu_image);
            menuTitle = itemView.findViewById(R.id.menu_title);
            cv = itemView.findViewById(R.id.cardview);
        }

    }
}
