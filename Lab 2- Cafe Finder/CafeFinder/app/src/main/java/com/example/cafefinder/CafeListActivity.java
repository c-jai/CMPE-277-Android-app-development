package com.example.cafefinder;

import android.content.Intent;
import android.os.Bundle;

import com.example.cafefinder.recyclerview.ListItem;
import com.example.cafefinder.recyclerview.RecyclerListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CafeListActivity extends AppCompatActivity {

    ArrayList<ListItem> listItems = new ArrayList<ListItem>();
    RecyclerListAdapter recyclerListAdapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_list);


        Intent intent = getIntent();


        String[] testArrayNames = new String[]{"3 Below Theaters And Lounge", "Circle-A Skateboards and Coffee", "Whispers Cafe and Creperie",
                "Starbucks", "La Lune Sucrée Pastry Shop San Jose CA", "Snowman Hot Chocolate", "Coffee Kiosk", "InstaBrew"};

        Double[] testArrayRatings = new Double[]{4.6,4.6,4.3, 4.2, 4.0,4.0,4.0, 3.3};

        for(int i=0;i<testArrayNames.length;i++)
        {
            ListItem listItem = new ListItem();

            listItem.setCafeName(testArrayNames[i]);
            listItem.setRating(String.valueOf(testArrayRatings[i]));

            listItems.add(listItem);
        }

        recyclerListAdapter = new RecyclerListAdapter(listItems);

        recyclerView = findViewById(R.id.cafe_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerListAdapter);


    }

}
